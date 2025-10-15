package com.gzu.entity.platform;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PlatformCoupon {
    private Long platformCouponId;
    private String couponNo;
    private String couponName;
    private Integer couponType;
    private BigDecimal faceValue;
    private BigDecimal minSpend;
    private BigDecimal discount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalCount;
    private Integer remainCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}