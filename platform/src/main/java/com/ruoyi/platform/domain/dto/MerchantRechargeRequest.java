package com.ruoyi.platform.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantRechargeRequest {

    /** 充值金额（单位：元） */
    private BigDecimal amount;

    /** 支付渠道：1-支付宝 2-微信 3-银行卡 */
    private Integer payChannel;
}
