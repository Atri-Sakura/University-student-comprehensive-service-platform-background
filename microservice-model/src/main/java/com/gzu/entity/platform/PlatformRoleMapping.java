package com.gzu.entity.platform;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlatformRoleMapping {
    private Long platformRoleMappingId;
    private String username;
    private Integer roleType;
    private String targetDb;
    private String targetTable;
    private Integer accountStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}