package com.ruoyi.platform.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.platform.domain.*;
import com.ruoyi.platform.domain.dto.CreateOrderDTO;
import com.ruoyi.platform.domain.dto.OrderItemDTO;
import com.ruoyi.platform.domain.dto.PayOrderDTO;
import com.ruoyi.platform.domain.dto.PrePayOrderDTO;
import com.ruoyi.platform.domain.enums.OperatorTypeEnum;
import com.ruoyi.platform.domain.enums.OrderStatusEnum;
import com.ruoyi.platform.domain.enums.PayStatusEnum;
import com.ruoyi.platform.domain.vo.CreateErrandOrderDto;
import com.ruoyi.platform.mapper.*;
import com.ruoyi.platform.merchant.mapper.MerchantAddressInfoMapper;
import com.ruoyi.platform.merchant.mapper.MerchantInfoMapper;
import com.ruoyi.platform.service.IUserOrderService;
import com.ruoyi.platform.service.IWalletFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    private UserAddressMapper userAddressMapper;
    @Autowired
    private OrderErrandDetailMapper orderErrandDetailMapper;

    /**
     * 创建预支付订单（只校验，不真正创建订单）
     */
    @Override
    public PrePayOrderDTO createPrePayOrder(CreateOrderDTO createOrderDTO) {
        // 1. 参数校验
        validateOrderParams(createOrderDTO);

        // 2. 校验商品库存并使用数据库价格（但不扣减）
        for (OrderItemDTO item : createOrderDTO.getItems()) {
            MerchantGoods goods = merchantGoodsMapper.selectMerchantGoodsByMerchantGoodsId(item.getGoodsId());
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

        // 3. 计算订单金额（包含配送费）
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
     * 支付并创建订单（先扣款，再创建订单）
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

        // 3. 重新计算金额（防止金额被篡改）
        OrderAmountInfo amountInfo = calculateOrderAmount(createOrderDTO);

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
            // 7. 创建真正的订单（扣减库存）
            OrderMain order = createTakeoutOrderInternal(createOrderDTO, orderNo, amountInfo);

            // 8. 更新订单支付状态为已支付
            order.setPayStatus(PayStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            order.setPayType(payOrderDTO.getPayType());
            orderMainMapper.updateOrderMain(order);

            // 9. 删除预订单缓存
            redisCache.deleteObject(cacheKey);

            // 10. 记录支付日志
            UserBase user = userBaseMapper.selectUserBaseByUserBaseId(userId);
            saveStatusLog(order.getOrderMainId(), null, OrderStatusEnum.PENDING_ACCEPT.getCode(),
                    OperatorTypeEnum.USER, userId,
                    user.getNickname(), "用户支付并创建订单");

            log.info("用户支付并创建订单成功，订单号：{}，用户ID：{}，配送费：{}",
                    order.getOrderNo(), userId, amountInfo.getDeliveryFee());

            return order;

        } catch (Exception e) {
            // 如果订单创建失败，退款
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
     * 支付并创建订单（先扣款，再创建订单）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderMain payAndCreateErrandOrder(Long userId, PayOrderDTO payOrderDTO,Long userAddressId) {
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
            // 7. 创建真正的订单（扣减库存）
            OrderMain order = createErrandOrderInternal(createOrderDTO, orderNo, amountInfo,userAddressId);

            // 8. 更新订单支付状态为已支付
            order.setPayStatus(PayStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            order.setPayType(payOrderDTO.getPayType());
            orderMainMapper.updateOrderMain(order);

            // 9. 删除预订单缓存
            redisCache.deleteObject(cacheKey);

            // 10. 记录支付日志
            UserBase user = userBaseMapper.selectUserBaseByUserBaseId(userId);
            saveStatusLog(order.getOrderMainId(), null, OrderStatusEnum.PENDING_ACCEPT.getCode(),
                    OperatorTypeEnum.USER, userId,
                    user.getNickname(), "用户支付并创建订单");

            log.info("用户支付并创建订单成功，订单号：{}，用户ID：{}，配送费：{}",
                    order.getOrderNo(), userId, amountInfo.getDeliveryFee());

            return order;

        } catch (Exception e) {
            // 如果订单创建失败，退款
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

        log.info("创建预支付订单成功，预订单号：{}，用户ID：{}，配送费：{}",
                preOrderNo, createErrandOrderDto.getUserId(), amountInfo.getDeliveryFee());

        return prePayOrder;
    }

    /**
     * 取消预支付订单
     */
    @Override
    public boolean cancelPrePayOrder(Long userId, String preOrderNo) {
        // 1. 从 Redis 获取预订单信息
        String cacheKey = getPreOrderCacheKey(preOrderNo);
        CreateOrderDTO createOrderDTO = redisCache.getCacheObject(cacheKey);
        if (createOrderDTO == null) {
            throw new ServiceException("预订单不存在或已过期");
        }

        // 2. 校验用户ID是否匹配
        if (!createOrderDTO.getUserId().equals(userId)) {
            throw new ServiceException("无权操作此订单");
        }

        // 3. 删除预订单缓存
        redisCache.deleteObject(cacheKey);

        log.info("取消预支付订单成功，预订单号：{}，用户ID：{}", preOrderNo, userId);

        return true;
    }

    /**
     * 取消预支付订单
     */
    @Override
    public boolean cancelPrePayErrandOrder(Long userId, String preOrderNo) {
        // 1. 从 Redis 获取预订单信息
        String cacheKey = getPreOrderCacheKey(preOrderNo);
        CreateErrandOrderDto createOrderDTO = redisCache.getCacheObject(cacheKey);
        if (createOrderDTO == null) {
            throw new ServiceException("预订单不存在或已过期");
        }

        // 2. 校验用户ID是否匹配
        if (!createOrderDTO.getUserId().equals(userId)) {
            throw new ServiceException("无权操作此订单");
        }

        // 3. 删除预订单缓存
        redisCache.deleteObject(cacheKey);

        log.info("取消预支付订单成功，预订单号：{}，用户ID：{}", preOrderNo, userId);

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
        if (!OrderStatusEnum.PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
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

            // 7. 记录状态变更日志
            UserBase user = userBaseMapper.selectUserBaseByUserBaseId(userId);
            saveStatusLog(orderMainId, OrderStatusEnum.PENDING_ACCEPT.getCode(),
                    OrderStatusEnum.CANCELED.getCode(),
                    OperatorTypeEnum.USER, userId,
                    user.getNickname(), "用户取消订单：" + cancelReason);

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

            // 6. 记录状态变更日志
            UserBase user = userBaseMapper.selectUserBaseByUserBaseId(userId);
            saveStatusLog(orderMainId, OrderStatusEnum.DELIVERING.getCode(),
                    OrderStatusEnum.COMPLETED.getCode(),
                    OperatorTypeEnum.USER, userId,
                    user.getNickname(), "用户确认收货");

            log.info("用户确认收货成功，订单ID：{}，用户ID：{}", orderMainId, userId);
        }

        return result;
    }

    /**
     * 用户确认收货
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

        // 3. 校验订单状态（必须是配送中状态）
//        if (!OrderStatusEnum.DELIVERING.getCode().equals(order.getOrderStatus())) {
//            throw new ServiceException("订单状态不正确");
//        }

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

            // 6. 记录状态变更日志
            UserBase user = userBaseMapper.selectUserBaseByUserBaseId(userId);
            saveStatusLog(orderMainId, OrderStatusEnum.DELIVERING.getCode(),
                    OrderStatusEnum.COMPLETED.getCode(),
                    OperatorTypeEnum.USER, userId,
                    user.getNickname(), "用户确认收货");

            log.info("用户确认收货成功，订单ID：{}，用户ID：{}", orderMainId, userId);
        }

        return result;
    }

    /**
     * 内部方法：真正创建订单
     */
    private OrderMain createTakeoutOrderInternal(CreateOrderDTO createOrderDTO, String orderNo, OrderAmountInfo amountInfo) {
        // 1. 查询商家地址（作为取货地址）
        MerchantAddress merchantAddress = merchantAddressInfoMapper
                .selectMerchantAddressByMerchantBaseId(createOrderDTO.getMerchantId());
        if (merchantAddress == null) {
            throw new ServiceException("商家地址不存在");
        }

        // 2. 创建订单主表记录
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
        orderMain.setPlatformHoldAmount(amountInfo.getPayAmount()); // 初始平台暂存=实付金额
        orderMain.setGoodsAmount(amountInfo.getGoodsAmount());
        orderMain.setDeliveryFeeAmount(amountInfo.getDeliveryFee());

        // 支付状态（已支付）
        orderMain.setPayStatus(PayStatusEnum.PAID.getCode());
        orderMain.setPayTime(new Date());
        orderMain.setPayType(1L); // 默认余额支付

        // 订单状态（待接单）
        orderMain.setOrderStatus(OrderStatusEnum.PENDING_ACCEPT.getCode());

        // 取货地址（商家地址）
        orderMain.setPickAddressId(merchantAddress.getMerchantAddressId());
        orderMain.setPickAddress(merchantAddress.getProvince() + merchantAddress.getCity()
                + merchantAddress.getDistrict() + merchantAddress.getDetailAddress());
        orderMain.setPickContact(merchantAddress.getContactPerson());
        orderMain.setPickPhone(merchantAddress.getContactPhone());

        // 送货地址（用户地址）
        orderMain.setDeliverAddressId(createOrderDTO.getDeliverAddressId());
        orderMain.setDeliverAddress(createOrderDTO.getDeliverAddress());
        orderMain.setDeliverContact(createOrderDTO.getDeliverContact());
        orderMain.setDeliverPhone(createOrderDTO.getDeliverPhone());
        orderMain.setDeliverLongitude(createOrderDTO.getDeliverLongitude());
        orderMain.setDeliverLatitude(createOrderDTO.getDeliverLatitude());

        orderMain.setRemark(createOrderDTO.getRemark());
        orderMain.setCreateTime(new Date());
        orderMain.setUpdateTime(new Date());

        // 插入订单主表
        int mainResult = orderMainMapper.insertOrderMain(orderMain);
        if (mainResult == 0) {
            throw new ServiceException("创建订单失败");
        }

        // 3. 创建订单明细并扣减库存
        for (OrderItemDTO item : createOrderDTO.getItems()) {
            // 查询商品信息
            MerchantGoods goods = merchantGoodsMapper.selectMerchantGoodsByMerchantGoodsId(item.getGoodsId());
            if (goods == null) {
                throw new ServiceException("商品不存在：" + item.getGoodsId());
            }

            // 以数据库价格为准
            item.setGoodsPrice(goods.getPrice());

            // 扣减库存（支付成功后才扣减）
            int stockResult = merchantGoodsMapper.decreaseStock(item.getGoodsId(), item.getQuantity());
            if (stockResult == 0) {
                throw new ServiceException("扣减库存失败：" + goods.getGoodsName());
            }

            // 创建订单明细
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
            detail.setSettleStatus(0L); // 未结算

            orderTakeoutDetailMapper.insertOrderTakeoutDetail(detail);
        }

        // 4. 创建配送记录
        OrderDelivery delivery = new OrderDelivery();
        delivery.setOrderDeliveryId(generateLongId());
        delivery.setOrderMainId(orderMain.getOrderMainId());
        delivery.setDeliveryFee(amountInfo.getDeliveryFee());
        delivery.setDeliveryFeeFromUser(amountInfo.getDeliveryFee());
        delivery.setRiderIncome(amountInfo.getDeliveryFee()); // 简化：配送费全部给骑手
        delivery.setIncomeStatus(0L); // 未发放
        delivery.setAssignTime(new Date());
        delivery.setDeliveryStatus(0L); // 待分配

        orderDeliveryMapper.insertOrderDelivery(delivery);

        // 5. 记录订单创建日志
        saveStatusLog(orderMain.getOrderMainId(), null, OrderStatusEnum.PENDING_ACCEPT.getCode(),
                OperatorTypeEnum.USER, createOrderDTO.getUserId(),
                createOrderDTO.getUserNickname(), "用户支付并创建订单");

        return orderMain;
    }

    /**
     * 内部方法：真正创建订单
     */
    private OrderMain createErrandOrderInternal(CreateErrandOrderDto createOrderDTO, String orderNo,
                                                OrderAmountInfo amountInfo, Long userAddressId) {

        // 2. 创建订单主表记录
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
        orderMain.setPlatformHoldAmount(amountInfo.getPayAmount()); // 初始平台暂存=实付金额
        orderMain.setGoodsAmount(amountInfo.getGoodsAmount());
        orderMain.setDeliveryFeeAmount(amountInfo.getDeliveryFee());

        // 支付状态（已支付）
        orderMain.setPayStatus(PayStatusEnum.PAID.getCode());
        orderMain.setPayTime(new Date());
        orderMain.setPayType(1L); // 默认余额支付

        // 订单状态（待接单）
        orderMain.setOrderStatus(2L);

        // 取货地址处理（支持帮我买订单：userAddressId 为 null）
        if (userAddressId != null) {
            orderMain.setPickAddressId(userAddressId);
            UserAddress userAddress = userAddressMapper.selectUserAddressByUserAddressId(userAddressId);
            if (userAddress != null) {
                orderMain.setPickAddress(userAddress.getProvince() + userAddress.getCity()
                        + userAddress.getDistrict() + userAddress.getDetailAddress());
                orderMain.setPickContact(userAddress.getReceiver());
                orderMain.setPickPhone(userAddress.getPhone());
            }
        } else {
            // 帮我买订单：设置默认取件地址信息
            orderMain.setPickAddressId(null);
            orderMain.setPickAddress("帮我买（无固定取件地址）");
            orderMain.setPickContact("用户指定");
            orderMain.setPickPhone(createOrderDTO.getDeliverPhone()); // 使用收货电话
        }

        // 送货地址（用户地址）- 确保不为空
        if (createOrderDTO.getDeliverAddressId() == null || createOrderDTO.getDeliverAddress() == null) {
            throw new ServiceException("收货地址不能为空");
        }
        orderMain.setDeliverAddressId(createOrderDTO.getDeliverAddressId());
        orderMain.setDeliverAddress(createOrderDTO.getDeliverAddress());
        orderMain.setDeliverContact(createOrderDTO.getDeliverContact());
        orderMain.setDeliverPhone(createOrderDTO.getDeliverPhone());
        orderMain.setDeliverLongitude(createOrderDTO.getDeliverLongitude());
        orderMain.setDeliverLatitude(createOrderDTO.getDeliverLatitude());
        orderMain.setOrderStatus(OrderStatusEnum.PENDING_ACCEPT.getCode());
        orderMain.setRemark(createOrderDTO.getRemark());
        orderMain.setCreateTime(new Date());
        orderMain.setUpdateTime(new Date());

        // 插入订单主表
        int mainResult = orderMainMapper.insertOrderMain(orderMain);
        if (mainResult == 0) {
            throw new ServiceException("创建订单失败");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30); // 加上30分钟

        // 创建跑腿订单明细（修复帮我买订单判断逻辑）
        OrderErrandDetail orderErrandDetail = new OrderErrandDetail();
        orderErrandDetail.setOrderMainId(orderMain.getOrderMainId());
        orderErrandDetail.setOrderErrandDetailId(com.ruoyi.platform.chat.utils.SnowflakeIdGenerator.getInstance().nextId());

        // 正确判断订单类型：取件地址为空 → 帮我买（2），否则 → 配送（1）
        orderErrandDetail.setErrandType(userAddressId == null ? 2L : 1L);
        orderErrandDetail.setGoodsDesc(createOrderDTO.getGoodsDesc());
        orderErrandDetail.setExpectedTime(calendar.getTime());
        orderErrandDetail.setAdvanceAmount(orderMain.getGoodsAmount());
        orderErrandDetail.setTipAmount(orderMain.getDeliveryFeeAmount());

        orderErrandDetailMapper.insertOrderErrandDetail(orderErrandDetail);

        // 4. 创建配送记录
        OrderDelivery delivery = new OrderDelivery();
        delivery.setOrderDeliveryId(generateLongId());
        delivery.setOrderMainId(orderMain.getOrderMainId());
        delivery.setDeliveryFee(amountInfo.getDeliveryFee());
        delivery.setDeliveryFeeFromUser(amountInfo.getDeliveryFee());
        delivery.setRiderIncome(amountInfo.getDeliveryFee()); // 简化：配送费全部给骑手
        delivery.setIncomeStatus(0L); // 未发放
        delivery.setAssignTime(new Date());
        delivery.setDeliveryStatus(0L); // 待分配

        orderDeliveryMapper.insertOrderDelivery(delivery);

        // 5. 记录订单创建日志
        saveStatusLog(orderMain.getOrderMainId(), null, OrderStatusEnum.PENDING_ACCEPT.getCode(),
                OperatorTypeEnum.USER, createOrderDTO.getUserId(),
                createOrderDTO.getUserNickname(), "用户支付并创建订单");

        return orderMain;
    }



    /**
     * 恢复商品库存（取消订单时调用）
     */
    private void restoreGoodsStock(Long orderMainId) {
        // 查询订单明细
        OrderTakeoutDetail queryDetail = new OrderTakeoutDetail();
        queryDetail.setOrderMainId(orderMainId);
        List<OrderTakeoutDetail> details = orderTakeoutDetailMapper.selectOrderTakeoutDetailList(queryDetail);

        // 恢复每个商品的库存
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

        if (createOrderDTO.getDeliverAddressId() == null) {
            throw new ServiceException("送货地址不能为空");
        }

    }

    /**
     * 计算订单金额
     */
    private OrderAmountInfo calculateOrderAmount(CreateOrderDTO createOrderDTO) {
        OrderAmountInfo info = new OrderAmountInfo();

        // 1. 计算商品总金额
        BigDecimal goodsAmount = BigDecimal.ZERO;
        for (OrderItemDTO item : createOrderDTO.getItems()) {
            BigDecimal itemAmount = item.getGoodsPrice().multiply(new BigDecimal(item.getQuantity()));
            goodsAmount = goodsAmount.add(itemAmount);
        }

        // 2. 计算配送费
        BigDecimal deliveryFee = calculateDeliveryFee(createOrderDTO);

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
     * @param createOrderDTO
     * @return
     */
    private OrderAmountInfo calculateErrandOrderAmount(CreateErrandOrderDto createOrderDTO) {
        OrderAmountInfo info = new OrderAmountInfo();

        // 1. 商品金额：null 时兜底为 0（避免 NPE，同时符合金额计算逻辑）
        BigDecimal goodsAmount = Optional.ofNullable(createOrderDTO.getGoodsPrice())
                .orElse(BigDecimal.ZERO);
        // 校验商品金额合法性（非负，避免负数金额）
        if (goodsAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("商品金额不能为负数");
        }

        // 2. 配送费：null 时兜底为 0，同时校验合法性
        BigDecimal deliveryFee = Optional.ofNullable(createOrderDTO.getDeliverAmount())
                .orElse(BigDecimal.ZERO);
        if (deliveryFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("配送费不能为负数");
        }

        // 3. 优惠金额（暂时为0，后续扩展时同样需判空）
        BigDecimal discountAmount = BigDecimal.ZERO;

        // 4. 总金额 = 商品金额 + 配送费（此时已确保两个值非 null）
        BigDecimal totalAmount = goodsAmount.add(deliveryFee);

        // 5. 实付金额 = 总金额 - 优惠金额（优惠金额不能超过总金额）
        BigDecimal payAmount = totalAmount.subtract(discountAmount);
        // 兜底：实付金额不能为负数
        if (payAmount.compareTo(BigDecimal.ZERO) < 0) {
            payAmount = BigDecimal.ZERO;
        }

        // 赋值返回
        info.setGoodsAmount(goodsAmount);
        info.setDeliveryFee(deliveryFee);
        info.setDiscountAmount(discountAmount);
        info.setTotalAmount(totalAmount);
        info.setPayAmount(payAmount);

        return info;
    }

    /**
     * 计算配送费
     *
     * 策略1（已注释）：根据距离动态计算
     * 策略2（当前使用）：固定5元
     */
    private BigDecimal calculateDeliveryFee(CreateOrderDTO createOrderDTO) {
        // ==================== 策略1：根据距离动态计算（已注释） ====================
        /*
        try {
            // 1. 查询商家信息获取商家坐标
            MerchantBase merchant = merchantInfoMapper.selectMerchantBaseByMerchantBaseId(
                    createOrderDTO.getMerchantId());

            if (merchant == null) {
                log.warn("商家不存在，使用默认配送费，商家ID：{}", createOrderDTO.getMerchantId());
                return new BigDecimal("5.00");
            }

            // 2. 检查商家和用户坐标是否存在
            BigDecimal merchantLongitude = merchant.getLongitude();
            BigDecimal merchantLatitude = merchant.getLatitude();
            BigDecimal userLongitude = createOrderDTO.getDeliverLongitude();
            BigDecimal userLatitude = createOrderDTO.getDeliverLatitude();

            if (merchantLongitude == null || merchantLatitude == null ||
                userLongitude == null || userLatitude == null) {
                log.warn("坐标信息不完整，使用默认配送费");
                return new BigDecimal("5.00");
            }

            // 3. 计算距离（单位：公里）
            BigDecimal distance = calculateDistance(
                    merchantLongitude.doubleValue(),
                    merchantLatitude.doubleValue(),
                    userLongitude.doubleValue(),
                    userLatitude.doubleValue()
            );

            log.info("计算配送距离，商家ID：{}，距离：{}公里", createOrderDTO.getMerchantId(), distance);

            // 4. 根据距离计算配送费
            BigDecimal deliveryFee;

            if (distance.compareTo(new BigDecimal("2")) <= 0) {
                // 2公里以内：5元
                deliveryFee = new BigDecimal("5.00");
            } else if (distance.compareTo(new BigDecimal("3")) <= 0) {
                // 2-3公里：6元
                deliveryFee = new BigDecimal("6.00");
            } else if (distance.compareTo(new BigDecimal("5")) <= 0) {
                // 3-5公里：8元
                deliveryFee = new BigDecimal("8.00");
            } else if (distance.compareTo(new BigDecimal("8")) <= 0) {
                // 5-8公里：12元
                deliveryFee = new BigDecimal("12.00");
            } else if (distance.compareTo(new BigDecimal("10")) <= 0) {
                // 8-10公里：15元
                deliveryFee = new BigDecimal("15.00");
            } else {
                // 超过10公里：15元 + 超出部分每公里2元
                BigDecimal extraDistance = distance.subtract(new BigDecimal("10"));
                BigDecimal extraFee = extraDistance.multiply(new BigDecimal("2"));
                deliveryFee = new BigDecimal("15.00").add(extraFee);
            }

            // 5. 检查是否超过商家配送范围
            BigDecimal merchantDeliveryRange = merchant.getDeliveryRange();
            if (merchantDeliveryRange != null && distance.compareTo(merchantDeliveryRange) > 0) {
                throw new ServiceException("超出商家配送范围，最大配送距离：" + merchantDeliveryRange + "公里");
            }

            // 6. 四舍五入到小数点后2位
            deliveryFee = deliveryFee.setScale(2, RoundingMode.HALF_UP);

            log.info("计算配送费完成，距离：{}公里，配送费：{}元", distance, deliveryFee);

            return deliveryFee;

        } catch (ServiceException e) {
            throw e; // 超出配送范围的异常需要抛出
        } catch (Exception e) {
            log.error("计算配送费失败，使用默认配送费", e);
            return new BigDecimal("5.00");
        }
        */

        // ==================== 策略2：固定配送费（当前使用） ====================
        return new BigDecimal("5.00");
    }

    /**
     * 计算两点之间的距离（单位：公里）
     * 使用 Haversine 公式计算球面距离
     *
     * @param lon1 起点经度
     * @param lat1 起点纬度
     * @param lon2 终点经度
     * @param lat2 终点纬度
     * @return 距离（公里）
     */
    private BigDecimal calculateDistance(double lon1, double lat1, double lon2, double lat2) {
        // 将经纬度转换为弧度
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        // 计算差值
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Haversine 公式
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算距离
        double distance = EARTH_RADIUS * c;

        // 转换为 BigDecimal 并保留2位小数
        return new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 生成预订单编号
     */
    private String generatePreOrderNo() {
        // 预订单号格式：PRE + yyyyMMddHHmmss + 6位随机数
        String timestamp = DateUtils.dateTimeNow("yyyyMMddHHmmss");
        String random = String.format("%06d", (int)(Math.random() * 1000000));
        return "PRE" + timestamp + random;
    }

    /**
     * 生成正式订单编号
     */
    private String generateOrderNo() {
        // 订单号格式：TO + yyyyMMddHHmmss + 6位随机数
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
     * 保存订单状态变更日志
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
     * 生成Long类型的唯一ID
     */
    private Long generateLongId() {
        String uuid = IdUtils.fastSimpleUUID();
        String hexString = uuid.substring(0, 15);
        try {
            return Long.parseLong(hexString, 16);
        } catch (NumberFormatException e) {
            return System.currentTimeMillis() * 1000 + (long)(Math.random() * 1000);
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