package com.ruoyi.common.enums;


public enum PayChannel {
    ALIPAY(1, "支付宝"),
    WECHAT(2, "微信"),
    BANK_CARD(3, "银行卡");

    private final int code;
    private final String desc;

    PayChannel(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PayChannel fromCode(Integer code) {
        if (code == null) return null;
        for (PayChannel c : values()) {
            if (c.code == code) {
                return c;
            }
        }
        return null;
    }
}
