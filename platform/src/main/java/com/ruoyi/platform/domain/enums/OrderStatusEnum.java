package com.ruoyi.platform.domain.enums;

/**
 * 订单状态枚举
 */
public enum OrderStatusEnum {

    /** 待接单 */
    PENDING_ACCEPT(1L, "待接单"),

    /** 待取货 */
    PENDING_PICKUP(2L, "待取货"),

    /** 配送中 */
    DELIVERING(3L, "配送中"),

    /** 已完成 */
    COMPLETED(4L, "已完成"),

    /** 已取消 */
    CANCELED(5L, "已取消"),

    /** 已拒单 */
    REJECTED(6L, "已拒单");

    private final Long code;
    private final String description;

    OrderStatusEnum(Long code, String description) {
        this.code = code;
        this.description = description;
    }

    public Long getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举
     */
    public static OrderStatusEnum getByCode(Long code) {
        if (code == null) {
            return null;
        }
        for (OrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}