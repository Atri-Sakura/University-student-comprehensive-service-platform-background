package com.gzu.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_base")
public class UserBase {

    /**
     * 用户唯一ID（雪花算法）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long userBaseId;

    /**
     * 登录账号（唯一）
     */
    private String username;

    /**
     * 密码（BCrypt加密）
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 学号（唯一）
     */
    private String studentId;

    /**
     * 所属学院（如计算机学院）
     */
    private String college;

    /**
     * 所属专业（如软件工程）
     */
    private String major;

    /**
     * 年级（如2022级）
     */
    private String grade;

    /**
     * 性别：1-男 2-女 0-未知
     */
    private Integer gender;

    /**
     * 联系电话（AES加密）
     */
    private String phone;

    /**
     * 信用分（影响推荐优先级）
     */
    private Integer creditScore;

    /**
     * 账号状态：0-禁用 1-正常
     */
    private Integer accountStatus;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}