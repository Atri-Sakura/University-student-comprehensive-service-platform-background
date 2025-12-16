package com.ruoyi.platform.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.platform.domain.*;
import com.ruoyi.platform.domain.enums.OperatorTypeEnum;
import com.ruoyi.platform.domain.enums.OrderStatusEnum;
import com.ruoyi.platform.domain.enums.PayStatusEnum;
import com.ruoyi.platform.mapper.*;
import com.ruoyi.platform.service.IOrderFlowService;
import com.ruoyi.platform.service.IOrderNotifyService;
import com.ruoyi.platform.service.IWalletFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.List;
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
    private static final String CACHE_RIDER_KEY = "rider:base: ";
    private static final String CACHE_DELIVERY_KEY = "order:delivery:";
    private static final String LOCK_ORDER_KEY = "lock:order:";

    // 缓存过期时间
    private static final long CACHE_EXPIRE_SECONDS = 300; // 5分钟
    private static final long LOCK_EXPIRE_SECONDS = 10; // 分布式锁10秒

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

    /**
     * 商家接单（仅外卖单：1->2）
     *
     * @param merchantId 商家ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int merchantAcceptOrder(Long merchantId, Long orderMainId) {
        // 使用分布式锁防止并发问题
        String lockKey = LOCK_ORDER_KEY + orderMainId;
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(lockAcquired)) {
            throw new ServiceException("订单正在处理中，请稍后再试");
        }

        try {
            // 1. 查询订单信息（优先从缓存）
            OrderMain order = getOrderFromCache(orderMainId);
            if (order == null) {
                throw new ServiceException("订单不存在");
            }

            // 仅外卖订单允许商家接单
            if (order.getOrderType() == null || !Long.valueOf(1L).equals(order.getOrderType())) {
                throw new ServiceException("非外卖订单不支持商家接单");
            }

            // 2. 查询明细
            OrderTakeoutDetail queryDetail = new OrderTakeoutDetail();
            queryDetail.setOrderMainId(orderMainId);
            List<OrderTakeoutDetail> details = orderTakeoutDetailMapper.selectOrderTakeoutDetailList(queryDetail);

            // 3. 状态校验：必须是 1-商家待接单
            if (!OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
                throw new ServiceException("订单状态不正确，当前状态：" + order.getOrderStatus());
            }

            // 4. 支付状态校验：必须已支付
            if (! PayStatusEnum.PAID.getCode().equals(order.getPayStatus())) {
                throw new ServiceException("订单未支付");
            }

            // 5. 更新订单状态为 2-骑手待接单（核心同步操作）
            OrderMain updateOrder = new OrderMain();
            updateOrder.setOrderMainId(orderMainId);
            updateOrder.setOrderStatus(OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode());
            updateOrder.setUpdateTime(DateUtils.getNowDate());
            int result = orderMainMapper.updateOrderMain(updateOrder);

            if (result == 0) {
                throw new ServiceException("订单状态更新失败");
            }

            // 6. 更新订单明细结算状态（核心同步操作）
            for (OrderTakeoutDetail detail : details) {
                OrderTakeoutDetail updateDetail = new OrderTakeoutDetail();
                updateDetail.setOrderTakeoutDetailId(detail.getOrderTakeoutDetailId());
                updateDetail.setSettleStatus(1L);
                orderTakeoutDetailMapper.updateOrderTakeoutDetail(updateDetail);
            }

            // 清除订单缓存
            clearOrderCache(orderMainId);

            // 7. 异步记录状态变更日志（边缘异步操作）
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
            // 释放分布式锁
            redisTemplate.delete(lockKey);
        }
    }

    /**
     * 商家拒单
     *
     * @param merchantId 商家ID
     * @param orderMainId 订单ID
     * @param refuseReason 拒单原因
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int merchantRejectOrder(Long merchantId, Long orderMainId, String refuseReason) {
        // 使用分布式锁
        String lockKey = LOCK_ORDER_KEY + orderMainId;
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(lockAcquired)) {
            throw new ServiceException("订单正在处理中，请稍后再试");
        }

        try {
            // 1. 查询订单信息（优先从缓存）
            OrderMain order = getOrderFromCache(orderMainId);
            if (order == null) {
                throw new ServiceException("订单不存在");
            }

            // 仅外卖订单允许商家拒单
            if (order.getOrderType() == null || !Long.valueOf(1L).equals(order.getOrderType())) {
                throw new ServiceException("非外卖订单不支持商家拒单");
            }

            // 2. 权限校验
            OrderTakeoutDetail queryDetail = new OrderTakeoutDetail();
            queryDetail.setOrderMainId(orderMainId);
            List<OrderTakeoutDetail> details = orderTakeoutDetailMapper.selectOrderTakeoutDetailList(queryDetail);
            if (details.isEmpty() || !details.get(0).getMerchantId().equals(merchantId)) {
                throw new ServiceException("无权操作此订单");
            }

            // 3. 状态校验：必须是待接单状态
            if (!OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
                throw new ServiceException("订单状态不正确，无法拒单");
            }

            // 4. 更新订单状态为已拒单（核心同步操作）
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

            // 5. 退款给用户（核心同步操作）
            walletFlowService.refundUser(order.getUserId(), orderMainId, order.getPayAmount());

            // 6. 更新支付状态为已退款（核心同步操作）
            OrderMain updatePayStatus = new OrderMain();
            updatePayStatus.setOrderMainId(orderMainId);
            updatePayStatus.setPayStatus(PayStatusEnum.REFUNDED.getCode());
            updatePayStatus.setUpdateTime(DateUtils.getNowDate());
            orderMainMapper.updateOrderMain(updatePayStatus);

            // 清除订单缓存
            clearOrderCache(orderMainId);

            // 7. 异步恢复商品库存（边缘异步操作）
            final List<OrderTakeoutDetail> detailsCopy = details;
            executeAfterCommit(() -> asyncRestoreStock(detailsCopy));

            // 8. 异步记录状态变更日志
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

            log.info("商家拒单成功，订单号：{}，拒单原因：{}", order.getOrderNo(), refuseReason);
            return result;
        } finally {
            // 释放分布式锁
            redisTemplate.delete(lockKey);
        }
    }

    /**
     * 骑手接单（外卖/跑腿：2->3）
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int riderAcceptOrder(Long riderId, Long orderMainId) {
        // 使用分布式锁
        String lockKey = LOCK_ORDER_KEY + orderMainId;
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);

        if (Boolean.FALSE. equals(lockAcquired)) {
            throw new ServiceException("订单正在处理中，请稍后再试");
        }

        try {
            // 1. 查询订单信息（优先从缓存）
            OrderMain order = getOrderFromCache(orderMainId);
            if (order == null) {
                throw new ServiceException("订单不存在");
            }

            // 2. 状态校验：必须是 2-骑手待接单
            if (!OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
                throw new ServiceException("订单状态不正确，无法接单");
            }

            // 3. 查询配送记录（优先从缓存）
            OrderDelivery delivery = getDeliveryFromCache(orderMainId);
            if (delivery == null) {
                throw new ServiceException("配送记录不存在");
            }

            // 4. 校验是否已被其他骑手接单
            if (delivery.getRiderId() != null && ! delivery.getRiderId().equals(riderId)) {
                throw new ServiceException("订单已被其他骑手接单");
            }

            // 5. 校验并获取骑手收入（配送费）
            BigDecimal riderIncome = delivery.getRiderIncome();
            if (riderIncome == null || riderIncome.compareTo(BigDecimal.ZERO) <= 0) {
                riderIncome = order.getDeliveryFeeAmount();
                if (riderIncome == null || riderIncome.compareTo(BigDecimal.ZERO) <= 0) {
                    log.error("配送费异常，订单ID：{}，配送记录ID：{}", orderMainId, delivery.getOrderDeliveryId());
                    throw new ServiceException("配送费信息异常，无法接单");
                }

                // 修复配送记录费用字段
                OrderDelivery updateFee = new OrderDelivery();
                updateFee.setOrderDeliveryId(delivery.getOrderDeliveryId());
                updateFee.setDeliveryFee(riderIncome);
                updateFee.setDeliveryFeeFromUser(riderIncome);
                updateFee.setRiderIncome(riderIncome);
                orderDeliveryMapper.updateOrderDelivery(updateFee);

                log.info("自动修复配送费，订单ID：{}，配送费：{}", orderMainId, riderIncome);
            }

            // 6. 获取骑手信息（优先从缓存）
            String riderNickname = getRiderNicknameFromCache(riderId);

            // 7. 更新配送记录（核心同步操作）
            OrderDelivery updateDelivery = new OrderDelivery();
            updateDelivery.setOrderDeliveryId(delivery.getOrderDeliveryId());
            updateDelivery.setRiderId(riderId);
            updateDelivery.setRiderNickname(riderNickname);
            updateDelivery.setReceiveTime(DateUtils.getNowDate());
            updateDelivery.setDeliveryStatus(1L); // 1-已接单
            int deliveryResult = orderDeliveryMapper.updateOrderDelivery(updateDelivery);

            if (deliveryResult == 0) {
                throw new ServiceException("配送记录更新失败");
            }

            // 8. 更新订单状态为 3-骑手待取货（核心同步操作）
            OrderMain updateOrder = new OrderMain();
            updateOrder.setOrderMainId(orderMainId);
            updateOrder.setOrderStatus(OrderStatusEnum.RIDER_PENDING_PICKUP.getCode());
            updateOrder.setUpdateTime(DateUtils.getNowDate());
            int orderResult = orderMainMapper.updateOrderMain(updateOrder);

            if (orderResult == 0) {
                throw new ServiceException("订单状态更新失败");
            }

            // 清除缓存
            clearOrderCache(orderMainId);
            clearDeliveryCache(orderMainId);

            // 9. 异步记录状态变更日志
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
            // 释放分布式锁
            redisTemplate.delete(lockKey);
        }
    }

    /**
     * 骑手取货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int riderPickupOrder(Long riderId, Long orderMainId) {
        // 使用分布式锁
        String lockKey = LOCK_ORDER_KEY + orderMainId;
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);

        if (Boolean.FALSE.equals(lockAcquired)) {
            throw new ServiceException("订单正在处理中，请稍后再试");
        }

        try {
            // 1. 查询订单信息（优先从缓存）
            OrderMain order = getOrderFromCache(orderMainId);
            if (order == null) {
                throw new ServiceException("订单不存在");
            }

            // 2. 状态校验：必须是"骑手待取货"状态
            if (!OrderStatusEnum.RIDER_PENDING_PICKUP.getCode().equals(order.getOrderStatus())) {
                throw new ServiceException("订单状态不正确，无法取货，当前状态：" + order.getOrderStatus());
            }

            // 3. 查询配送记录并校验权限（优先从缓存）
            OrderDelivery delivery = getDeliveryFromCache(orderMainId);
            if (delivery == null) {
                throw new ServiceException("配送记录不存在");
            }

            if (!riderId.equals(delivery.getRiderId())) {
                throw new ServiceException("无权操作此订单");
            }

            // 4. 更新订单状态为配送中（核心同步操作）
            OrderMain updateOrder = new OrderMain();
            updateOrder.setOrderMainId(orderMainId);
            updateOrder.setOrderStatus(OrderStatusEnum.DELIVERING.getCode());
            updateOrder.setUpdateTime(DateUtils.getNowDate());
            int result = orderMainMapper.updateOrderMain(updateOrder);

            if (result == 0) {
                throw new ServiceException("订单状态更新失败");
            }

            // 5. 更新配送记录（核心同步操作）
            OrderDelivery updateDelivery = new OrderDelivery();
            updateDelivery.setOrderDeliveryId(delivery.getOrderDeliveryId());
            updateDelivery.setPickTime(DateUtils.getNowDate());
            updateDelivery.setDeliveryStatus(2L); // 2-已取货（配送中）
            updateDelivery.setActualPickLongitude(order.getPickLongitude());
            updateDelivery.setActualPickLatitude(order.getPickLatitude());
            orderDeliveryMapper.updateOrderDelivery(updateDelivery);

            // 清除缓存
            clearOrderCache(orderMainId);
            clearDeliveryCache(orderMainId);

            // 6. 异步记录状态变更日志
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
                log.info("骑手取货成功，订单号：{}，骑手ID：{}", order.getOrderNo(), riderId);
            });

            return result;
        } finally {
            // 释放分布式锁
            redisTemplate. delete(lockKey);
        }
    }

    /**
     * 骑手送达
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int riderDeliverOrder(Long riderId, Long orderMainId) {
        // 使用分布式锁
        String lockKey = LOCK_ORDER_KEY + orderMainId;
        Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);

        if (Boolean.FALSE. equals(lockAcquired)) {
            throw new ServiceException("订单正在处理中，请稍后再试");
        }

        try {
            // 1. 查询订单信息（优先从缓存）
            OrderMain order = getOrderFromCache(orderMainId);
            if (order == null) {
                throw new ServiceException("订单不存在");
            }

            // 2. 状态校验
            if (!OrderStatusEnum.DELIVERING.getCode().equals(order.getOrderStatus())) {
                throw new ServiceException("订单状态不正确，无法完成配送，当前状态：" + order.getOrderStatus());
            }

            // 3. 查询配送记录并校验权限（优先从缓存）
            OrderDelivery delivery = getDeliveryFromCache(orderMainId);
            if (delivery == null) {
                throw new ServiceException("配送记录不存在");
            }

            if (!riderId.equals(delivery.getRiderId())) {
                throw new ServiceException("无权操作此订单");
            }

            if (!Long.valueOf(2L).equals(delivery.getDeliveryStatus())) {
                throw new ServiceException("配送状态不正确，当前状态：" + delivery. getDeliveryStatus());
            }

            // 4. 更新订单状态为已完成（核心同步操作）
            OrderMain updateOrder = new OrderMain();
            updateOrder.setOrderMainId(orderMainId);
            updateOrder.setOrderStatus(OrderStatusEnum.COMPLETED.getCode());
            updateOrder.setCompleteTime(DateUtils.getNowDate());
            updateOrder.setUpdateTime(DateUtils.getNowDate());
            int result = orderMainMapper.updateOrderMain(updateOrder);

            if (result == 0) {
                throw new ServiceException("订单状态更新失败");
            }

            // 5. 更新配送记录（核心同步操作）
            OrderDelivery updateDelivery = new OrderDelivery();
            updateDelivery.setOrderDeliveryId(delivery.getOrderDeliveryId());
            updateDelivery.setDeliverTime(DateUtils.getNowDate());
            updateDelivery.setDeliveryStatus(3L); // 3-已送达
            updateDelivery.setActualDeliverLongitude(order.getDeliverLongitude());
            updateDelivery.setActualDeliverLatitude(order.getDeliverLatitude());
            orderDeliveryMapper.updateOrderDelivery(updateDelivery);

            // 6. 结算给骑手（核心同步操作）
            BigDecimal riderIncome = order.getDeliveryFeeAmount();
            if (riderIncome == null || riderIncome.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("配送费为空或为0，订单ID：{}", orderMainId);
                riderIncome = BigDecimal.ZERO;
            }

            try {
                walletFlowService.settleRider(riderId, orderMainId, riderIncome);
                log.info("骑手结算成功，订单ID：{}，骑手ID：{}，配送费：{}", orderMainId, riderId, riderIncome);

                // 更新配送记录的收入发放状态
                OrderDelivery updateIncomeStatus = new OrderDelivery();
                updateIncomeStatus.setOrderDeliveryId(delivery.getOrderDeliveryId());
                updateIncomeStatus.setIncomeStatus(1L);
                orderDeliveryMapper.updateOrderDelivery(updateIncomeStatus);

            } catch (Exception e) {
                log.error("骑手结算失败，订单ID：{}，骑手ID：{}", orderMainId, riderId, e);
                throw new ServiceException("骑手结算失败：" + e.getMessage());
            }

            // 清除缓存
            clearOrderCache(orderMainId);
            clearDeliveryCache(orderMainId);

            // 7. 异步结算给商家（边缘异步操作）
            if (order.getOrderType() == 1L && order.getMerchantId() != null) {
                final Long merchantId = order.getMerchantId();
                final BigDecimal goodsAmount = order.getGoodsAmount();
                executeAfterCommit(() -> asyncSettleMerchant(merchantId, orderMainId, goodsAmount));
            }

            // 8. 异步记录状态变更日志
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
                log.info("订单送达成功，订单号：{}，骑手ID：{}", order.getOrderNo(), riderId);
            });

            return result;
        } finally {
            // 释放分布式锁
            redisTemplate. delete(lockKey);
        }
    }

    /**
     * 从缓存获取订单信息
     */
    private OrderMain getOrderFromCache(Long orderMainId) {
        String cacheKey = CACHE_ORDER_KEY + orderMainId;
        OrderMain order = (OrderMain) redisTemplate.opsForValue().get(cacheKey);

        if (order == null) {
            order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
            if (order != null) {
                redisTemplate. opsForValue().set(cacheKey, order, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }
        }

        return order;
    }

    /**
     * 从缓存获取配送记录
     */
    private OrderDelivery getDeliveryFromCache(Long orderMainId) {
        String cacheKey = CACHE_DELIVERY_KEY + orderMainId;
        OrderDelivery delivery = (OrderDelivery) redisTemplate.opsForValue().get(cacheKey);

        if (delivery == null) {
            OrderDelivery queryDelivery = new OrderDelivery();
            queryDelivery.setOrderMainId(orderMainId);
            List<OrderDelivery> deliveries = orderDeliveryMapper.selectOrderDeliveryList(queryDelivery);

            if (!deliveries.isEmpty()) {
                delivery = deliveries.get(0);
                redisTemplate.opsForValue().set(cacheKey, delivery, CACHE_EXPIRE_SECONDS, TimeUnit. SECONDS);
            }
        }

        return delivery;
    }

    /**
     * 从缓存获取商家名称
     */
    private String getMerchantNameFromCache(Long merchantId) {
        String cacheKey = CACHE_MERCHANT_KEY + merchantId;
        String merchantName = (String) redisTemplate.opsForValue().get(cacheKey);

        if (merchantName == null) {
            MerchantBase merchant = merchantBaseMapper.selectMerchantBaseByMerchantBaseId(merchantId);
            if (merchant != null) {
                merchantName = merchant.getMerchantName();
                redisTemplate.opsForValue().set(cacheKey, merchantName, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            } else {
                merchantName = "商家";
            }
        }

        return merchantName;
    }

    /**
     * 从缓存获取骑手昵称
     */
    private String getRiderNicknameFromCache(Long riderId) {
        String cacheKey = CACHE_RIDER_KEY + riderId;
        String riderNickname = (String) redisTemplate.opsForValue().get(cacheKey);

        if (riderNickname == null) {
            RiderBase rider = riderBaseMapper.selectRiderBaseByRiderBaseId(riderId);
            if (rider != null) {
                riderNickname = rider. getNickname();
                redisTemplate.opsForValue().set(cacheKey, riderNickname, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
            } else {
                riderNickname = "骑手";
            }
        }

        return riderNickname;
    }

    /**
     * 清除订单缓存
     */
    private void clearOrderCache(Long orderMainId) {
        redisTemplate.delete(CACHE_ORDER_KEY + orderMainId);
    }

    /**
     * 清除配送记录缓存
     */
    private void clearDeliveryCache(Long orderMainId) {
        redisTemplate.delete(CACHE_DELIVERY_KEY + orderMainId);
    }

    /**
     * 异步保存订单状态变更日志
     */
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
            log.debug("订单状态日志记录成功，订单ID：{}", orderMainId);
        } catch (Exception e) {
            log.error("订单状态日志记录失败，订单ID：{}", orderMainId, e);
        }
    }

    /**
     * 异步恢复商品库存
     */
    @Async
    public void asyncRestoreStock(List<OrderTakeoutDetail> details) {
        try {
            for (OrderTakeoutDetail detail : details) {
                merchantGoodsMapper.increaseStock(detail.getGoodsId(), detail.getQuantity());
            }
            log.debug("商品库存恢复成功，商品数量：{}", details.size());
        } catch (Exception e) {
            log.error("商品库存恢复失败", e);
        }
    }

    /**
     * 异步结算给商家
     */
    @Async
    public void asyncSettleMerchant(Long merchantId, Long orderMainId, BigDecimal goodsAmount) {
        try {
            walletFlowService.settleMerchant(merchantId, orderMainId, goodsAmount);
            log.info("商家结算成功，订单ID：{}，商家ID：{}，金额：{}", orderMainId, merchantId, goodsAmount);
        } catch (Exception e) {
            log.error("商家结算失败，订单ID：{}，商家ID：{}", orderMainId, merchantId, e);
            // 商家结算失败不影响骑手结算，只记录日志
        }
    }

    /**
     * 事务提交后执行
     */
    private void executeAfterCommit(Runnable runnable) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            // 如果没有事务，直接执行
            runnable.run();
        }
    }
}