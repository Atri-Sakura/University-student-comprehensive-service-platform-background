package com.ruoyi.common.enums;

public enum UserWalletTradeType {
    RECHARGE(1, "充值"),
    WITHDRAW(2, "提现"),
    TAKEOUT_PAY(3, "外卖支付"),
    ERRAND_PAY(4, "跑腿支付"),
    REFUND(5, "退款");

    private final long code;
    private final String desc;

    UserWalletTradeType(long code, String desc) {
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
