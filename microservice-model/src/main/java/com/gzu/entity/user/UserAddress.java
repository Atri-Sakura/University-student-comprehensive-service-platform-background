package com.gzu.entity.user;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserAddress {
    private Long userAddressId;
    private Long userBaseId;
    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String addressTag;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Integer isDefault;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}