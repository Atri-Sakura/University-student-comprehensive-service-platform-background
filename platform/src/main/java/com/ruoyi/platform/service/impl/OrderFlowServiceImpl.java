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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单流转服务实现类
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Service
public class OrderFlowServiceImpl implements IOrderFlowService {

    private static final Logger log = LoggerFactory.getLogger(OrderFlowServiceImpl.class);

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

    /**
     * 商家接单
     *
     * @param merchantId 商家ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int merchantAcceptOrder(Long merchantId, Long orderMainId) {
        // 1. 查询订单信息
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 权限校验：订单必须属于该商家
        OrderTakeoutDetail queryDetail = new OrderTakeoutDetail();
        queryDetail.setOrderMainId(orderMainId);
        List<OrderTakeoutDetail> details = orderTakeoutDetailMapper.selectOrderTakeoutDetailList(queryDetail);
        if (details.isEmpty() || !details.get(0).getMerchantId().equals(merchantId)) {
            throw new ServiceException("无权操作此订单");
        }

        // 3. 状态校验：必须是待接单状态
        if (!OrderStatusEnum.PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
            throw new ServiceException("订单状态不正确，当前状态：" + order.getOrderStatus());
        }

        // 4. 支付状态校验：必须已支付
        if (!PayStatusEnum.PAID.getCode().equals(order.getPayStatus())) {
            throw new ServiceException("订单未支付");
        }

        // 5. 更新订单状态为待取货
        OrderMain updateOrder = new OrderMain();
        updateOrder.setOrderMainId(orderMainId);
        updateOrder.setOrderStatus(OrderStatusEnum.PENDING_PICKUP.getCode());
        updateOrder.setUpdateTime(DateUtils.getNowDate());
        int result = orderMainMapper.updateOrderMain(updateOrder);

        if (result == 0) {
            throw new ServiceException("订单状态更新失败");
        }

        // 6. 结算商品金额给商家
        walletFlowService.settleMerchant(merchantId, orderMainId, order.getGoodsAmount());

        // 7. 更新订单明细结算状态
        for (OrderTakeoutDetail detail : details) {
            OrderTakeoutDetail updateDetail = new OrderTakeoutDetail();
            updateDetail.setOrderTakeoutDetailId(detail.getOrderTakeoutDetailId());
            updateDetail.setSettleStatus(1L);
            orderTakeoutDetailMapper.updateOrderTakeoutDetail(updateDetail);
        }

        // 8. 记录状态变更日志
        MerchantBase merchant = merchantBaseMapper.selectMerchantBaseByMerchantBaseId(merchantId);
        saveStatusLog(orderMainId, OrderStatusEnum.PENDING_ACCEPT.getCode(),
                OrderStatusEnum.PENDING_PICKUP.getCode(),
                OperatorTypeEnum.MERCHANT, merchantId,
                merchant.getMerchantName(), "商家接单");

        log.info("商家接单成功，订单号：{}，商家ID：{}", order.getOrderNo(), merchantId);
        return result;
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
        // 1. 查询订单信息
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 权限校验
        OrderTakeoutDetail queryDetail = new OrderTakeoutDetail();
        queryDetail.setOrderMainId(orderMainId);
        List<OrderTakeoutDetail> details = orderTakeoutDetailMapper.selectOrderTakeoutDetailList(queryDetail);
        if (details.isEmpty() || !details.get(0).getMerchantId().equals(merchantId)) {
            throw new ServiceException("无权操作此订单");
        }

        // 3. 状态校验：必须是待接单状态
        if (!OrderStatusEnum.PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
            throw new ServiceException("订单状态不正确，无法拒单");
        }

        // 4. 更新订单状态为已拒单
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

        // 5. 退款给用户
        walletFlowService.refundUser(order.getUserId(), orderMainId, order.getPayAmount());

        // 6. 更新支付状态为已退款
        OrderMain updatePayStatus = new OrderMain();
        updatePayStatus.setOrderMainId(orderMainId);
        updatePayStatus.setPayStatus(PayStatusEnum.REFUNDED.getCode());
        updatePayStatus.setUpdateTime(DateUtils.getNowDate());
        orderMainMapper.updateOrderMain(updatePayStatus);

        // 7. 恢复商品库存
        for (OrderTakeoutDetail detail : details) {
            // TODO: 调用商品服务恢复库存
            // merchantGoodsMapper.increaseStock(detail.getGoodsId(), detail.getQuantity());
        }

        // 8. 记录状态变更日志
        MerchantBase merchant = merchantBaseMapper.selectMerchantBaseByMerchantBaseId(merchantId);
        saveStatusLog(orderMainId, OrderStatusEnum.PENDING_ACCEPT.getCode(),
                OrderStatusEnum.REJECTED.getCode(),
                OperatorTypeEnum.MERCHANT, merchantId,
                merchant.getMerchantName(), "商家拒单：" + refuseReason);

        log.info("商家拒单成功，订单号：{}，拒单原因：{}", order.getOrderNo(), refuseReason);
        return result;
    }

    /**
     * 骑手接单
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int riderAcceptOrder(Long riderId, Long orderMainId) {
        // 1. 查询订单信息
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 状态校验：必须是待取货状态
        if (!OrderStatusEnum.PENDING_PICKUP.getCode().equals(order.getOrderStatus())) {
            throw new ServiceException("订单状态不正确，无法接单");
        }

        if(order.getOrderType().equals(2L)){
            order.setOrderStatus(OrderStatusEnum.PENDING_PICKUP.getCode());
        }
        // 3. 查询配送记录
        OrderDelivery queryDelivery = new OrderDelivery();
        queryDelivery.setOrderMainId(orderMainId);
        List<OrderDelivery> deliveries = orderDeliveryMapper.selectOrderDeliveryList(queryDelivery);
        if (deliveries.isEmpty()) {
            throw new ServiceException("配送记录不存在");
        }

        OrderDelivery delivery = deliveries.get(0);

        // 4. 校验是否已被其他骑手接单
        if (delivery.getRiderId() != null && !delivery.getRiderId().equals(riderId)) {
            throw new ServiceException("订单已被其他骑手接单");
        }

        // 【关键修复】5. 校验并获取骑手收入（配送费）
        BigDecimal riderIncome = delivery.getRiderIncome();
        if (riderIncome == null || riderIncome.compareTo(BigDecimal.ZERO) <= 0) {
            // 如果配送记录中没有配送费，从订单主表获取
            riderIncome = order.getDeliveryFeeAmount();

            if (riderIncome == null || riderIncome.compareTo(BigDecimal.ZERO) <= 0) {
                log.error("配送费异常，订单ID：{}，配送记录ID：{}", orderMainId, delivery.getOrderDeliveryId());
                throw new ServiceException("配送费信息异常，无法接单");
            }

            // 更新配送记录中的配送费信息
            OrderDelivery updateFee = new OrderDelivery();
            updateFee.setOrderDeliveryId(delivery.getOrderDeliveryId());
            updateFee.setDeliveryFee(riderIncome);
            updateFee.setDeliveryFeeFromUser(riderIncome);
            updateFee.setRiderIncome(riderIncome);
            orderDeliveryMapper.updateOrderDelivery(updateFee);

            log.info("自动修复配送费，订单ID：{}，配送费：{}", orderMainId, riderIncome);
        }

        // 6. 更新配送记录
        RiderBase rider = riderBaseMapper.selectRiderBaseByRiderBaseId(riderId);
        OrderDelivery updateDelivery = new OrderDelivery();
        updateDelivery.setOrderDeliveryId(delivery.getOrderDeliveryId());
        updateDelivery.setRiderId(riderId);
        updateDelivery.setRiderNickname(rider.getNickname());
        updateDelivery.setReceiveTime(DateUtils.getNowDate());
        updateDelivery.setDeliveryStatus(1L); // 已接单
        int result = orderDeliveryMapper.updateOrderDelivery(updateDelivery);

        if (result == 0) {
            throw new ServiceException("配送记录更新失败");
        }

        // 7. 结算配送费给骑手（使用校验后的配送费）
        walletFlowService.settleRider(riderId, orderMainId, riderIncome);

        // 8. 更新配送费结算状态
        OrderDelivery updateIncomeStatus = new OrderDelivery();
        updateIncomeStatus.setOrderDeliveryId(delivery.getOrderDeliveryId());
        updateIncomeStatus.setIncomeStatus(1L);
        orderDeliveryMapper.updateOrderDelivery(updateIncomeStatus);

        // 9. 记录状态变更日志
        saveStatusLog(orderMainId,
                OrderStatusEnum.PENDING_PICKUP.getCode(),  // 旧状态：待取货
                OrderStatusEnum.PENDING_PICKUP.getCode(),  // 新状态：待取货
                OperatorTypeEnum.RIDER, riderId,
                rider.getNickname(), "骑手接单（配送状态：待分配 → 已接单）");

        log.info("骑手接单成功，订单号：{}，骑手ID：{}，配送费：{}", order.getOrderNo(), riderId, riderIncome);
        return result;
    }

    /**
     * 骑手取货
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int riderPickupOrder(Long riderId, Long orderMainId) {
        // 1. 查询订单信息
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 状态校验
        if (!OrderStatusEnum.PENDING_PICKUP.getCode().equals(order.getOrderStatus())&&order.getOrderType().equals(1)) {
            throw new ServiceException("订单状态不正确，无法取货");
        }

        // 3. 查询配送记录并校验权限
        OrderDelivery queryDelivery = new OrderDelivery();
        queryDelivery.setOrderMainId(orderMainId);
        List<OrderDelivery> deliveries = orderDeliveryMapper.selectOrderDeliveryList(queryDelivery);
        if (deliveries.isEmpty()) {
            throw new ServiceException("配送记录不存在");
        }

        OrderDelivery delivery = deliveries.get(0);
        if (!riderId.equals(delivery.getRiderId())) {
            throw new ServiceException("无权操作此订单");
        }

        if (!Long.valueOf(1L).equals(delivery.getDeliveryStatus())) {
            throw new ServiceException("配送状态不正确");
        }

        // 4. 更新订单状态为配送中
        OrderMain updateOrder = new OrderMain();
        updateOrder.setOrderMainId(orderMainId);
        updateOrder.setOrderStatus(OrderStatusEnum.DELIVERING.getCode());
        updateOrder.setUpdateTime(DateUtils.getNowDate());
        int result = orderMainMapper.updateOrderMain(updateOrder);

        if (result == 0) {
            throw new ServiceException("订单状态更新失败");
        }

        // 5. 更新配送记录
        OrderDelivery updateDelivery = new OrderDelivery();
        updateDelivery.setOrderDeliveryId(delivery.getOrderDeliveryId());
        updateDelivery.setPickTime(DateUtils.getNowDate());
        updateDelivery.setDeliveryStatus(2L); // 已取货
        orderDeliveryMapper.updateOrderDelivery(updateDelivery);

        // 6. 记录状态变更日志
        RiderBase rider = riderBaseMapper.selectRiderBaseByRiderBaseId(riderId);
        saveStatusLog(orderMainId, OrderStatusEnum.PENDING_PICKUP.getCode(),
                OrderStatusEnum.DELIVERING.getCode(),
                OperatorTypeEnum.RIDER, riderId,
                rider.getNickname(), "骑手取货");

        log.info("骑手取货成功，订单号：{}，骑手ID：{}", order.getOrderNo(), riderId);
        return result;
    }

    /**
     * 骑手送达
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int riderDeliverOrder(Long riderId, Long orderMainId) {
        // 1. 查询订单信息
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 状态校验
        if (!OrderStatusEnum.DELIVERING.getCode().equals(order.getOrderStatus())) {
            throw new ServiceException("订单状态不正确，无法完成配送");
        }

        // 3. 查询配送记录并校验权限
        OrderDelivery queryDelivery = new OrderDelivery();
        queryDelivery.setOrderMainId(orderMainId);
        List<OrderDelivery> deliveries = orderDeliveryMapper.selectOrderDeliveryList(queryDelivery);
        if (deliveries.isEmpty()) {
            throw new ServiceException("配送记录不存在");
        }

        OrderDelivery delivery = deliveries.get(0);
        if (!riderId.equals(delivery.getRiderId())) {
            throw new ServiceException("无权操作此订单");
        }

        if (!Long.valueOf(2L).equals(delivery.getDeliveryStatus())) {
            throw new ServiceException("配送状态不正确");
        }

        // 4. 更新订单状态为已完成
        OrderMain updateOrder = new OrderMain();
        updateOrder.setOrderMainId(orderMainId);
        updateOrder.setOrderStatus(OrderStatusEnum.COMPLETED.getCode());
        updateOrder.setCompleteTime(DateUtils.getNowDate());
        updateOrder.setUpdateTime(DateUtils.getNowDate());
        int result = orderMainMapper.updateOrderMain(updateOrder);

        if (result == 0) {
            throw new ServiceException("订单状态更新失败");
        }

        // 5. 更新配送记录
        OrderDelivery updateDelivery = new OrderDelivery();
        updateDelivery.setOrderDeliveryId(delivery.getOrderDeliveryId());
        updateDelivery.setDeliverTime(DateUtils.getNowDate());
        updateDelivery.setDeliveryStatus(3L); // 已送达
        orderDeliveryMapper.updateOrderDelivery(updateDelivery);

        // 6. 记录状态变更日志
        RiderBase rider = riderBaseMapper.selectRiderBaseByRiderBaseId(riderId);
        saveStatusLog(orderMainId, OrderStatusEnum.DELIVERING.getCode(),
                OrderStatusEnum.COMPLETED.getCode(),
                OperatorTypeEnum.RIDER, riderId,
                rider.getNickname(), "骑手送达");

        log.info("订单送达成功，订单号：{}，骑手ID：{}", order.getOrderNo(), riderId);
        return result;
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
}