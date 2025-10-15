package com.gzu.entity.rider;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RiderBase {
    private Long riderBaseId;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String realName;
    private String idCard;
    private String idCardFront;
    private String idCardBack;
    private String phone;
    private Integer auditStatus;
    private Integer workStatus;
    private Integer creditScore;
    private Integer accountStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}