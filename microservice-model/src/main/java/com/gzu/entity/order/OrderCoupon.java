package com.gzu.entity.order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderCoupon {
    private Long orderCouponId;
    private Long orderMainId;
    private Long couponId;
    private String couponName;
    private BigDecimal discountAmount;
    private Date createTime;
}