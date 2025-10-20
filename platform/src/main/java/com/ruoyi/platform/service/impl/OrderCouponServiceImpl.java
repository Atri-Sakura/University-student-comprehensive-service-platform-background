package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.OrderCouponMapper;
import com.ruoyi.platform.domain.OrderCoupon;
import com.ruoyi.platform.service.IOrderCouponService;

/**
 * 订单优惠券Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class OrderCouponServiceImpl implements IOrderCouponService 
{
    @Autowired
    private OrderCouponMapper orderCouponMapper;

    /**
     * 查询订单优惠券
     * 
     * @param orderCouponId 订单优惠券主键
     * @return 订单优惠券
     */
    @Override
    public OrderCoupon selectOrderCouponByOrderCouponId(Long orderCouponId)
    {
        return orderCouponMapper.selectOrderCouponByOrderCouponId(orderCouponId);
    }

    /**
     * 查询订单优惠券列表
     * 
     * @param orderCoupon 订单优惠券
     * @return 订单优惠券
     */
    @Override
    public List<OrderCoupon> selectOrderCouponList(OrderCoupon orderCoupon)
    {
        return orderCouponMapper.selectOrderCouponList(orderCoupon);
    }

    /**
     * 新增订单优惠券
     * 
     * @param orderCoupon 订单优惠券
     * @return 结果
     */
    @Override
    public int insertOrderCoupon(OrderCoupon orderCoupon)
    {
        orderCoupon.setCreateTime(DateUtils.getNowDate());
        return orderCouponMapper.insertOrderCoupon(orderCoupon);
    }

    /**
     * 修改订单优惠券
     * 
     * @param orderCoupon 订单优惠券
     * @return 结果
     */
    @Override
    public int updateOrderCoupon(OrderCoupon orderCoupon)
    {
        return orderCouponMapper.updateOrderCoupon(orderCoupon);
    }

    /**
     * 批量删除订单优惠券
     * 
     * @param orderCouponIds 需要删除的订单优惠券主键
     * @return 结果
     */
    @Override
    public int deleteOrderCouponByOrderCouponIds(Long[] orderCouponIds)
    {
        return orderCouponMapper.deleteOrderCouponByOrderCouponIds(orderCouponIds);
    }

    /**
     * 删除订单优惠券信息
     * 
     * @param orderCouponId 订单优惠券主键
     * @return 结果
     */
    @Override
    public int deleteOrderCouponByOrderCouponId(Long orderCouponId)
    {
        return orderCouponMapper.deleteOrderCouponByOrderCouponId(orderCouponId);
    }
}
