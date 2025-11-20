package com.ruoyi.platform.domain.enums;

import lombok.Getter;

/**
 * 操作人类型枚举
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Getter
public enum OperatorTypeEnum {

    USER(1L, "用户"),
    RIDER(2L, "骑手"),
    MERCHANT(3L, "商家"),
    SYSTEM(4L, "系统");

    private final Long code;
    private final String desc;

    OperatorTypeEnum(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}