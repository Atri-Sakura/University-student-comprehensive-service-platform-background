package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.OrderCoupon;

/**
 * 订单优惠券Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface OrderCouponMapper 
{
    /**
     * 查询订单优惠券
     * 
     * @param orderCouponId 订单优惠券主键
     * @return 订单优惠券
     */
    public OrderCoupon selectOrderCouponByOrderCouponId(Long orderCouponId);

    /**
     * 查询订单优惠券列表
     * 
     * @param orderCoupon 订单优惠券
     * @return 订单优惠券集合
     */
    public List<OrderCoupon> selectOrderCouponList(OrderCoupon orderCoupon);

    /**
     * 新增订单优惠券
     * 
     * @param orderCoupon 订单优惠券
     * @return 结果
     */
    public int insertOrderCoupon(OrderCoupon orderCoupon);

    /**
     * 修改订单优惠券
     * 
     * @param orderCoupon 订单优惠券
     * @return 结果
     */
    public int updateOrderCoupon(OrderCoupon orderCoupon);

    /**
     * 删除订单优惠券
     * 
     * @param orderCouponId 订单优惠券主键
     * @return 结果
     */
    public int deleteOrderCouponByOrderCouponId(Long orderCouponId);

    /**
     * 批量删除订单优惠券
     * 
     * @param orderCouponIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderCouponByOrderCouponIds(Long[] orderCouponIds);
}
