package com.ruoyi.platform.domain.vo;

import lombok.Data;

/*
 * 骑手基础信息视图
 */
@Data
public class RiderBaseInfoVO {
    /**骑手基础信息id*/
    private Long riderBaseId;
    /**昵称*/
    private String nickname;
    /**头像*/
    private String avatar;
    /** 真实姓名*/
    private String realName;
    /** 脱敏身份证号码 */
    private String idCard;
    /** 脱敏手机号码 */
    private String phone;
    /** 信用分*/
    private Integer creditScore;
    /** 账户状态*/
    private Integer accountStatus;
    /** 创建时间*/
    private String createTime;
}
