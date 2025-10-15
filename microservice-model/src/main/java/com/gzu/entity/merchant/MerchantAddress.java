package com.gzu.entity.merchant;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MerchantAddress {
    private Long merchantAddressId;
    private Long merchantBaseId;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String contactPerson;
    private String contactPhone;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}