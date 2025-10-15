package com.gzu.entity.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserRecommendSetting {
    private Long userRecommendSettingId;
    private Long userBaseId;
    private Integer isRecommendEnabled;
    private Integer recommendFreq;
    private String shieldedTagCodes;
    private String preferredScene;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}