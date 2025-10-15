package com.gzu.entity.platform;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlatformOperateLog {
    private Long platformOperateLogId;
    private Long adminId;
    private String adminName;
    private String operType;
    private String operModule;
    private String operContent;
    private String ipAddress;
    private LocalDateTime operTime;
    private String userAgent;
}