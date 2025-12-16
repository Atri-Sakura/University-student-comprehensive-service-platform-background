package com.ruoyi.platform.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.platform.domain.*;
import com.ruoyi.platform.domain.enums.OperatorTypeEnum;
import com.ruoyi.platform.domain.enums.OrderStatusEnum;
import com.ruoyi.platform.domain.enums.PayStatusEnum;
import com.ruoyi.platform.mapper.*;
import com.ruoyi.platform.service.IOrderFlowService;
import com.ruoyi.platform.service.IWalletFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 订单流转服务实现类
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Service
public class OrderFlowServiceImpl implements IOrderFlowService {

    private static final Logger log = LoggerFactory.getLogger(OrderFlowServiceImpl.class);

    // Redis缓存Key前缀
    private static final String CACHE_ORDER_KEY = "order:main:";
    private static final String CACHE_MERCHANT_KEY = "merchant:base:";
    private static final String CACHE_RIDER_KEY = "rider:base:";
    private static final String CACHE_DELIVERY_KEY = "order:delivery:";
    private static final String LOCK_ORDER_KEY = "lock:order:";

    // 缓存过期时间
    private static final long CACHE_EXPIRE_SECONDS = 300; // 5分钟

    // 分布式锁基础过期时间，看门狗会在此基础上续期
    private static final long LOCK_EXPIRE_SECONDS = 30;

    private static final DefaultRedisScript<Long> RENEW_SCRIPT;
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    static {
        RENEW_SCRIPT = new DefaultRedisScript<>("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end", Long.class);
        UNLOCK_SCRIPT = new DefaultRedisScript<>("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end", Long.class);
    }

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private OrderTakeoutDetailMapper orderTakeoutDetailMapper;

    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;

    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;

    @Autowired
    private MerchantBaseMapper merchantBaseMapper;

    @Autowired
    private RiderBaseMapper riderBaseMapper;

    @Autowired
    private IWalletFlowService walletFlowService;

    @Autowired
    private MerchantGoodsMapper merchantGoodsMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 注入在 ThreadPoolConfig 中配置的调度线程池
    @Autowired
    @Qualifier("scheduledExecutorService")
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * 商家接单（仅外卖单：1->2）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int merchantAcceptOrder(Long merchantId, Long orderMainId) {
        String lockKey = LOCK_ORDER_KEY + orderMainId;
        String lockValue = UUID.randomUUID().toString();

        // 1. 获取分布式锁（带看门狗续期功能）
        WatchDog watchDog = lockWithWatchDog(lockKey, lockValue);
        if (watchDog == null) {
            throw new ServiceException("订单正在处理中，请稍后再试");
        }

        try {
            // 2. 查询订单信息（优先从缓存）
            OrderMain order = getOrderFromCache(orderMainId);
            if (order == null) {
                throw new ServiceException("订单不存在");
            }

            if (order.getOrderType() == null || !Long.valueOf(1L).equals(order.getOrderType())) {
                throw new ServiceException("非外卖订单不支持商家接单");
            }

            // 3. 状态校验
            if (!OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
                throw new ServiceException("订单状态不正确，当前状态：" + order.getOrderStatus());
            }

            if (!PayStatusEnum.PAID.getCode().equals(order.getPayStatus())) {
                throw new ServiceException("订单未支付");
            }

            // 4. 更新订单状态
            OrderMain updateOrder = new OrderMain();
            updateOrder.setOrderMainId(orderMainId);
            updateOrder.setOrderStatus(OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode());
            updateOrder.setUpdateTime(DateUtils.getNowDate());
            int result = orderMainMapper.updateOrderMain(updateOrder);

            if (result == 0) {
                throw new ServiceException("订单状态更新失败");
            }

            // 5. 更新明细结算状态
            OrderTakeoutDetail queryDetail = new OrderTakeoutDetail();
            queryDetail.setOrderMainId(orderMainId);
            List<OrderTakeoutDetail> details = orderTakeoutDetailMapper.selectOrderTakeoutDetailList(queryDetail);

            if (!details.isEmpty()) {
                orderTakeoutDetailMapper.updateBatchSettleStatus(details);
            }

            // 6. 清除缓存
            clearOrderCache(orderMainId);

            // 7. 异步记录日志
            final String merchantName = getMerchantNameFromCache(merchantId);
            executeAfterCommit(() -> asyncSaveStatusLog(
                    orderMainId,
                    OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode(),
                    OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode(),
                    OperatorTypeEnum.MERCHANT,
                    merchantId,
                    merchantName,
                    "商家接单"
            ));

            log.info("商家接单成功，订单号：{}，商家ID：{}", order.getOrderNo(), merchantId);
            return result;
        } finally {
            // 安全释放锁
            unlockWithWatchDog(watchDog);
        }
    }

    /**
     * 商家拒单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int merchantRejectOrder(Long merchantId, Long orderMainId, String refuseReason) {
        String lockKey = LOCK_ORDER_KEY + orderMainId;
        String lockValue = UUID.randomUUID().toString();

        // 1. 获取分布式锁
        WatchDog watchDog = lockWithWatchDog(lockKey, lockValue);
        if (watchDog == null) {
            throw new ServiceException("订单正在处理中，请稍后再试");
        }

        try {
            OrderMain order = getOrderFromCache(orderMainId);
            if (order == null) {
                throw new ServiceException("订单不存在");
            }

            if (order.getOrderType() == null || !Long.valueOf(1L).equals(order.getOrderType())) {
                throw new ServiceException("非外卖订单不支持商家拒单");
            }

            // 校验权限
            OrderTakeoutDetail queryDetail = new OrderTakeoutDetail();
            queryDetail.setOrderMainId(orderMainId);
            List<OrderTakeoutDetail> details = orderTakeoutDetailMapper.selectOrderTakeoutDetailList(queryDetail);
            if (details.isEmpty() || !details.get(0).getMerchantId().equals(merchantId)) {
                throw new ServiceException("无权操作此订单");
            }

            if (!OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
                throw new ServiceException("订单状态不正确，无法拒单");
            }

            // 更新状态
            OrderMain updateOrder = new OrderMain();
            updateOrder.setOrderMainId(orderMainId);
            updateOrder.setOrderStatus(OrderStatusEnum.REJECTED.getCode());
            updateOrder.setPayStatus(PayStatusEnum.REFUNDING.getCode());
            updateOrder.setCancelReason(refuseReason);
            updateOrder.setCancelOperator("商家");
            updateOrder.setUpdateTime(DateUtils.getNowDate());
            int result = orderMainMapper.updateOrderMain(updateOrder);

            if (result == 0) {
                throw new ServiceException("订单状态更新失败");
            }

            // 退款逻辑
            walletFlowService.refundUser(order.getUserId(), orderMainId, order.getPayAmount());

            OrderMain updatePayStatus = new OrderMain();
            updatePayStatus.setOrderMainId(orderMainId);
            updatePayStatus.setPayStatus(PayStatusEnum.REFUNDED.getCode());
            updatePayStatus.setUpdateTime(DateUtils.getNowDate());
            orderMainMapper.updateOrderMain(updatePayStatus);

            clearOrderCache(orderMainId);

            // 异步恢复库存
            final List<OrderTakeoutDetail> detailsCopy = details;
            executeAfterCommit(() -> asyncRestoreStock(detailsCopy));

            // 异步日志
            final String merchantName = getMerchantNameFromCache(merchantId);
            executeAfterCommit(() -> asyncSaveStatusLog(
                    orderMainId,
                    OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode(),
                    OrderStatusEnum.REJECTED.getCode(),
                    OperatorTypeEnum.MERCHANT,
                    merchantId,
                    merchantName,
                    "商家拒单：" + refuseReason
            ));

            return result;
        } finally {
            unlockWithWatchDog(watchDog);
        }
    }

    /**
     * 骑手接单（外卖/跑腿：2->3）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int riderAcceptOrder(Long riderId, Long orderMainId) {
        String lockKey = LOCK_ORDER_KEY + orderMainId;
        String lockValue = UUID.randomUUID().toString();

        WatchDog watchDog = lockWithWatchDog(lockKey, lockValue);
        if (watchDog == null) {
            throw new ServiceException("订单正在处理中，请稍后再试");
        }

        try {
            OrderMain order = getOrderFromCache(orderMainId);
            if (order == null) {
                throw new ServiceException("订单不存在");
            }

            if (!OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
                throw new ServiceException("订单状态不正确，无法接单");
            }

            OrderDelivery delivery = getDeliveryFromCache(orderMainId);
            if (delivery == null) {
                throw new ServiceException("配送记录不存在");
            }

            if (delivery.getRiderId() != null && !delivery.getRiderId().equals(riderId)) {
                throw new ServiceException("订单已被其他骑手接单");
            }

            // 费用处理
            BigDecimal riderIncome = delivery.getRiderIncome();
            if (riderIncome == null || riderIncome.compareTo(BigDecimal.ZERO) <= 0) {
                riderIncome = order.getDeliveryFeeAmount();
                if (riderIncome == null || riderIncome.compareTo(BigDecimal.ZERO) <= 0) {
                    // 容错处理
                    riderIncome = BigDecimal.ZERO;
                }

                // 修复数据
                OrderDelivery updateFee = new OrderDelivery();
                updateFee.setOrderDeliveryId(delivery.getOrderDeliveryId());
                updateFee.setDeliveryFee(riderIncome);
                updateFee.setDeliveryFeeFromUser(riderIncome);
                updateFee.setRiderIncome(riderIncome);
                orderDeliveryMapper.updateOrderDelivery(updateFee);
            }

            String riderNickname = getRiderNicknameFromCache(riderId);

            // 更新配送记录
            OrderDelivery updateDelivery = new OrderDelivery();
            updateDelivery.setOrderDeliveryId(delivery.getOrderDeliveryId());
            updateDelivery.setRiderId(riderId);
            updateDelivery.setRiderNickname(riderNickname);
            updateDelivery.setReceiveTime(DateUtils.getNowDate());
            updateDelivery.setDeliveryStatus(1L);
            orderDeliveryMapper.updateOrderDelivery(updateDelivery);

            // 更新订单状态
            OrderMain updateOrder = new OrderMain();
            updateOrder.setOrderMainId(orderMainId);
            updateOrder.setOrderStatus(OrderStatusEnum.RIDER_PENDING_PICKUP.getCode());
            updateOrder.setUpdateTime(DateUtils.getNowDate());
            int orderResult = orderMainMapper.updateOrderMain(updateOrder);

            clearOrderCache(orderMainId);
            clearDeliveryCache(orderMainId);

            final BigDecimal finalRiderIncome = riderIncome;
            executeAfterCommit(() -> {
                asyncSaveStatusLog(
                        orderMainId,
                        OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode(),
                        OrderStatusEnum.RIDER_PENDING_PICKUP.getCode(),
                        OperatorTypeEnum.RIDER,
                        riderId,
                        riderNickname,
                        "骑手接单"
                );
                log.info("骑手接单成功，订单号：{}，骑手ID：{}，配送费：{}", order.getOrderNo(), riderId, finalRiderIncome);
            });

            return orderResult;
        } finally {
            unlockWithWatchDog(watchDog);
        }
    }

    /**
     * 骑手取货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int riderPickupOrder(Long riderId, Long orderMainId) {
        String lockKey = LOCK_ORDER_KEY + orderMainId;
        String lockValue = UUID.randomUUID().toString();

        WatchDog watchDog = lockWithWatchDog(lockKey, lockValue);
        if (watchDog == null) {
            throw new ServiceException("订单正在处理中，请稍后再试");
        }

        try {
            OrderMain order = getOrderFromCache(orderMainId);
            if (order == null) throw new ServiceException("订单不存在");

            if (!OrderStatusEnum.RIDER_PENDING_PICKUP.getCode().equals(order.getOrderStatus())) {
                throw new ServiceException("订单状态不正确，无法取货");
            }

            OrderDelivery delivery = getDeliveryFromCache(orderMainId);
            if (delivery == null) throw new ServiceException("配送记录不存在");

            if (!riderId.equals(delivery.getRiderId())) {
                throw new ServiceException("无权操作此订单");
            }

            // 更新订单状态
            OrderMain updateOrder = new OrderMain();
            updateOrder.setOrderMainId(orderMainId);
            updateOrder.setOrderStatus(OrderStatusEnum.DELIVERING.getCode());
            updateOrder.setUpdateTime(DateUtils.getNowDate());
            int result = orderMainMapper.updateOrderMain(updateOrder);

            // 更新配送状态
            OrderDelivery updateDelivery = new OrderDelivery();
            updateDelivery.setOrderDeliveryId(delivery.getOrderDeliveryId());
            updateDelivery.setPickTime(DateUtils.getNowDate());
            updateDelivery.setDeliveryStatus(2L);
            updateDelivery.setActualPickLongitude(order.getPickLongitude());
            updateDelivery.setActualPickLatitude(order.getPickLatitude());
            orderDeliveryMapper.updateOrderDelivery(updateDelivery);

            clearOrderCache(orderMainId);
            clearDeliveryCache(orderMainId);

            final String riderNickname = getRiderNicknameFromCache(riderId);
            executeAfterCommit(() -> {
                asyncSaveStatusLog(
                        orderMainId,
                        OrderStatusEnum.RIDER_PENDING_PICKUP.getCode(),
                        OrderStatusEnum.DELIVERING.getCode(),
                        OperatorTypeEnum.RIDER,
                        riderId,
                        riderNickname,
                        "骑手取货"
                );
            });

            return result;
        } finally {
            unlockWithWatchDog(watchDog);
        }
    }

    /**
     * 骑手送达
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int riderDeliverOrder(Long riderId, Long orderMainId) {
        String lockKey = LOCK_ORDER_KEY + orderMainId;
        String lockValue = UUID.randomUUID().toString();

        WatchDog watchDog = lockWithWatchDog(lockKey, lockValue);
        if (watchDog == null) {
            throw new ServiceException("订单正在处理中，请稍后再试");
        }

        try {
            OrderMain order = getOrderFromCache(orderMainId);
            if (order == null) throw new ServiceException("订单不存在");

            if (!OrderStatusEnum.DELIVERING.getCode().equals(order.getOrderStatus())) {
                throw new ServiceException("订单状态不正确，无法完成配送");
            }

            OrderDelivery delivery = getDeliveryFromCache(orderMainId);
            if (delivery == null) throw new ServiceException("配送记录不存在");

            if (!riderId.equals(delivery.getRiderId())) {
                throw new ServiceException("无权操作此订单");
            }

            // 更新订单状态
            OrderMain updateOrder = new OrderMain();
            updateOrder.setOrderMainId(orderMainId);
            updateOrder.setOrderStatus(OrderStatusEnum.COMPLETED.getCode());
            updateOrder.setCompleteTime(DateUtils.getNowDate());
            updateOrder.setUpdateTime(DateUtils.getNowDate());
            int result = orderMainMapper.updateOrderMain(updateOrder);

            // 更新配送状态
            OrderDelivery updateDelivery = new OrderDelivery();
            updateDelivery.setOrderDeliveryId(delivery.getOrderDeliveryId());
            updateDelivery.setDeliverTime(DateUtils.getNowDate());
            updateDelivery.setDeliveryStatus(3L);
            updateDelivery.setActualDeliverLongitude(order.getDeliverLongitude());
            updateDelivery.setActualDeliverLatitude(order.getDeliverLatitude());
            orderDeliveryMapper.updateOrderDelivery(updateDelivery);

            // 结算逻辑
            BigDecimal riderIncome = order.getDeliveryFeeAmount();
            if (riderIncome == null) riderIncome = BigDecimal.ZERO;

            try {
                walletFlowService.settleRider(riderId, orderMainId, riderIncome);

                OrderDelivery updateIncomeStatus = new OrderDelivery();
                updateIncomeStatus.setOrderDeliveryId(delivery.getOrderDeliveryId());
                updateIncomeStatus.setIncomeStatus(1L);
                orderDeliveryMapper.updateOrderDelivery(updateIncomeStatus);
            } catch (Exception e) {
                log.error("骑手结算失败", e);
                throw new ServiceException("骑手结算失败：" + e.getMessage());
            }

            clearOrderCache(orderMainId);
            clearDeliveryCache(orderMainId);

            // 商家结算
            if (order.getOrderType() == 1L && order.getMerchantId() != null) {
                final Long merchantId = order.getMerchantId();
                final BigDecimal goodsAmount = order.getGoodsAmount();
                executeAfterCommit(() -> asyncSettleMerchant(merchantId, orderMainId, goodsAmount));
            }

            final String riderNickname = getRiderNicknameFromCache(riderId);
            executeAfterCommit(() -> {
                asyncSaveStatusLog(
                        orderMainId,
                        OrderStatusEnum.DELIVERING.getCode(),
                        OrderStatusEnum.COMPLETED.getCode(),
                        OperatorTypeEnum.RIDER,
                        riderId,
                        riderNickname,
                        "骑手送达"
                );
            });

            return result;
        } finally {
            unlockWithWatchDog(watchDog);
        }
    }

    /**
     * 核心方法：加锁并启动看门狗
     */
    private WatchDog lockWithWatchDog(String lockKey, String lockValue) {
        // 尝试加锁
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);

        if (Boolean.TRUE.equals(lockAcquired)) {
            // 加锁成功，启动看门狗线程
            WatchDog watchDog = new WatchDog(lockKey, lockValue);
            watchDog.start();
            return watchDog;
        }
        return null;
    }

    /**
     * 核心方法：停止看门狗并安全释放锁
     */
    private void unlockWithWatchDog(WatchDog watchDog) {
        if (watchDog != null) {
            // 1. 停止续期任务
            watchDog.stop();

            // 2. 使用Lua脚本安全释放锁（校验UUID，只删除自己的锁）
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptText(script);
            redisScript.setResultType(Long.class);

            redisTemplate.execute(redisScript, Collections.singletonList(watchDog.key), watchDog.value);
        }
    }

    /**
     * 看门狗内部类：负责锁的自动续期
     */
    private class WatchDog {
        private final String key;
        private final String value;
        private ScheduledFuture<?> future;

        public WatchDog(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public void start() {
            // 在过期时间的 1/3 处进行续期（例如30秒过期，每10秒续期一次）
            long period = LOCK_EXPIRE_SECONDS / 3;

            this.future = scheduledExecutorService.scheduleAtFixedRate(() -> {
                try {
                    // 使用 Lua 脚本进行续期：检查值是否匹配，匹配则重置过期时间
                    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end";
                    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                    redisScript.setScriptText(script);
                    redisScript.setResultType(Long.class);

                    Long result = redisTemplate.execute(redisScript, Collections.singletonList(key), value, LOCK_EXPIRE_SECONDS);

                    // 如果返回0，说明锁已经不在了（可能Redis重启或被手动删除了），停止续期
                    if (result != null && result == 0) {
                        this.stop();
                    }
                } catch (Exception e) {
                    log.error("分布式锁续期失败，Key: {}", key, e);
                    // 续期失败不抛出异常，以免影响主业务，但记录日志
                }
            }, period, period, TimeUnit.SECONDS);
        }

        public void stop() {
            if (this.future != null && !this.future.isCancelled()) {
                this.future.cancel(true);
            }
        }
    }

    // ================= 以下为辅助方法和缓存操作 =================

    private OrderMain getOrderFromCache(Long orderMainId) {
        String cacheKey = CACHE_ORDER_KEY + orderMainId;
        OrderMain order = (OrderMain) redisTemplate.opsForValue().get(cacheKey);

        if (order == null) {
            order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
            if (order != null) {
                redisTemplate.opsForValue().set(cacheKey, order, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }
        }
        return order;
    }

    private OrderDelivery getDeliveryFromCache(Long orderMainId) {
        String cacheKey = CACHE_DELIVERY_KEY + orderMainId;
        OrderDelivery delivery = (OrderDelivery) redisTemplate.opsForValue().get(cacheKey);

        if (delivery == null) {
            OrderDelivery queryDelivery = new OrderDelivery();
            queryDelivery.setOrderMainId(orderMainId);
            List<OrderDelivery> deliveries = orderDeliveryMapper.selectOrderDeliveryList(queryDelivery);

            if (!deliveries.isEmpty()) {
                delivery = deliveries.get(0);
                redisTemplate.opsForValue().set(cacheKey, delivery, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }
        }
        return delivery;
    }

    private String getMerchantNameFromCache(Long merchantId) {
        String cacheKey = CACHE_MERCHANT_KEY + merchantId;
        String merchantName = (String) redisTemplate.opsForValue().get(cacheKey);

        if (merchantName == null) {
            MerchantBase merchant = merchantBaseMapper.selectMerchantBaseByMerchantBaseId(merchantId);
            merchantName = (merchant != null) ? merchant.getMerchantName() : "商家";
            redisTemplate.opsForValue().set(cacheKey, merchantName, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
        }
        return merchantName;
    }

    private String getRiderNicknameFromCache(Long riderId) {
        String cacheKey = CACHE_RIDER_KEY + riderId;
        String riderNickname = (String) redisTemplate.opsForValue().get(cacheKey);

        if (riderNickname == null) {
            RiderBase rider = riderBaseMapper.selectRiderBaseByRiderBaseId(riderId);
            riderNickname = (rider != null) ? rider.getNickname() : "骑手";
            redisTemplate.opsForValue().set(cacheKey, riderNickname, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
        }
        return riderNickname;
    }

    private void clearOrderCache(Long orderMainId) {
        redisTemplate.delete(CACHE_ORDER_KEY + orderMainId);
    }

    private void clearDeliveryCache(Long orderMainId) {
        redisTemplate.delete(CACHE_DELIVERY_KEY + orderMainId);
    }

    @Async
    public void asyncSaveStatusLog(Long orderMainId, Long oldStatus, Long newStatus,
                                   OperatorTypeEnum operatorType, Long operatorId,
                                   String operatorName, String remark) {
        try {
            OrderStatusLog statusLog = new OrderStatusLog();
            statusLog.setOrderMainId(orderMainId);
            statusLog.setOldStatus(oldStatus);
            statusLog.setNewStatus(newStatus);
            statusLog.setOperatorType(operatorType.getCode());
            statusLog.setOperatorId(operatorId);
            statusLog.setOperatorName(operatorName);
            statusLog.setRemark(remark);
            orderStatusLogMapper.insertOrderStatusLog(statusLog);
        } catch (Exception e) {
            log.error("订单状态日志记录失败，订单ID：{}", orderMainId, e);
        }
    }

    @Async
    public void asyncRestoreStock(List<OrderTakeoutDetail> details) {
        try {
            for (OrderTakeoutDetail detail : details) {
                merchantGoodsMapper.increaseStock(detail.getGoodsId(), detail.getQuantity());
            }
        } catch (Exception e) {
            log.error("商品库存恢复失败", e);
        }
    }

    @Async
    public void asyncSettleMerchant(Long merchantId, Long orderMainId, BigDecimal goodsAmount) {
        try {
            walletFlowService.settleMerchant(merchantId, orderMainId, goodsAmount);
        } catch (Exception e) {
            log.error("商家结算失败，订单ID：{}，商家ID：{}", orderMainId, merchantId, e);
        }
    }

    private void executeAfterCommit(Runnable runnable) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }
}