package com.ruoyi.common.enums;

public enum PayOwnerType {

    USER(1, "用户"),
    RIDER(2, "骑手"),
    MERCHANT(3, "商家");

    private final int code;
    private final String desc;

    PayOwnerType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PayOwnerType fromCode(Integer code) {
        if (code == null) return null;
        for (PayOwnerType t : values()) {
            if (t.code == code) {
                return t;
            }
        }
        return null;
    }
}
