package com.gzu.entity.platform;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlatformAdmin {
    private Long platformAdminId;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private Long platformRoleId;
    private Integer accountStatus;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}