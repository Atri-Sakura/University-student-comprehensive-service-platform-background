package com.gzu.entity.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserBehaviorLog {
    private Long userBehaviorLogId;
    private Long userBaseId;
    private Integer behaviorType;
    private Long targetId;
    private Integer targetType;
    private String targetName;
    private LocalDateTime behaviorTime;
    private String device;
    private String scene;
    private Integer duration;
    private String extra;
}