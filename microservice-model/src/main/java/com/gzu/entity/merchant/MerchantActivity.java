package com.gzu.entity.merchant;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MerchantActivity {
    private Long merchantActivityId;
    private Long merchantBaseId;
    private String activityName;
    private String activityType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String content;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}