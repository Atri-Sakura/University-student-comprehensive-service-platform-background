package com.ruoyi.platform.domain. enums;

/**
 * 订单状态枚举
 */
public enum OrderStatusEnum {

    /** 商家待接单 */
    MERCHANT_PENDING_ACCEPT(1L, "商家待接单"),

    /** 骑手待接单 */
    RIDER_PENDING_ACCEPT(2L, "骑手待接单"),

    /** 骑手待取货 */
    RIDER_PENDING_PICKUP(3L, "骑手待取货"),

    /** 配送中 */
    DELIVERING(4L, "配送中"),

    /** 已完成 */
    COMPLETED(5L, "已完成"),

    /** 已取消 */
    CANCELED(6L, "已取消"),

    /** 已拒单 */
    REJECTED(8L, "已拒单"),

    /** 骑手异常报备 */
    RIDER_ABNORMAL_REPORT(7L, "骑手异常报备");

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

    /**
     * 判断是否为有效状态值
     */
    public static boolean isValidStatus(Long code) {
        return getByCode(code) != null;
    }
}