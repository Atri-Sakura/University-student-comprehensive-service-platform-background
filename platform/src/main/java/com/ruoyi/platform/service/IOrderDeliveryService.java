package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.OrderDelivery;

/**
 * 订单配送（含实际配送定位）Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IOrderDeliveryService 
{
    /**
     * 查询订单配送（含实际配送定位）
     * 
     * @param orderDeliveryId 订单配送（含实际配送定位）主键
     * @return 订单配送（含实际配送定位）
     */
    public OrderDelivery selectOrderDeliveryByOrderDeliveryId(Long orderDeliveryId);

    /**
     * 查询订单配送（含实际配送定位）列表
     * 
     * @param orderDelivery 订单配送（含实际配送定位）
     * @return 订单配送（含实际配送定位）集合
     */
    public List<OrderDelivery> selectOrderDeliveryList(OrderDelivery orderDelivery);

    /**
     * 新增订单配送（含实际配送定位）
     * 
     * @param orderDelivery 订单配送（含实际配送定位）
     * @return 结果
     */
    public int insertOrderDelivery(OrderDelivery orderDelivery);

    /**
     * 修改订单配送（含实际配送定位）
     * 
     * @param orderDelivery 订单配送（含实际配送定位）
     * @return 结果
     */
    public int updateOrderDelivery(OrderDelivery orderDelivery);

    /**
     * 批量删除订单配送（含实际配送定位）
     * 
     * @param orderDeliveryIds 需要删除的订单配送（含实际配送定位）主键集合
     * @return 结果
     */
    public int deleteOrderDeliveryByOrderDeliveryIds(Long[] orderDeliveryIds);

    /**
     * 删除订单配送（含实际配送定位）信息
     * 
     * @param orderDeliveryId 订单配送（含实际配送定位）主键
     * @return 结果
     */
    public int deleteOrderDeliveryByOrderDeliveryId(Long orderDeliveryId);

    /**
     * 根据订单ID查询订单配送信息
     * @param orderMainId
     * @return
     */
    public OrderDelivery selectOrderDeliveryByOrderMainId(Long orderMainId);
}
