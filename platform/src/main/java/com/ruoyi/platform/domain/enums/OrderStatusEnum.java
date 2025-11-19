package com.ruoyi.platform.domain.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Getter
public enum OrderStatusEnum {

    PENDING_ACCEPT(1L, "待接单"),
    PENDING_PICKUP(2L, "待取货"),
    DELIVERING(3L, "配送中"),
    COMPLETED(4L, "已完成"),
    CANCELLED(5L, "已取消");

    private final Long code;
    private final String desc;

    OrderStatusEnum(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static OrderStatusEnum getByCode(Long code) {
        for (OrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}