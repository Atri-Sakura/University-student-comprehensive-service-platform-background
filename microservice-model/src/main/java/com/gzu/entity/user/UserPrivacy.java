package com.gzu.entity.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserPrivacy {
    private Long userPrivacyId;
    private Long userBaseId;
    private Integer isRecommend;
    private Integer isLocationPermit;
    private LocalDateTime updateTime;
}