package com.gzu.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 用户地址表实体类
 * 对应数据库表：user_address
 */
@Data
@TableName("user_address")
public class UserAddress {
    /**
     * 地址唯一ID
     */
    private Long userAddressId;

    /**
     * 所属用户ID（关联user_base.user_base_id）
     */
    private Long userBaseId;

    /**
     * 收货人姓名
     */
    private String receiver;

    /**
     * 收货人电话（AES加密）
     */
    private String phone;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址（如XX宿舍3栋201）
     */
    private String detailAddress;

    /**
     * 地址标签（如DORM-宿舍/CLASSROOM-教室）
     */
    private String addressTag;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 是否默认地址：0-否 1-是
     */
    private Integer isDefault;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
