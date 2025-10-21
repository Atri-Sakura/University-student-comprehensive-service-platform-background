package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.merchant.mapper.OrderMapper;
import com.ruoyi.platform.merchant.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void acceptOrder(Long orderNoId) {
        OrderMain orderMain = orderMapper.selectOrderMainByNoId(orderNoId);
        if (orderMain == null){
            throw new RuntimeException("订单不存在");
        }
        if (orderMain.getOrderStatus() != 0){
            throw new RuntimeException("订单状态不正确,不能接单");
        }
        orderMapper.acceptOrder(orderNoId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptOrderBatch(Long[] orderNoIds) {
        List<String> errorMessages = new ArrayList<>();

        for (Long orderNoId : orderNoIds) {
            try {
                acceptOrder(orderNoId);
            } catch (Exception e) {
                errorMessages.add(e.getMessage());
            }
        }

        if (!errorMessages.isEmpty()) {
            throw new RuntimeException(String.join("; ", errorMessages));
        }
    }

}
