package com.ruoyi.platform.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.*;
import com.ruoyi.platform.domain.dto.CreateOrderDTO;
import com.ruoyi.platform.domain.dto.OrderItemDTO;
import com.ruoyi.platform.domain.dto.PayOrderDTO;
import com.ruoyi.platform.domain.dto.PrePayOrderDTO;
import com.ruoyi.platform.domain.enums.OperatorTypeEnum;
import com.ruoyi.platform.domain.enums.OrderStatusEnum;
import com.ruoyi.platform.domain.enums.PayStatusEnum;
import com.ruoyi.platform.domain.dto.CreateErrandOrderDto;
import com.ruoyi.platform.mapper.*;
import com.ruoyi.platform.merchant.mapper.MerchantAddressInfoMapper;
import com.ruoyi.platform.merchant.mapper.MerchantInfoMapper;
import com.ruoyi.platform.service.IOrderNotifyService;
import com.ruoyi.platform.service.IUserOrderService;
import com.ruoyi.platform.service.IWalletFlowService;
import com.ruoyi.common.utils.map.AMapGeocodeUtil;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户订单服务实现类
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Service
public class UserOrderServiceImpl implements IUserOrderService {

    private static final Logger log = LoggerFactory.getLogger(UserOrderServiceImpl.class);

    /** 预订单缓存过期时间（分钟） */
    private static final int PRE_ORDER_EXPIRE_MINUTES = 15;

    /** 地球半径（公里） */
    private static final double EARTH_RADIUS = 6371.0;

    /** Redis缓存Key前缀 */
    private static final String CACHE_MERCHANT_KEY = "merchant:info:";
    private static final String CACHE_USER_ADDRESS_KEY = "user:address: ";
    private static final String CACHE_GOODS_KEY = "goods:info:";
    private static final String CACHE_MERCHANT_ADDRESS_KEY = "merchant:address:";

    /** 缓存空对象标记（防止缓存穿透） */

    private static final String NULL_CACHE_VALUE = "NULL_VAL";

    private static final int NULL_CACHE_EXPIRE_MINUTES = 5;

    /** 缓存过期时间（小时） */
    private static final int CACHE_EXPIRE_HOURS = 2;

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private OrderTakeoutDetailMapper orderTakeoutDetailMapper;

    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;

    @Autowired
    private OrderStatusLogMapper orderStatusLogMapper;

    @Autowired
    private MerchantAddressInfoMapper merchantAddressInfoMapper;

    @Autowired
    private MerchantInfoMapper merchantInfoMapper;

    @Autowired
    private MerchantGoodsMapper merchantGoodsMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private IWalletFlowService walletFlowService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private OrderErrandDetailMapper orderErrandDetailMapper;

    @Autowired
    private MerchantGoodsImageMapper merchantGoodsImageMapper;

    @Autowired
    private SecondhandGoodsImageMapper secondhandGoodsImageMapper;

    @Autowired
    private IOrderNotifyService orderNotifyService;

    @Autowired
    private AMapGeocodeUtil aMapGeocodeUtil;

    /**
     * 创建预支付订单（只校验，不真正创建订单）
     */
    @Override
    public PrePayOrderDTO createPrePayOrder(CreateOrderDTO createOrderDTO) {
        // 1. 参数校验
        validateOrderParams(createOrderDTO);

        // 2. 批量查询商品并校验库存（使用Redis缓存）
        List<Long> goodsIds = createOrderDTO.getItems().stream()
                .map(OrderItemDTO::getGoodsId)
                .collect(Collectors.toList());

        Map<Long, MerchantGoods> goodsMap = batchGetGoodsByIds(goodsIds);

        for (OrderItemDTO item : createOrderDTO.getItems()) {
            MerchantGoods goods = goodsMap.get(item.getGoodsId());
            if (goods == null) {
                throw new ServiceException("商品不存在：" + item.getGoodsId());
            }
            if (goods.getStock() < item.getQuantity()) {
                throw new ServiceException("商品库存不足：" + goods.getGoodsName());
            }

            // 以数据库价格为准，覆盖前端传来的价格
            item.setGoodsPrice(goods.getPrice());
            item.setGoodsName(goods.getGoodsName());
        }

        // 3. 计算订单金额（使用缓存的商家配送费）
        OrderAmountInfo amountInfo = calculateOrderAmount(createOrderDTO);

        // 4. 生成预订单号
        String preOrderNo = generatePreOrderNo();

        // 5. 将预订单信息缓存到 Redis（15分钟过期）
        String cacheKey = getPreOrderCacheKey(preOrderNo);
        redisCache.setCacheObject(cacheKey, createOrderDTO, PRE_ORDER_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 6. 返回预支付信息
        PrePayOrderDTO prePayOrder = new PrePayOrderDTO();
        prePayOrder.setPreOrderNo(preOrderNo);
        prePayOrder.setTotalAmount(amountInfo.getTotalAmount());
        prePayOrder.setPayAmount(amountInfo.getPayAmount());
        prePayOrder.setGoodsAmount(amountInfo.getGoodsAmount());
        prePayOrder.setDeliveryFee(amountInfo.getDeliveryFee());
        prePayOrder.setDiscountAmount(amountInfo.getDiscountAmount());
        prePayOrder.setCreateTime(new Date());
        prePayOrder.setExpireTime(new Date(System.currentTimeMillis() + PRE_ORDER_EXPIRE_MINUTES * 60 * 1000));

        log.info("创建预支付订单成功，预订单号：{}，用户ID：{}，配送费：{}",
                preOrderNo, createOrderDTO.getUserId(), amountInfo.getDeliveryFee());

        return prePayOrder;
    }

    /**
     * 验证支付密码（仅余额支付时需要）
     */
    private void validatePayPassword(Long userId, Long payType, String payPassword) {
        if (payType != 1L) {
            log.info("非余额支付，跳过支付密码验证，用户ID：{}，支付方式：{}", userId, payType);
            return;
        }

        log.info("余额支付，开始验证支付密码，用户ID：{}", userId);

        if (payPassword == null || payPassword.trim().isEmpty()) {
            throw new ServiceException("余额支付需要输入支付密码");
        }

        UserBase user = userBaseMapper.selectUserBaseByUserBaseId(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        if (user.getPayPassword() == null || user.getPayPassword().isEmpty()) {
            throw new ServiceException("请先设置支付密码");
        }

        if (!SecurityUtils.matchesPassword(payPassword, user.getPayPassword())) {
            log.warn("支付密码验证失败，用户ID：{}", userId);
            throw new ServiceException("支付密码错误");
        }

        log.info("支付密码验证成功，用户ID：{}", userId);
    }

    /**
     * 支付并创建外卖订单（先扣款，再创建订单）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderMain payAndCreateOrder(Long userId, PayOrderDTO payOrderDTO) {
        String preOrderNo = payOrderDTO.getPreOrderNo();

        // 1. 从 Redis 获取预订单信息
        String cacheKey = getPreOrderCacheKey(preOrderNo);
        CreateOrderDTO createOrderDTO = redisCache.getCacheObject(cacheKey);
        if (createOrderDTO == null) {
            throw new ServiceException("订单已过期，请重新下单");
        }

        // 2. 校验用户ID是否匹配
        if (!createOrderDTO.getUserId().equals(userId)) {
            throw new ServiceException("订单信息异常");
        }

        validatePayPassword(userId, payOrderDTO.getPayType(), payOrderDTO.getPayPassword());

        // 3. 重新计算金额（防止金额被篡改）
        OrderAmountInfo amountInfo = calculateOrderAmount(createOrderDTO);

        // 4. 校验支付金额
        if(payOrderDTO.getPayAmount().compareTo(amountInfo.getPayAmount()) != 0){
            throw new ServiceException("支付金额不正确");
        }

        // 5. 生成正式订单号
        String orderNo = generateOrderNo();

        // 6. 先扣款（用户余额 → 平台钱包冻结）
        try {
            walletFlowService.userPay(userId, 0L, amountInfo.getPayAmount());
        } catch (Exception e) {
            log.error("用户余额扣款失败，预订单号：{}，用户ID：{}", preOrderNo, userId, e);
            throw new ServiceException("余额不足或支付失败");
        }

        try {
            // 7. 创建真正的订单（扣减库存）
            OrderMain order = createTakeoutOrderInternal(createOrderDTO, generateOrderNo(), amountInfo);

            // 8. 更新订单支付状态为已支付
            order.setPayStatus(PayStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            order.setPayType(payOrderDTO.getPayType());
            orderMainMapper.updateOrderMain(order);

            // 9. 删除预订单缓存
            redisCache.deleteObject(cacheKey);

            // 10. 异步记录支付日志（事务提交后执行）
            Long orderMainId = order.getOrderMainId();
            registerAfterCommitTask(() -> asyncSaveStatusLog(orderMainId, null,
                    OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode(),
                    OperatorTypeEnum.USER, userId, "用户支付并创建订单"));

            log.info("用户支付并创建订单成功，订单号：{}，用户ID：{}，配送费：{}",
                    order.getOrderNo(), userId, amountInfo.getDeliveryFee());

            return order;

        } catch (Exception e) {
            log.error("订单创建失败，开始退款，预订单号：{}，用户ID：{}", preOrderNo, userId, e);
            try {
                // TODO: 实现退款逻辑
                // walletFlowService.refundUser(userId, 0L, amountInfo.getPayAmount());
            } catch (Exception refundEx) {
                log.error("自动退款失败，需人工处理，预订单号：{}，用户ID：{}", preOrderNo, userId, refundEx);
            }
            throw new ServiceException("订单创建失败：" + e.getMessage());
        }
    }

    /**
     * 支付并创建跑腿订单（先扣款，再创建订单）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderMain payAndCreateErrandOrder(Long userId, PayOrderDTO payOrderDTO, Long userAddressId) {
        String preOrderNo = payOrderDTO.getPreOrderNo();

        // 1. 从 Redis 获取预订单信息
        String cacheKey = getPreOrderCacheKey(preOrderNo);
        CreateErrandOrderDto createOrderDTO = redisCache.getCacheObject(cacheKey);
        if (createOrderDTO == null) {
            throw new ServiceException("订单已过期，请重新下单");
        }

        // 2. 校验用户ID是否匹配
        if (!createOrderDTO.getUserId().equals(userId)) {
            throw new ServiceException("订单信息异常");
        }

        validatePayPassword(userId, payOrderDTO.getPayType(), payOrderDTO.getPayPassword());

        // 3. 重新计算金额（防止金额被篡改）
        OrderAmountInfo amountInfo = calculateErrandOrderAmount(createOrderDTO);

        // 4. 校验支付金额
        if (payOrderDTO.getPayAmount().compareTo(amountInfo.getPayAmount()) != 0) {
            throw new ServiceException("支付金额不正确");
        }

        // 5. 生成正式订单号
        String orderNo = generateOrderNo();

        // 6. 先扣款（用户余额 → 平台钱包冻结）
        try {
            walletFlowService.userPay(userId, 0L, amountInfo.getPayAmount());
        } catch (Exception e) {
            log.error("用户余额扣款失败，预订单号：{}，用户ID：{}", preOrderNo, userId, e);
            throw new ServiceException("余额不足或支付失败");
        }

        try {
            // 7. 创建真正的订单
            OrderMain order = createErrandOrderInternal(createOrderDTO, orderNo, amountInfo, userAddressId);

            // 8. 更新订单支付状态为已支付
            order.setPayStatus(PayStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            order.setPayType(payOrderDTO.getPayType());
            orderMainMapper.updateOrderMain(order);

            // 9. 删除预订单缓存
            redisCache.deleteObject(cacheKey);

            // 10. 异步记录日志和发送通知（事务提交后执行）
            Long orderMainId = order.getOrderMainId();
            registerAfterCommitTask(() -> {
                asyncSaveStatusLog(orderMainId, null, OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode(),
                        OperatorTypeEnum.USER, userId, "用户支付并创建跑腿订单");
                orderNotifyService.sendUserOrderSuccessNotify(order, userId);
            });

            log.info("用户支付并创建跑腿订单成功，订单号：{}，用户ID：{}，配送费：{}",
                    order.getOrderNo(), userId, amountInfo.getDeliveryFee());

            return order;

        } catch (Exception e) {
            log.error("跑腿订单创建失败，开始退款，预订单号：{}，用户ID：{}", preOrderNo, userId, e);
            try {
                // TODO: 实现退款逻辑
                // walletFlowService.refundUser(userId, 0L, amountInfo.getPayAmount());
            } catch (Exception refundEx) {
                log.error("自动退款失败，需人工处理，预订单号：{}，用户ID：{}", preOrderNo, userId, refundEx);
            }
            throw new ServiceException("订单创建失败：" + e.getMessage());
        }
    }

    /**
     * 创建跑腿预支付订单
     */
    @Override
    public PrePayOrderDTO createPrePayErrandOrder(CreateErrandOrderDto createErrandOrderDto) {
        validateErrandOrderParams(createErrandOrderDto);

        OrderAmountInfo amountInfo = calculateErrandOrderAmount(createErrandOrderDto);

        String preOrderNo = generatePreOrderNo();

        String cacheKey = getPreOrderCacheKey(preOrderNo);
        redisCache.setCacheObject(cacheKey, createErrandOrderDto, PRE_ORDER_EXPIRE_MINUTES, TimeUnit.MINUTES);

        PrePayOrderDTO prePayOrder = new PrePayOrderDTO();
        prePayOrder.setPreOrderNo(preOrderNo);
        prePayOrder.setTotalAmount(amountInfo.getTotalAmount());
        prePayOrder.setPayAmount(amountInfo.getPayAmount());
        prePayOrder.setGoodsAmount(amountInfo.getGoodsAmount());
        prePayOrder.setDeliveryFee(amountInfo.getDeliveryFee());
        prePayOrder.setDiscountAmount(amountInfo.getDiscountAmount());
        prePayOrder.setCreateTime(new Date());
        prePayOrder.setExpireTime(new Date(System.currentTimeMillis() + PRE_ORDER_EXPIRE_MINUTES * 60 * 1000));

        log.info("创建跑腿预支付订单成功，预订单号：{}，用户ID：{}，配送费：{}",
                preOrderNo, createErrandOrderDto.getUserId(), amountInfo.getDeliveryFee());

        return prePayOrder;
    }

    /**
     * 取消预支付订单
     */
    @Override
    public boolean cancelPrePayOrder(Long userId, String preOrderNo) {
        String cacheKey = getPreOrderCacheKey(preOrderNo);
        CreateOrderDTO createOrderDTO = redisCache.getCacheObject(cacheKey);
        if (createOrderDTO == null) {
            throw new ServiceException("预订单不存在或已过期");
        }

        if (!createOrderDTO.getUserId().equals(userId)) {
            throw new ServiceException("无权操作此订单");
        }

        redisCache.deleteObject(cacheKey);

        log.info("取消预支付订单成功，预订单号：{}，用户ID：{}", preOrderNo, userId);

        return true;
    }

    /**
     * 取消跑腿预支付订单
     */
    @Override
    public boolean cancelPrePayErrandOrder(Long userId, String preOrderNo) {
        String cacheKey = getPreOrderCacheKey(preOrderNo);
        CreateErrandOrderDto createOrderDTO = redisCache.getCacheObject(cacheKey);
        if (createOrderDTO == null) {
            throw new ServiceException("预订单不存在或已过期");
        }

        if (!createOrderDTO.getUserId().equals(userId)) {
            throw new ServiceException("无权操作此订单");
        }

        redisCache.deleteObject(cacheKey);

        log.info("取消跑腿预支付订单成功，预订单号：{}，用户ID：{}", preOrderNo, userId);

        return true;
    }

    /**
     * 用户取消订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelOrder(Long userId, Long orderMainId, String cancelReason) {
        // 1. 查询订单
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 校验订单归属
        if (!order.getUserId().equals(userId)) {
            throw new ServiceException("无权操作此订单");
        }

        // 3. 校验订单状态（只有待接单状态可以取消）
        if (!OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
            throw new ServiceException("当前订单状态不允许取消");
        }

        // 4. 更新订单状态为已取消
        OrderMain updateOrder = new OrderMain();
        updateOrder.setOrderMainId(orderMainId);
        updateOrder.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        updateOrder.setCancelReason(cancelReason);
        updateOrder.setCancelOperator("用户");
        updateOrder.setUpdateTime(new Date());
        int result = orderMainMapper.updateOrderMain(updateOrder);

        if (result > 0) {
            // 5. 恢复商品库存
            restoreGoodsStock(orderMainId);

            // 6. 退款给用户
            try {
                walletFlowService.refundUser(userId, orderMainId, order.getPayAmount());
            } catch (Exception e) {
                log.error("取消订单退款失败，订单ID：{}，用户ID：{}", orderMainId, userId, e);
                throw new ServiceException("退款失败，请联系客服");
            }

            // 7. 异步记录状态变更日志和发送通知（事务提交后执行）
            registerAfterCommitTask(() -> {
                asyncSaveStatusLog(orderMainId, OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode(),
                        OrderStatusEnum.CANCELED.getCode(), OperatorTypeEnum.USER, userId,
                        "用户取消订单：" + cancelReason);
                orderNotifyService.cancelOrderNotify(orderMainId);
            });

            log.info("用户取消订单成功，订单ID：{}，用户ID：{}", orderMainId, userId);
        }

        return result;
    }

    /**
     * 用户确认收货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int confirmReceive(Long userId, Long orderMainId) {
        // 1. 查询订单
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        log.info("订单ID：{}，用户ID：{}，当前状态：{}", orderMainId, userId, order.getOrderStatus());
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 校验订单归属
        if (!order.getUserId().equals(userId)) {
            throw new ServiceException("无权操作此订单");
        }

        // 3. 校验订单状态（必须是配送中状态）
        if (!OrderStatusEnum.DELIVERING.getCode().equals(order.getOrderStatus())) {
            throw new ServiceException("订单状态不正确");
        }

        // 4. 更新订单状态为已完成
        OrderMain updateOrder = new OrderMain();
        updateOrder.setOrderMainId(orderMainId);
        updateOrder.setOrderStatus(OrderStatusEnum.COMPLETED.getCode());
        updateOrder.setCompleteTime(new Date());
        updateOrder.setUpdateTime(new Date());
        int result = orderMainMapper.updateOrderMain(updateOrder);

        if (result > 0) {
            // 5. 结算给商家
            try {
                walletFlowService.settleMerchant(order.getMerchantId(), orderMainId, order.getGoodsAmount());
            } catch (Exception e) {
                log.error("结算给商家失败，订单ID：{}，商家ID：{}", orderMainId, order.getMerchantId(), e);
            }

            // 6. 异步记录状态变更日志和发送通知（事务提交后执行）
            registerAfterCommitTask(() -> {
                asyncSaveStatusLog(orderMainId, OrderStatusEnum.DELIVERING.getCode(),
                        OrderStatusEnum.COMPLETED.getCode(), OperatorTypeEnum.USER, userId,
                        "用户确认收货");
                orderNotifyService.sendOrderFinishNotify(orderMainId);
            });

            log.info("用户确认收货成功，订单ID：{}，用户ID：{}", orderMainId, userId);
        }

        return result;
    }

    /**
     * 用户确认收货（跑腿订单）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int confirmReceiveErrand(Long userId, Long orderMainId, Long riderId) {
        // 1. 查询订单
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        log.info("订单ID：{}，用户ID：{}，当前状态：{}", orderMainId, userId, order.getOrderStatus());
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 校验订单归属
        if (!order.getUserId().equals(userId)) {
            throw new ServiceException("无权操作此订单");
        }

        // 4. 更新订单状态为已完成
        OrderMain updateOrder = new OrderMain();
        updateOrder.setOrderMainId(orderMainId);
        updateOrder.setOrderStatus(OrderStatusEnum.COMPLETED.getCode());
        updateOrder.setCompleteTime(new Date());
        updateOrder.setUpdateTime(new Date());
        int result = orderMainMapper.updateOrderMain(updateOrder);

        if (result > 0) {
            // 5. 结算给骑手（不捕获异常，让异常向上传播）
            walletFlowService.settleRider(riderId, orderMainId, order.getDeliveryFeeAmount());

            // 6. 异步记录状态变更日志和发送通知（事务提交后执行）
            registerAfterCommitTask(() -> {
                asyncSaveStatusLog(orderMainId, OrderStatusEnum.DELIVERING.getCode(),
                        OrderStatusEnum.COMPLETED.getCode(), OperatorTypeEnum.USER, userId,
                        "用户确认收货");
                orderNotifyService.sendOrderFinishNotify(orderMainId);
            });

            log.info("用户确认收货成功，订单ID：{}，用户ID：{}", orderMainId, userId);
        }

        return result;
    }

    /**
     * 内部方法：真正创建外卖订单（带缓存优化）
     */
    private OrderMain createTakeoutOrderInternal(CreateOrderDTO createOrderDTO, String orderNo, OrderAmountInfo amountInfo) {
        // 1. 从缓存获取商家地址
        MerchantAddress merchantAddress = getCachedMerchantAddress(createOrderDTO.getMerchantId());
        if (merchantAddress == null) {
            throw new ServiceException("商家地址不存在");
        }

        String merchantFullAddress = merchantAddress.getProvince() + merchantAddress.getCity()
                + merchantAddress.getDistrict() + merchantAddress.getDetailAddress();

        // 2. 从缓存获取用户收货地址
        UserAddress userAddress = getCachedUserAddress(createOrderDTO.getDeliverAddressId());
        if (userAddress == null) {
            throw new ServiceException("收货地址不存在，地址ID：" + createOrderDTO.getDeliverAddressId());
        }

        // 3. 异步获取经纬度
        CompletableFuture<BigDecimal[]> pickLocationFuture = CompletableFuture.supplyAsync(() ->
                getOrConvertLocation(merchantFullAddress, merchantAddress.getCity(), null, null)
        );

        CompletableFuture<BigDecimal[]> deliverLocationFuture = CompletableFuture.supplyAsync(() ->
                getOrConvertLocation(
                        userAddress.getProvince() + userAddress.getCity() + userAddress.getDistrict() + userAddress.getDetailAddress(),
                        userAddress.getCity(),
                        userAddress.getUserAddressId(),
                        userAddress
                )
        );

        // 4. 获取订单缩略图
        String orderThumbnail = null;
        if (createOrderDTO.getItems() != null && !createOrderDTO.getItems().isEmpty()) {
            Long firstGoodsId = createOrderDTO.getItems().get(0).getGoodsId();
            orderThumbnail = getGoodsMainImage(firstGoodsId);
        }

        // 5. 等待经纬度转换完成
        BigDecimal[] pickLocation = pickLocationFuture.join();
        BigDecimal[] deliverLocation = deliverLocationFuture.join();

        // 6. 创建订单主表记录
        OrderMain orderMain = new OrderMain();
        orderMain.setOrderMainId(com.ruoyi.platform.chat.utils.SnowflakeIdGenerator.getInstance().nextId());
        orderMain.setOrderNo(orderNo);
        orderMain.setUserId(createOrderDTO.getUserId());
        orderMain.setUserNickname(createOrderDTO.getUserNickname());
        orderMain.setMerchantId(createOrderDTO.getMerchantId());
        orderMain.setOrderType(1L); // 1-外卖单

        // 金额信息
        orderMain.setTotalAmount(amountInfo.getTotalAmount());
        orderMain.setPayAmount(amountInfo.getPayAmount());
        orderMain.setDiscountAmount(amountInfo.getDiscountAmount());
        orderMain.setPlatformHoldAmount(amountInfo.getPayAmount());
        orderMain.setGoodsAmount(amountInfo.getGoodsAmount());
        orderMain.setDeliveryFeeAmount(amountInfo.getDeliveryFee());

        // 订单缩略图
        orderMain.setOrderThumbnail(orderThumbnail);

        // 支付状态（已支付）
        orderMain.setPayStatus(PayStatusEnum.PAID.getCode());
        orderMain.setPayTime(new Date());
        orderMain.setPayType(1L);

        // 订单状态（待接单）
        orderMain.setOrderStatus(OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode());

        // 取货地址（商家地址 + 转换的经纬度）
        orderMain.setPickAddressId(merchantAddress.getMerchantAddressId());
        orderMain.setPickAddress(merchantFullAddress);
        orderMain.setPickContact(merchantAddress.getContactPerson());
        orderMain.setPickPhone(merchantAddress.getContactPhone());
        orderMain.setPickLongitude(pickLocation[0]);
        orderMain.setPickLatitude(pickLocation[1]);

        // 送货地址（从数据库查询的用户地址）
        orderMain.setDeliverAddressId(userAddress.getUserAddressId());
        orderMain.setDeliverAddress(
                userAddress.getProvince() + userAddress.getCity() +
                        userAddress.getDistrict() + userAddress.getDetailAddress()
        );
        orderMain.setDeliverContact(userAddress.getReceiver());
        orderMain.setDeliverPhone(userAddress.getPhone());
        orderMain.setDeliverLongitude(deliverLocation[0]);
        orderMain.setDeliverLatitude(deliverLocation[1]);

        orderMain.setRemark(createOrderDTO.getRemark());
        orderMain.setCreateTime(new Date());
        orderMain.setUpdateTime(new Date());

        // 插入订单主表
        int mainResult = orderMainMapper.insertOrderMain(orderMain);
        if (mainResult == 0) {
            throw new ServiceException("创建订单失败");
        }

        // 7. 批量创建订单明细并扣减库存
        List<OrderTakeoutDetail> detailList = new ArrayList<>();
        for (OrderItemDTO item : createOrderDTO.getItems()) {
            MerchantGoods goods = merchantGoodsMapper.selectMerchantGoodsByMerchantGoodsId(item.getGoodsId());
            if (goods == null) {
                throw new ServiceException("商品不存在：" + item.getGoodsId());
            }

            item.setGoodsPrice(goods.getPrice());

            int stockResult = merchantGoodsMapper.decreaseStock(item.getGoodsId(), item.getQuantity());
            if (stockResult == 0) {
                throw new ServiceException("扣减库存失败：" + goods.getGoodsName());
            }

            OrderTakeoutDetail detail = new OrderTakeoutDetail();
            detail.setOrderTakeoutDetailId(generateLongId());
            detail.setOrderMainId(orderMain.getOrderMainId());
            detail.setMerchantId(createOrderDTO.getMerchantId());
            detail.setMerchantName(createOrderDTO.getMerchantName());
            detail.setGoodsId(item.getGoodsId());
            detail.setGoodsName(goods.getGoodsName());
            detail.setGoodsPrice(goods.getPrice());
            detail.setQuantity(item.getQuantity());
            detail.setSubtotal(goods.getPrice().multiply(new BigDecimal(item.getQuantity())));
            detail.setGoodsSpec(item.getGoodsSpec());
            detail.setGoodsTags(item.getGoodsTags());
            detail.setSettleStatus(0L);

            detailList.add(detail);
        }

        // 批量插入订单明细
        detailList.forEach(orderTakeoutDetailMapper::insertOrderTakeoutDetail);

        // 8. 创建配送记录
        OrderDelivery delivery = new OrderDelivery();
        delivery.setOrderDeliveryId(generateLongId());
        delivery.setOrderMainId(orderMain.getOrderMainId());
        delivery.setDeliveryFee(amountInfo.getDeliveryFee());
        delivery.setDeliveryFeeFromUser(amountInfo.getDeliveryFee());
        delivery.setRiderIncome(amountInfo.getDeliveryFee());
        delivery.setIncomeStatus(0L);
        delivery.setAssignTime(new Date());
        delivery.setDeliveryStatus(0L);
        delivery. setActualPickLongitude(orderMain.getPickLongitude());
        delivery.setActualPickLatitude(orderMain.getPickLatitude());
        delivery.setActualDeliverLongitude(orderMain.getDeliverLongitude());
        delivery.setActualDeliverLatitude(orderMain.getDeliverLatitude());

        orderDeliveryMapper.insertOrderDelivery(delivery);

        // 9. 记录订单创建日志（同步，因为需要立即记录）
        saveStatusLog(orderMain.getOrderMainId(), null, OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode(),
                OperatorTypeEnum.USER, createOrderDTO.getUserId(),
                createOrderDTO.getUserNickname(), "用户支付并创建订单");

        return orderMain;
    }

    /**
     * 内部方法：创建跑腿订单（带缓存优化）
     */
    private OrderMain createErrandOrderInternal(CreateErrandOrderDto createOrderDTO, String orderNo,
                                                OrderAmountInfo amountInfo, Long userAddressId) {

        // 1. 拼接完整取货地址
        String pickFullAddress = createOrderDTO.getPickProvince()
                + createOrderDTO.getPickCity()
                + createOrderDTO.getPickDistrict()
                + createOrderDTO.getPickDetailAddress();

        // 2. 从缓存获取送货地址
        UserAddress deliverAddress = getCachedUserAddress(createOrderDTO.getDeliverAddressId());
        if (deliverAddress == null) {
            throw new ServiceException("收货地址不存在，地址ID：" + createOrderDTO.getDeliverAddressId());
        }

        String deliverFullAddress = deliverAddress.getProvince()
                + deliverAddress.getCity()
                + deliverAddress.getDistrict()
                + deliverAddress.getDetailAddress();

        // 3. 异步获取经纬度
        CompletableFuture<BigDecimal[]> pickLocationFuture = CompletableFuture.supplyAsync(() ->
                aMapGeocodeUtil.geocodeByFullAddress(
                        createOrderDTO.getPickProvince(),
                        createOrderDTO.getPickCity(),
                        createOrderDTO.getPickDistrict(),
                        createOrderDTO.getPickDetailAddress()
                )
        );

        CompletableFuture<BigDecimal[]> deliverLocationFuture = CompletableFuture.supplyAsync(() ->
                getOrConvertLocation(deliverFullAddress, deliverAddress.getCity(),
                        deliverAddress.getUserAddressId(), deliverAddress)
        );

        // 4. 等待经纬度转换完成
        BigDecimal[] pickLocation = pickLocationFuture. join();
        BigDecimal[] deliverLocation = deliverLocationFuture.join();

        if (pickLocation == null || pickLocation.length != 2) {
            log.warn("取货地址转经纬度失败，地址：{}", pickFullAddress);
            pickLocation = new BigDecimal[]{null, null};
        }

        if (deliverLocation == null || deliverLocation.length != 2) {
            throw new ServiceException("送货地址经纬度转换失败，请检查地址是否准确");
        }

        // 5. 创建订单主表记录
        OrderMain orderMain = new OrderMain();
        orderMain.setOrderMainId(com.ruoyi.platform.chat.utils.SnowflakeIdGenerator.getInstance().nextId());
        orderMain.setOrderNo(orderNo);
        orderMain.setUserId(createOrderDTO.getUserId());
        orderMain.setUserNickname(createOrderDTO.getUserNickname());
        orderMain.setOrderType(2L); // 2-跑腿单

        // 金额信息
        orderMain.setTotalAmount(amountInfo.getTotalAmount());
        orderMain.setPayAmount(amountInfo.getPayAmount());
        orderMain.setDiscountAmount(amountInfo.getDiscountAmount());
        orderMain.setPlatformHoldAmount(amountInfo.getPayAmount());
        orderMain.setGoodsAmount(amountInfo.getGoodsAmount());
        orderMain.setDeliveryFeeAmount(amountInfo.getDeliveryFee());

        // 订单缩略图（跑腿订单无缩略图）
        orderMain.setOrderThumbnail(null);

        // 支付状态（已支付）
        orderMain.setPayStatus(PayStatusEnum.PAID.getCode());
        orderMain.setPayTime(new Date());
        orderMain.setPayType(1L);

        // 订单状态（待接单）
        orderMain.setOrderStatus(OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode());

        // 取货地址（前端传入的详细地址 + 联系方式）
        orderMain.setPickAddressId(null);
        orderMain.setPickAddress(pickFullAddress);
        orderMain.setPickLongitude(pickLocation[0]);
        orderMain.setPickLatitude(pickLocation[1]);

        // 送货地址（从数据库查询的用户地址）
        orderMain.setDeliverAddressId(deliverAddress.getUserAddressId());
        orderMain.setDeliverAddress(deliverFullAddress);
        orderMain.setDeliverContact(deliverAddress.getReceiver());
        orderMain.setDeliverPhone(deliverAddress.getPhone());
        orderMain.setDeliverLongitude(deliverLocation[0]);
        orderMain.setDeliverLatitude(deliverLocation[1]);

        orderMain.setRemark(createOrderDTO.getRemark());
        orderMain.setCreateTime(new Date());
        orderMain.setUpdateTime(new Date());

        // 插入订单主表
        int mainResult = orderMainMapper.insertOrderMain(orderMain);
        if (mainResult == 0) {
            throw new ServiceException("创建订单失败");
        }

        // 6. 创建跑腿订单明细
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);

        OrderErrandDetail orderErrandDetail = new OrderErrandDetail();
        orderErrandDetail.setOrderMainId(orderMain.getOrderMainId());
        orderErrandDetail.setOrderErrandDetailId(
                com.ruoyi.platform.chat.utils.SnowflakeIdGenerator.getInstance().nextId()
        );
        orderErrandDetail.setErrandType(
                amountInfo.getGoodsAmount().compareTo(BigDecimal.ZERO) > 0 ? 2L : 1L
        );
        orderErrandDetail.setGoodsDesc(createOrderDTO.getGoodsDesc());
        orderErrandDetail.setExpectedTime(calendar.getTime());
        orderErrandDetail.setAdvanceAmount(orderMain.getGoodsAmount());
        orderErrandDetail.setTipAmount(orderMain.getDeliveryFeeAmount());

        orderErrandDetailMapper.insertOrderErrandDetail(orderErrandDetail);

        // 7. 创建配送记录
        OrderDelivery delivery = new OrderDelivery();
        delivery.setOrderDeliveryId(generateLongId());
        delivery.setOrderMainId(orderMain.getOrderMainId());
        delivery.setDeliveryFee(amountInfo.getDeliveryFee());
        delivery.setDeliveryFeeFromUser(amountInfo.getDeliveryFee());
        delivery.setRiderIncome(amountInfo.getDeliveryFee());
        delivery.setIncomeStatus(0L);
        delivery.setAssignTime(new Date());
        delivery.setDeliveryStatus(0L);
        delivery.setActualPickLongitude(orderMain.getPickLongitude());
        delivery.setActualPickLatitude(orderMain.getPickLatitude());
        delivery.setActualDeliverLongitude(orderMain.getDeliverLongitude());
        delivery.setActualDeliverLatitude(orderMain.getDeliverLatitude());

        orderDeliveryMapper.insertOrderDelivery(delivery);

        // 8. 记录订单创建日志（同步）
        saveStatusLog(
                orderMain.getOrderMainId(),
                null,
                OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode(),
                OperatorTypeEnum.USER,
                createOrderDTO.getUserId(),
                createOrderDTO.getUserNickname(),
                "用户支付并创建跑腿订单"
        );

        log.info("跑腿订单创建成功，订单号：{}，取货地址：{}，送货地址：{}",
                orderNo, pickFullAddress, deliverFullAddress);

        return orderMain;
    }

    /**
     * 查询外卖商品主图
     */
    private String getGoodsMainImage(Long goodsId) {
        if (goodsId == null) {
            return null;
        }

        try {
            MerchantGoodsImage image = merchantGoodsImageMapper.selectMainImageByGoodsId(goodsId);
            return image != null ? image.getImageUrl() : null;
        } catch (Exception e) {
            log.warn("查询商品主图失败，商品ID：{}", goodsId, e);
            return null;
        }
    }

    /**
     * 查询二手商品主图
     */
    private String getSecondhandGoodsMainImage(Long goodsId) {
        if (goodsId == null) {
            return null;
        }

        try {
            SecondhandGoodsImage image = secondhandGoodsImageMapper. selectMainImageByGoodsId(goodsId);
            return image != null ? image.getImageUrl() : null;
        } catch (Exception e) {
            log.warn("查询二手商品主图失败，商品ID：{}", goodsId, e);
            return null;
        }
    }

    /**
     * 恢复商品库存（取消订单时调用）
     */
    private void restoreGoodsStock(Long orderMainId) {
        OrderTakeoutDetail queryDetail = new OrderTakeoutDetail();
        queryDetail.setOrderMainId(orderMainId);
        List<OrderTakeoutDetail> details = orderTakeoutDetailMapper.selectOrderTakeoutDetailList(queryDetail);

        for (OrderTakeoutDetail detail : details) {
            int result = merchantGoodsMapper.increaseStock(detail.getGoodsId(), detail.getQuantity());
            if (result > 0) {
                log.info("恢复商品库存成功，商品ID：{}，数量：{}", detail.getGoodsId(), detail.getQuantity());
            } else {
                log.warn("恢复商品库存失败，商品ID：{}，数量：{}", detail.getGoodsId(), detail.getQuantity());
            }
        }
    }

    /**
     * 参数校验
     */
    private void validateOrderParams(CreateOrderDTO createOrderDTO) {
        if (createOrderDTO.getUserId() == null) {
            throw new ServiceException("用户ID不能为空");
        }
        if (createOrderDTO.getMerchantId() == null) {
            throw new ServiceException("商家ID不能为空");
        }
        if (createOrderDTO.getDeliverAddressId() == null) {
            throw new ServiceException("送货地址不能为空");
        }
        if (createOrderDTO.getItems() == null || createOrderDTO.getItems().isEmpty()) {
            throw new ServiceException("订单商品不能为空");
        }
    }

    /**
     * 参数跑腿校验
     */
    private void validateErrandOrderParams(CreateErrandOrderDto createOrderDTO) {
        if (createOrderDTO.getUserId() == null) {
            throw new ServiceException("用户ID不能为空");
        }

        if (createOrderDTO.getPickProvince() == null || createOrderDTO.getPickProvince().trim().isEmpty()) {
            throw new ServiceException("取货省份不能为空");
        }
        if (createOrderDTO.getPickCity() == null || createOrderDTO.getPickCity().trim().isEmpty()) {
            throw new ServiceException("取货城市不能为空");
        }
        if (createOrderDTO.getPickDistrict() == null || createOrderDTO.getPickDistrict().trim().isEmpty()) {
            throw new ServiceException("取货区县不能为空");
        }
        if (createOrderDTO.getPickDetailAddress() == null || createOrderDTO.getPickDetailAddress().trim().isEmpty()) {
            throw new ServiceException("取货详细地址不能为空");
        }

        if (createOrderDTO.getDeliverAddressId() == null) {
            throw new ServiceException("送货地址不能为空");
        }
    }

    /**
     * 计算订单金额（使用缓存的商家配送费）
     */
    private OrderAmountInfo calculateOrderAmount(CreateOrderDTO createOrderDTO) {
        OrderAmountInfo info = new OrderAmountInfo();

        // 1. 计算商品总金额
        BigDecimal goodsAmount = BigDecimal.ZERO;
        for (OrderItemDTO item : createOrderDTO.getItems()) {
            BigDecimal itemAmount = item.getGoodsPrice().multiply(new BigDecimal(item.getQuantity()));
            goodsAmount = goodsAmount.add(itemAmount);
        }

        // 2. 从缓存获取商家配送费
        BigDecimal deliveryFee = getCachedMerchantDeliveryFee(createOrderDTO.getMerchantId());

        // 3. 计算优惠金额（暂时为0，后续可扩展）
        BigDecimal discountAmount = BigDecimal.ZERO;

        // 4. 计算总金额 = 商品金额 + 配送费
        BigDecimal totalAmount = goodsAmount.add(deliveryFee);

        // 5. 计算实付金额 = 总金额 - 优惠金额
        BigDecimal payAmount = totalAmount.subtract(discountAmount);

        info.setGoodsAmount(goodsAmount);
        info.setDeliveryFee(deliveryFee);
        info.setDiscountAmount(discountAmount);
        info.setTotalAmount(totalAmount);
        info.setPayAmount(payAmount);

        return info;
    }

    /**
     * 跑腿专用
     */
    private OrderAmountInfo calculateErrandOrderAmount(CreateErrandOrderDto createOrderDTO) {
        OrderAmountInfo info = new OrderAmountInfo();

        BigDecimal goodsAmount = Optional.ofNullable(createOrderDTO.getGoodsPrice())
                .orElse(BigDecimal.ZERO);
        if (goodsAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("商品金额不能为负数");
        }

        BigDecimal deliveryFee = Optional.ofNullable(createOrderDTO.getDeliverAmount())
                .orElse(BigDecimal.ZERO);
        if (deliveryFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("配送费不能为负数");
        }

        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = goodsAmount.add(deliveryFee);
        BigDecimal payAmount = totalAmount.subtract(discountAmount);

        if (payAmount.compareTo(BigDecimal.ZERO) < 0) {
            payAmount = BigDecimal.ZERO;
        }

        info.setGoodsAmount(goodsAmount);
        info.setDeliveryFee(deliveryFee);
        info.setDiscountAmount(discountAmount);
        info.setTotalAmount(totalAmount);
        info.setPayAmount(payAmount);

        return info;
    }

    /**
     * 生成预订单编号
     */
    private String generatePreOrderNo() {
        String timestamp = DateUtils.dateTimeNow("yyyyMMddHHmmss");
        String random = String.format("%06d", (int)(Math.random() * 1000000));
        return "PRE" + timestamp + random;
    }

    /**
     * 生成正式订单编号
     */
    private String generateOrderNo() {
        String timestamp = DateUtils.dateTimeNow("yyyyMMddHHmmss");
        String random = String.format("%06d", (int)(Math.random() * 1000000));
        return "TO" + timestamp + random;
    }

    /**
     * 获取预订单缓存Key
     */
    private String getPreOrderCacheKey(String preOrderNo) {
        return "pre_order:" + preOrderNo;
    }

    /**
     * 保存订单状态变更日志（同步方法）
     */
    private void saveStatusLog(Long orderMainId, Long oldStatus, Long newStatus,
                               OperatorTypeEnum operatorType, Long operatorId,
                               String operatorName, String remark) {
        OrderStatusLog statusLog = new OrderStatusLog();
        statusLog.setOrderMainId(orderMainId);
        statusLog.setOldStatus(oldStatus);
        statusLog.setNewStatus(newStatus);
        statusLog.setOperatorType(operatorType.getCode());
        statusLog.setOperatorId(operatorId);
        statusLog.setOperatorName(operatorName);
        statusLog.setRemark(remark);
        orderStatusLogMapper.insertOrderStatusLog(statusLog);
    }

    /**
     * 异步保存订单状态变更日志
     */
    @Async
    protected void asyncSaveStatusLog(Long orderMainId, Long oldStatus, Long newStatus,
                                      OperatorTypeEnum operatorType, Long operatorId, String remark) {
        try {
            UserBase user = userBaseMapper.selectUserBaseByUserBaseId(operatorId);
            String operatorName = user != null ? user.getNickname() : "未知用户";
            saveStatusLog(orderMainId, oldStatus, newStatus, operatorType, operatorId, operatorName, remark);
        } catch (Exception e) {
            log.error("异步保存状态日志失败，订单ID：{}", orderMainId, e);
        }
    }

    /**
     * 生成Long类型的唯一ID
     */
    private Long generateLongId() {
        return com.ruoyi.platform.chat.utils.SnowflakeIdGenerator.getInstance().nextId();
    }

    /**
     * 批量获取商品信息（使用Redis Pipeline/MultiGet 优化）
     * 优化点：解决了N+1次网络请求问题，解决了缓存穿透问题
     */
    private Map<Long, MerchantGoods> batchGetGoodsByIds(List<Long> goodsIds) {
        if (goodsIds == null || goodsIds.isEmpty()) {
            return new HashMap<>();
        }

        // 去重
        List<Long> distinctIds = goodsIds.stream().distinct().collect(Collectors.toList());
        List<String> keys = distinctIds.stream()
                .map(id -> CACHE_GOODS_KEY + id)
                .collect(Collectors.toList());

        // 1. 批量获取缓存 (性能优化：一次网络IO)
        List<Object> cachedObjects = redisTemplate.opsForValue().multiGet(keys);

        Map<Long, MerchantGoods> result = new HashMap<>();
        List<Long> missIds = new ArrayList<>();

        for (int i = 0; i < distinctIds.size(); i++) {
            Long goodsId = distinctIds.get(i);
            Object obj = (cachedObjects != null && cachedObjects.size() > i) ? cachedObjects.get(i) : null;

            if (obj instanceof MerchantGoods) {
                result.put(goodsId, (MerchantGoods) obj);
            } else if (NULL_CACHE_VALUE.equals(obj)) {
                // 命中空缓存，不做处理，视为商品不存在
            } else {
                missIds.add(goodsId);
            }
        }

        // 2. 只有缓存缺失的才查库
        if (!missIds.isEmpty()) {
            for (Long id : missIds) {
                MerchantGoods goods = merchantGoodsMapper.selectMerchantGoodsByMerchantGoodsId(id);
                String cacheKey = CACHE_GOODS_KEY + id;
                if (goods != null) {
                    result.put(id, goods);
                    redisCache.setCacheObject(cacheKey, goods, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
                } else {
                    // 解决缓存穿透：缓存空值
                    redisCache.setCacheObject(cacheKey, NULL_CACHE_VALUE, NULL_CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
                }
            }
        }

        return result;
    }

    /**
     * 从缓存获取商家配送费
     * 优化点：增加空值缓存
     */
    private BigDecimal getCachedMerchantDeliveryFee(Long merchantId) {
        String cacheKey = CACHE_MERCHANT_KEY + merchantId;
        Object cacheObj = redisCache.getCacheObject(cacheKey);

        if (NULL_CACHE_VALUE.equals(cacheObj)) {
            return new BigDecimal("5.00"); // 默认值
        }

        MerchantBase merchant = (MerchantBase) cacheObj;

        if (merchant == null) {
            merchant = merchantInfoMapper.selectMerchantBaseByMerchantBaseId(merchantId);
            if (merchant != null) {
                redisCache.setCacheObject(cacheKey, merchant, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            } else {
                // 防止缓存穿透
                redisCache.setCacheObject(cacheKey, NULL_CACHE_VALUE, NULL_CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            }
        }

        if (merchant == null) {
            return new BigDecimal("5.00");
        }

        BigDecimal deliveryFee = merchant.getDeliveryFee();
        return deliveryFee != null ? deliveryFee : new BigDecimal("5.00");
    }

    /**
     * 从缓存获取商家地址
     * 优化点：增加空值缓存
     */
    private MerchantAddress getCachedMerchantAddress(Long merchantId) {
        String cacheKey = CACHE_MERCHANT_ADDRESS_KEY + merchantId;
        Object cacheObj = redisCache.getCacheObject(cacheKey);

        if (NULL_CACHE_VALUE.equals(cacheObj)) {
            return null;
        }

        MerchantAddress address = (MerchantAddress) cacheObj;

        if (address == null) {
            address = merchantAddressInfoMapper.selectMerchantAddressByMerchantBaseId(merchantId);
            if (address != null) {
                redisCache.setCacheObject(cacheKey, address, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            } else {
                redisCache.setCacheObject(cacheKey, NULL_CACHE_VALUE, NULL_CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            }
        }

        return address;
    }

    /**
     * 从缓存获取用户地址
     */
    private UserAddress getCachedUserAddress(Long addressId) {
        String cacheKey = CACHE_USER_ADDRESS_KEY + addressId;
        // 同样可以加上空值判断
        UserAddress address = redisCache.getCacheObject(cacheKey);

        if (address == null) {
            address = userAddressMapper.selectUserAddressByUserAddressId(addressId);
            if (address != null) {
                redisCache.setCacheObject(cacheKey, address, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
            }
        }

        return address;
    }

    /**
     * 获取或转换地理位置（带缓存和数据库更新）
     */
    private BigDecimal[] getOrConvertLocation(String fullAddress, String city, Long addressId, UserAddress userAddress) {
        // 如果用户地址已有经纬度，直接返回
        if (userAddress != null && userAddress.getLongitude() != null && userAddress.getLatitude() != null) {
            return new BigDecimal[]{userAddress.getLongitude(), userAddress.getLatitude()};
        }

        // 调用高德API转换
        try {
            BigDecimal[] location = aMapGeocodeUtil.geocode(fullAddress, city);
            if (location != null && location.length == 2) {
                log.info("地址转经纬度成功，地址：{}，经度：{}，纬度：{}", fullAddress, location[0], location[1]);

                // 异步更新数据库中的经纬度（如果有addressId）
                if (addressId != null && userAddress != null) {
                    CompletableFuture. runAsync(() -> {
                        try {
                            UserAddress updateAddress = new UserAddress();
                            updateAddress.setUserAddressId(addressId);
                            updateAddress.setLongitude(location[0]);
                            updateAddress.setLatitude(location[1]);
                            userAddressMapper.updateUserAddress(updateAddress);

                            // 更新缓存
                            String cacheKey = CACHE_USER_ADDRESS_KEY + addressId;
                            redisCache.deleteObject(cacheKey);
                        } catch (Exception e) {
                            log.error("异步更新用户地址经纬度失败，地址ID：{}", addressId, e);
                        }
                    });
                }

                return location;
            }
        } catch (Exception e) {
            log.error("地址转经纬度异常，地址：{}", fullAddress, e);
        }

        return new BigDecimal[]{null, null};
    }

    /**
     * 注册事务提交后执行的任务
     */
    private void registerAfterCommitTask(Runnable task) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    task. run();
                }
            });
        } else {
            // 如果没有事务，直接执行
            task.run();
        }
    }

    /**
     * 订单金额信息内部类
     */
    @lombok.Data
    private static class OrderAmountInfo {
        /** 商品金额 */
        private BigDecimal goodsAmount;

        /** 配送费 */
        private BigDecimal deliveryFee;

        /** 优惠金额 */
        private BigDecimal discountAmount;

        /** 总金额 = 商品金额 + 配送费 */
        private BigDecimal totalAmount;

        /** 实付金额 = 总金额 - 优惠金额 */
        private BigDecimal payAmount;
    }
}