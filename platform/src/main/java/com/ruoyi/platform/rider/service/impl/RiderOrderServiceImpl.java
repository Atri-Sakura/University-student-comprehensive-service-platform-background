package com.ruoyi.platform.rider.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.rider.mapper.RiderOrderMapper;
import com.ruoyi.platform.rider.service.IRiderOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 骑手订单服务实现类
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Service
public class RiderOrderServiceImpl implements IRiderOrderService {

    @Autowired
    private RiderOrderMapper riderOrderMapper;

    /**
     * 查询可接单的订单列表（待取货状态且未被接单）
     *
     * @param orderMain 查询条件
     * @return 订单列表
     */
    @Override
    public List<OrderMain> selectAvailableOrderList(OrderMain orderMain) {
        return riderOrderMapper.selectAvailableOrderList(orderMain);
    }

    /**
     * 查询骑手自己的订单列表
     *
     * @param riderId 骑手ID
     * @param orderMain 查询条件
     * @return 订单列表
     */
    @Override
    public List<OrderMain> selectRiderOrderList(Long riderId, OrderMain orderMain) {
        orderMain.getParams().put("riderId", riderId);
        return riderOrderMapper.selectRiderOrderList(orderMain);
    }

    /**
     * 查询骑手订单详情
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 订单详情
     */
    @Override
    public OrderMain selectRiderOrderById(Long riderId, Long orderMainId) {
        OrderMain order = riderOrderMapper.selectRiderOrderById(riderId, orderMainId);
        if (order == null) {
            throw new ServiceException("订单不存在或无权查看");
        }
        return order;
    }
}