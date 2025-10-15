package com.gzu.entity.platform;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlatformRolePerm {
    private Long platformRolePermId;
    private Long platformRoleId;
    private Long platformPermissionId;
    private LocalDateTime createTime;
}