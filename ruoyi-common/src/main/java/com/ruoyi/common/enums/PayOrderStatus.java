package com.ruoyi.common.enums;

public enum PayOrderStatus {

    PENDING(0, "待支付"),
    PROCESSING(1, "支付中"),
    SUCCESS(2, "支付成功"),
    FAILED(3, "支付失败"),
    CLOSED(4, "已关闭");

    private final int code;
    private final String desc;

    PayOrderStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PayOrderStatus fromCode(Integer code) {
        if (code == null) return null;
        for (PayOrderStatus s : values()) {
            if (s.code == code) {
                return s;
            }
        }
        return null;
    }
}
