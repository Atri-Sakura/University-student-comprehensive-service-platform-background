package com.ruoyi.platform.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 提现申请参数
 *
 * @date 2025/10/24
 */
@Data
public class WithdrawApplyDTO {
    /* 提现账户ID */
    private Long accountId;
    /* 提现金额 */
    private BigDecimal amount;
    /* 幂等key */
    private String idempotentKey;
}
