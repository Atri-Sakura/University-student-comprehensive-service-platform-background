package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单优惠券对象 order_coupon
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class OrderCoupon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long orderCouponId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderMainId;

    /** 优惠券ID */
    @Excel(name = "优惠券ID")
    private Long couponId;

    /** 优惠券名称 */
    @Excel(name = "优惠券名称")
    private String couponName;

    /** 优惠金额 */
    @Excel(name = "优惠金额")
    private BigDecimal discountAmount;

    public void setOrderCouponId(Long orderCouponId) 
    {
        this.orderCouponId = orderCouponId;
    }

    public Long getOrderCouponId() 
    {
        return orderCouponId;
    }

    public void setOrderMainId(Long orderMainId) 
    {
        this.orderMainId = orderMainId;
    }

    public Long getOrderMainId() 
    {
        return orderMainId;
    }

    public void setCouponId(Long couponId) 
    {
        this.couponId = couponId;
    }

    public Long getCouponId() 
    {
        return couponId;
    }

    public void setCouponName(String couponName) 
    {
        this.couponName = couponName;
    }

    public String getCouponName() 
    {
        return couponName;
    }

    public void setDiscountAmount(BigDecimal discountAmount) 
    {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmount() 
    {
        return discountAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderCouponId", getOrderCouponId())
            .append("orderMainId", getOrderMainId())
            .append("couponId", getCouponId())
            .append("couponName", getCouponName())
            .append("discountAmount", getDiscountAmount())
            .append("createTime", getCreateTime())
            .toString();
    }
}
