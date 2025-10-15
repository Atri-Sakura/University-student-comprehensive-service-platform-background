package com.gzu.entity.merchant;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MerchantBase {
    private Long merchantBaseId;
    private String username;
    private String password;
    private String merchantName;
    private String logo;
    private Long merchantAddressId;
    private String businessScope;
    private String businessHours;
    private BigDecimal deliveryRange;
    private BigDecimal minOrderAmount;
    private BigDecimal deliveryFee;
    private String licenseImg;
    private BigDecimal rating;
    private Integer monthSales;
    private Integer auditStatus;
    private Integer businessStatus;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}