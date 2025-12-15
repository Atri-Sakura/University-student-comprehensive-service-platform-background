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

    @Autowired
    private MerchantGoodsMapper merchantGoodsMapper;

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
        // 1. 查询订单信息
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 仅外卖订单允许商家接单
        if (order.getOrderType() == null || !Long.valueOf(1L).equals(order.getOrderType())) {
            throw new ServiceException("非外卖订单不支持商家接单");
        }

        // 2. 查询明细（如需校验商家归属，可打开校验）
        OrderTakeoutDetail queryDetail = new OrderTakeoutDetail();
        queryDetail.setOrderMainId(orderMainId);
        List<OrderTakeoutDetail> details = orderTakeoutDetailMapper.selectOrderTakeoutDetailList(queryDetail);
//        if (details.isEmpty() || !details.get(0).getMerchantId().equals(merchantId)) {
//            throw new ServiceException("无权操作此订单");
//        }

        // 3. 状态校验：必须是 1-商家待接单
        if (!OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
            throw new ServiceException("订单状态不正确，当前状态：" + order.getOrderStatus());
        }

        // 4. 支付状态校验：必须已支付
        if (!PayStatusEnum.PAID.getCode().equals(order.getPayStatus())) {
            throw new ServiceException("订单未支付");
        }

        // 5. 更新订单状态为 2-骑手待接单
        OrderMain updateOrder = new OrderMain();
        updateOrder.setOrderMainId(orderMainId);
        updateOrder.setOrderStatus(OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode());
        updateOrder.setUpdateTime(DateUtils.getNowDate());
        int result = orderMainMapper.updateOrderMain(updateOrder);

        if (result == 0) {
            throw new ServiceException("订单状态更新失败");
        }

        // 6. 更新订单明细结算状态
        for (OrderTakeoutDetail detail : details) {
            OrderTakeoutDetail updateDetail = new OrderTakeoutDetail();
            updateDetail.setOrderTakeoutDetailId(detail.getOrderTakeoutDetailId());
            updateDetail.setSettleStatus(1L);
            orderTakeoutDetailMapper.updateOrderTakeoutDetail(updateDetail);
        }

        // 7. 记录状态变更日志
        MerchantBase merchant = merchantBaseMapper.selectMerchantBaseByMerchantBaseId(merchantId);
        saveStatusLog(orderMainId, OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode(),
                OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode(),
                OperatorTypeEnum.MERCHANT, merchantId,
                merchant != null ? merchant.getMerchantName() : "商家", "商家接单");

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
            merchantGoodsMapper.increaseStock(detail.getGoodsId(), detail.getQuantity());
        }

        // 8. 记录状态变更日志
        MerchantBase merchant = merchantBaseMapper.selectMerchantBaseByMerchantBaseId(merchantId);
        saveStatusLog(orderMainId, OrderStatusEnum.MERCHANT_PENDING_ACCEPT.getCode(),
                OrderStatusEnum.REJECTED.getCode(),
                OperatorTypeEnum.MERCHANT, merchantId,
                merchant != null ? merchant.getMerchantName() : "商家", "商家拒单：" + refuseReason);

        log.info("商家拒单成功，订单号：{}，拒单原因：{}", order.getOrderNo(), refuseReason);
        return result;
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
        // 1. 查询订单信息
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 状态校验：必须是 2-骑手待接单（外卖商家接单后=2；跑腿下单后=2）
        if (!OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode().equals(order.getOrderStatus())) {
            throw new ServiceException("订单状态不正确，无法接单");
        }

        // 3. 查询配送记录（用户下单时已创建过 order_delivery）
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

        // 6. 更新配送记录：写入骑手信息 + 接单时间 + 配送状态=1(已接单)
        RiderBase rider = riderBaseMapper.selectRiderBaseByRiderBaseId(riderId);

        OrderDelivery updateDelivery = new OrderDelivery();
        updateDelivery.setOrderDeliveryId(delivery.getOrderDeliveryId());
        updateDelivery.setRiderId(riderId);
        updateDelivery.setRiderNickname(rider != null ? rider.getNickname() : null);
        updateDelivery.setReceiveTime(DateUtils.getNowDate());
        updateDelivery.setDeliveryStatus(1L); // 1-已接单（与表注释一致）
        int deliveryResult = orderDeliveryMapper.updateOrderDelivery(updateDelivery);

        if (deliveryResult == 0) {
            throw new ServiceException("配送记录更新失败");
        }

        // 7. 更新订单状态为 3-骑手待取货（外卖/跑腿统一：2->3）
        OrderMain updateOrder = new OrderMain();
        updateOrder.setOrderMainId(orderMainId);
        updateOrder.setOrderStatus(OrderStatusEnum.RIDER_PENDING_PICKUP.getCode());
        updateOrder.setUpdateTime(DateUtils.getNowDate());
        int orderResult = orderMainMapper.updateOrderMain(updateOrder);

        if (orderResult == 0) {
            throw new ServiceException("订单状态更新失败");
        }

        // 8. 记录状态变更日志（2->3）
        saveStatusLog(orderMainId,
                OrderStatusEnum.RIDER_PENDING_ACCEPT.getCode(),
                OrderStatusEnum.RIDER_PENDING_PICKUP.getCode(),
                OperatorTypeEnum.RIDER, riderId,
                rider != null ? rider.getNickname() : "骑手",
                "骑手接单");

        log.info("骑手接单成功，订单号：{}，骑手ID：{}，配送费：{}", order.getOrderNo(), riderId, riderIncome);
        return orderResult;
    }

    /**
     * 骑手取货
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int riderPickupOrder(Long riderId, Long orderMainId) {
        // 1. 查询订单信息
        OrderMain order = orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 状态校验：必须是"骑手待取货"状态
        if (!OrderStatusEnum.RIDER_PENDING_PICKUP.getCode().equals(order.getOrderStatus())) {
            throw new ServiceException("订单状态不正确，无法取货，当前状态：" + order.getOrderStatus());
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
        updateDelivery.setDeliveryStatus(2L); // 2-已取货（配送中）
        updateDelivery.setActualPickLongitude(order.getPickLongitude());
        updateDelivery.setActualPickLatitude(order.getPickLatitude());
        orderDeliveryMapper.updateOrderDelivery(updateDelivery);

        // 6. 记录状态变更日志
        RiderBase rider = riderBaseMapper.selectRiderBaseByRiderBaseId(riderId);
        saveStatusLog(orderMainId, OrderStatusEnum.RIDER_PENDING_PICKUP.getCode(),
                OrderStatusEnum.DELIVERING.getCode(),
                OperatorTypeEnum.RIDER, riderId,
                rider != null ? rider.getNickname() : "骑手", "骑手取货");

        log.info("骑手取货成功，订单号：{}，骑手ID：{}", order.getOrderNo(), riderId);
        return result;
    }

    /**
     * 骑手送达
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
            throw new ServiceException("订单状态不正确，无法完成配送，当前状态：" + order.getOrderStatus());
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
            throw new ServiceException("配送状态不正确，当前状态：" + delivery. getDeliveryStatus());
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
        updateDelivery.setDeliveryStatus(3L); // 3-已送达
        updateDelivery.setActualDeliverLongitude(order.getDeliverLongitude());
        updateDelivery.setActualDeliverLatitude(order. getDeliverLatitude());
        orderDeliveryMapper.updateOrderDelivery(updateDelivery);

        // 6. 结算给骑手
        try {
            BigDecimal riderIncome = order.getDeliveryFeeAmount();

            if (riderIncome == null || riderIncome.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("配送费为空或为0，订单ID：{}", orderMainId);
                riderIncome = BigDecimal.ZERO;
            }

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

        // 7. 结算给商家（仅外卖订单）
        if (order.getOrderType() == 1L && order.getMerchantId() != null) {
            try {
                walletFlowService.settleMerchant(order.getMerchantId(), orderMainId, order.getGoodsAmount());
                log. info("商家结算成功，订单ID：{}，商家ID：{}，金额：{}",
                        orderMainId, order.getMerchantId(), order.getGoodsAmount());
            } catch (Exception e) {
                log.error("商家结算失败，订单ID：{}，商家ID：{}", orderMainId, order. getMerchantId(), e);
                // 商家结算失败不影响骑手结算，只记录日志
            }
        }

        // 8. 记录状态变更日志
        RiderBase rider = riderBaseMapper.selectRiderBaseByRiderBaseId(riderId);
        saveStatusLog(orderMainId, OrderStatusEnum.DELIVERING.getCode(),
                OrderStatusEnum.COMPLETED.getCode(),
                OperatorTypeEnum.RIDER, riderId,
                rider != null ? rider.getNickname() : "骑手", "骑手送达");

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