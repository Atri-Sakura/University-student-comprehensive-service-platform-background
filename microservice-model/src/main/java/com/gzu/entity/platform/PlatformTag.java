package com.gzu.entity.platform;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlatformTag {
    private Long platformTagId;
    private String tagCode;
    private String tagName;
    private String tagType;
    private String tagDesc;
    private String parentCode;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}