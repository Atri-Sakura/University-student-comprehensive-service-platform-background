package com.ruoyi.platform.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.OrderDeliveryMapper;
import com.ruoyi.platform.domain.OrderDelivery;
import com.ruoyi.platform.service.IOrderDeliveryService;

/**
 * 订单配送（含实际配送定位）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class OrderDeliveryServiceImpl implements IOrderDeliveryService 
{
    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;

    /**
     * 查询订单配送（含实际配送定位）
     * 
     * @param orderDeliveryId 订单配送（含实际配送定位）主键
     * @return 订单配送（含实际配送定位）
     */
    @Override
    public OrderDelivery selectOrderDeliveryByOrderDeliveryId(Long orderDeliveryId)
    {
        return orderDeliveryMapper.selectOrderDeliveryByOrderDeliveryId(orderDeliveryId);
    }

    /**
     * 查询订单配送（含实际配送定位）列表
     * 
     * @param orderDelivery 订单配送（含实际配送定位）
     * @return 订单配送（含实际配送定位）
     */
    @Override
    public List<OrderDelivery> selectOrderDeliveryList(OrderDelivery orderDelivery)
    {
        return orderDeliveryMapper.selectOrderDeliveryList(orderDelivery);
    }

    /**
     * 新增订单配送（含实际配送定位）
     * 
     * @param orderDelivery 订单配送（含实际配送定位）
     * @return 结果
     */
    @Override
    public int insertOrderDelivery(OrderDelivery orderDelivery)
    {
        return orderDeliveryMapper.insertOrderDelivery(orderDelivery);
    }

    /**
     * 修改订单配送（含实际配送定位）
     * 
     * @param orderDelivery 订单配送（含实际配送定位）
     * @return 结果
     */
    @Override
    public int updateOrderDelivery(OrderDelivery orderDelivery)
    {
        return orderDeliveryMapper.updateOrderDelivery(orderDelivery);
    }

    /**
     * 批量删除订单配送（含实际配送定位）
     * 
     * @param orderDeliveryIds 需要删除的订单配送（含实际配送定位）主键
     * @return 结果
     */
    @Override
    public int deleteOrderDeliveryByOrderDeliveryIds(Long[] orderDeliveryIds)
    {
        return orderDeliveryMapper.deleteOrderDeliveryByOrderDeliveryIds(orderDeliveryIds);
    }

    /**
     * 删除订单配送（含实际配送定位）信息
     * 
     * @param orderDeliveryId 订单配送（含实际配送定位）主键
     * @return 结果
     */
    @Override
    public int deleteOrderDeliveryByOrderDeliveryId(Long orderDeliveryId)
    {
        return orderDeliveryMapper.deleteOrderDeliveryByOrderDeliveryId(orderDeliveryId);
    }
}
