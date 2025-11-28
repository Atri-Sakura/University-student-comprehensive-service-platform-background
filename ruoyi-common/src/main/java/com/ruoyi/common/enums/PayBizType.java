package com.ruoyi.common.enums;

public enum PayBizType {

    // -------- 用户相关 --------
    USER_RECHARGE(1, "用户充值到平台"),
    USER_WITHDRAW(2, "用户从平台提现"),

    // -------- 骑手相关 --------
    RIDER_RECHARGE(3, "骑手充值"),
    RIDER_WITHDRAW(4, "骑手提现"),

    // -------- 商家相关 --------
    MERCHANT_RECHARGE(5, "商家充值"),
    MERCHANT_WITHDRAW(6, "商家提现");

    private final int code;
    private final String desc;

    PayBizType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PayBizType fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PayBizType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null; // 或者抛异常也行，看你习惯
    }
}
