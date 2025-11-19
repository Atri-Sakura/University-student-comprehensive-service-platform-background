package com.ruoyi.platform.domain.enums;

import lombok.Getter;

/**
 * 支付状态枚举
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Getter
public enum PayStatusEnum {

    UNPAID(0L, "未支付"),
    PAID(1L, "已支付"),
    REFUNDING(2L, "退款中"),
    REFUNDED(3L, "已退款");

    private final Long code;
    private final String desc;

    PayStatusEnum(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static PayStatusEnum getByCode(Long code) {
        for (PayStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}