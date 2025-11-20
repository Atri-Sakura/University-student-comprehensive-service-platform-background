package com.ruoyi.platform.domain.enums;

import lombok.Getter;

/**
 * 钱包流水类型枚举
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Getter
public enum WalletFlowTypeEnum {

    USER_PAY("USER_PAY", "用户支付"),
    MERCHANT_SETTLE("MERCHANT_SETTLE", "商家结算"),
    RIDER_SETTLE("RIDER_SETTLE", "骑手结算"),
    REFUND("REFUND", "退款");

    private final String code;
    private final String desc;

    WalletFlowTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}