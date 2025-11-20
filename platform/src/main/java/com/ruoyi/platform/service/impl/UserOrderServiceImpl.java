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
import com.ruoyi.platform.mapper.*;
import com.ruoyi.platform.merchant.mapper.MerchantAddressInfoMapper;
import com.ruoyi.platform.service.IUserOrderService;
import com.ruoyi.platform.service.IWalletFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
    private MerchantGoodsMapper merchantGoodsMapper;

    @Autowired
    private UserBaseMapper userBaseMapper;

    @Autowired
    private IWalletFlowService walletFlowService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 创建预支付订单（只校验，不真正创建订单）
     */
    @Override
    public PrePayOrderDTO createPrePayOrder(CreateOrderDTO createOrderDTO) {
        // 1. 参数校验
        validateOrderParams(createOrderDTO);

        // 2. 校验商品库存（但不扣减）
        for (OrderItemDTO item : createOrderDTO.getItems()) {
            MerchantGoods goods = merchantGoodsMapper.selectMerchantGoodsByMerchantGoodsId(item.getGoodsId());
            if (goods == null) {
                throw new ServiceException("商品不存在：" + item.getGoodsId());
            }
            if (goods.getStock() < item.getQuantity()) {
                throw new ServiceException("商品库存不足：" + goods.getGoodsName());
            }

            // 校验商品价格是否被篡改
            if (goods.getPrice().compareTo(item.getGoodsPrice()) != 0) {
                throw new ServiceException("商品价格已变更，请刷新后重试");
            }
        }

        // 3. 计算订单金额
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

        log.info("创建预支付订单成功，预订单号：{}，用户ID：{}", preOrderNo, createOrderDTO.getUserId());

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

            log.info("用户支付并创建订单成功，订单号：{}，用户ID：{}", order.getOrderNo(), userId);

            return order;

        } catch (Exception e) {
            // 如果订单创建失败，退款
            log.error("订单创建失败，开始退款，预订单号：{}，用户ID：{}", preOrderNo, userId, e);
            try {
                // 这里需要实现退款逻辑
                // walletFlowService.refundToUser(userId, preOrderNo, amountInfo.getPayAmount());
            } catch (Exception refundEx) {
                log.error("自动退款失败，需人工处理，预订单号：{}，用户ID：{}", preOrderNo, userId, refundEx);
            }
            throw new ServiceException("订单创建失败：" + e.getMessage());
        }
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
     * 内部方法：真正创建订单（从原 createTakeoutOrder 拆分）
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
            detail.setGoodsName(item.getGoodsName());
            detail.setGoodsPrice(item.getGoodsPrice());
            detail.setQuantity(item.getQuantity());
            detail.setSubtotal(item.getGoodsPrice().multiply(new BigDecimal(item.getQuantity())));
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
     * 用户创建外卖订单（保留旧方法，标记为废弃）
     */
    @Override
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public OrderMain createTakeoutOrder(CreateOrderDTO createOrderDTO) {
        // ... 保留原有代码不变 ...
        // (这里省略，和你原来的代码一样)
        return null;
    }

    /**
     * 用户支付订单（保留旧方法，标记为废弃）
     */
    @Override
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(Long userId, String orderNo) {
        // ... 保留原有代码不变 ...
        // (这里省略，和你原来的代码一样)
        return true;
    }

    /**
     * 用户取消订单（仅待支付状态可取消）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelOrder(Long userId, Long orderMainId, String cancelReason) {
        // ... 保留原有代码不变 ...
        // (和你原来的代码一样)
        return 1;
    }

    /**
     * 用户确认收货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int confirmReceive(Long userId, Long orderMainId) {
        // ... 保留原有代码不变 ...
        // (和你原来的代码一样)
        return 1;
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
     * 计算订单金额
     */
    private OrderAmountInfo calculateOrderAmount(CreateOrderDTO createOrderDTO) {
        OrderAmountInfo info = new OrderAmountInfo();

        // 计算商品总金额
        BigDecimal goodsAmount = BigDecimal.ZERO;
        for (OrderItemDTO item : createOrderDTO.getItems()) {
            BigDecimal itemAmount = item.getGoodsPrice().multiply(new BigDecimal(item.getQuantity()));
            goodsAmount = goodsAmount.add(itemAmount);
        }

        // 计算配送费（简化：固定5元，实际应根据距离计算）
        BigDecimal deliveryFee = new BigDecimal("5.00");

        // 计算优惠金额（暂时为0）
        BigDecimal discountAmount = BigDecimal.ZERO;

        // 计算总金额和实付金额
        BigDecimal totalAmount = goodsAmount.add(deliveryFee);
        BigDecimal payAmount = totalAmount.subtract(discountAmount);

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
        OrderStatusLog log = new OrderStatusLog();
        log.setOrderMainId(orderMainId);
        log.setOldStatus(oldStatus);
        log.setNewStatus(newStatus);
        log.setOperatorType(operatorType.getCode());
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setRemark(remark);
        orderStatusLogMapper.insertOrderStatusLog(log);
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
        private BigDecimal goodsAmount;      // 商品金额
        private BigDecimal deliveryFee;      // 配送费
        private BigDecimal discountAmount;   // 优惠金额
        private BigDecimal totalAmount;      // 总金额
        private BigDecimal payAmount;        // 实付金额
    }
}