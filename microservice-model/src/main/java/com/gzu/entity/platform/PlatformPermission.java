package com.gzu.entity.platform;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlatformPermission {
    private Long platformPermissionId;
    private String permName;
    private String permKey;
    private Integer permType;
    private Long parentPermId;
    private String menuPath;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}