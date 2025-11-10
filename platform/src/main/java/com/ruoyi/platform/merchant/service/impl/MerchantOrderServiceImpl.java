package com.ruoyi.platform.merchant.service.impl;

import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.merchant.mapper.MerchantOrderMapper;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.merchant.service.IMerchantOrderService;

/**
 * 商家订单服务实现
 *
 * @author ruoyi
 */
@Service
public class MerchantOrderServiceImpl implements IMerchantOrderService {

    @Autowired
    private MerchantOrderMapper merchantOrderMapper;

    @Override
    public List<OrderMain> selectMerchantOrderList(Long merchantId, OrderMain orderMain) {
        // 将商家ID放入查询参数中，用于Mapper.xml中的权限校验
        orderMain.getParams().put("merchantId", merchantId);
        return merchantOrderMapper.selectMerchantOrderList(orderMain);
    }

    @Override
    public OrderMain selectMerchantOrderById(Long merchantId, Long orderMainId) {
        OrderMain queryParam = new OrderMain();
        queryParam.setOrderMainId(orderMainId);
        queryParam.getParams().put("merchantId", merchantId);

        return merchantOrderMapper.selectMerchantOrderByOrderMainId(queryParam);
    }

    @Override
    public int acceptOrder(Long merchantId, Long orderMainId) {
        // 1. 查询订单详情，确认订单存在
        OrderMain order = merchantOrderMapper.selectMerchantOrderById(merchantId, orderMainId);

        // 2. 权限与状态校验
        if (order == null) {
            throw new ServiceException("订单不存在");
        }

        // 校验订单是否属于该商家 (双重验证，防止逻辑漏洞)
        // 这一步也可以通过查询 order_takeout_detail 表来确认
        // 但既然详情能查出来，说明权限已通过，这里主要是做状态校验

        // 校验订单状态是否为“待接单”
        if (order.getOrderStatus() != 1L) {
            throw new ServiceException("订单状态已更新，请勿重复操作");
        }

        // 3. 更新订单状态
        OrderMain updateOrder = new OrderMain();
        updateOrder.setOrderMainId(orderMainId);
        updateOrder.setOrderStatus(2L); // 2: 待取货

        return merchantOrderMapper.updateOrderMain(updateOrder);
    }

    @Override
    public int rejectOrder(Long merchantId, Long orderMainId, String cancelOperator) {
        // 查询订单详情
        OrderMain order = merchantOrderMapper.selectMerchantOrderById(merchantId, orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        if (order.getOrderStatus() != 1L) {
            throw new ServiceException("订单状态已更新，无法拒单");
        }
        // 构造更新对象
        OrderMain updateOrder = new OrderMain();
        updateOrder.setOrderMainId(orderMainId);
        updateOrder.setOrderStatus(5L);
        updateOrder.setCancelOperator(cancelOperator);

        // 同时更新支付状态为 已退款 (3)
        updateOrder.setPayStatus(3L);

        return merchantOrderMapper.updateOrderMain(updateOrder);
    }
}