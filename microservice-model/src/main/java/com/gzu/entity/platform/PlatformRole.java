package com.gzu.entity.platform;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlatformRole {
    private Long platformRoleId;
    private String roleName;
    private String roleCode;
    private String roleDesc;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}