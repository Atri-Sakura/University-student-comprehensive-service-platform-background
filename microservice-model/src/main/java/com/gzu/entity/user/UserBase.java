package com.gzu.entity.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserBase {
    private Long userBaseId;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String studentId;
    private String college;
    private String major;
    private String grade;
    private Integer gender;
    private String phone;
    private Integer creditScore;
    private Integer accountStatus;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}