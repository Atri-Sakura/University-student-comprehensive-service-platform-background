package com.ruoyi.platform.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserRechargeRequest {

    /** 充值金额 */
    private BigDecimal amount;

    /** 支付渠道：现在只能填 1（支付宝），未来可扩展 */
    private Integer payChannel;

    /** 幂等键：前端生成的 requestId，防止重复点击 */
//    private String requestId;
}
