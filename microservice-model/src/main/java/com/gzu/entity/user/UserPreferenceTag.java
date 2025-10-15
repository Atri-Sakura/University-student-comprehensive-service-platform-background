package com.gzu.entity.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserPreferenceTag {
    private Long userPreferenceTagId;
    private Long userBaseId;
    private String tagCode;
    private String tagName;
    private String tagType;
    private Integer score;
    private Integer source;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}