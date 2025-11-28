package com.ruoyi.common.enums;

public enum TradeStatus {

    PROCESSING(0, "处理中"),
    SUCCESS(1, "成功"),
    FAILED(2, "失败");

    private final long code;
    private final String desc;

    TradeStatus(long code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public long getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
