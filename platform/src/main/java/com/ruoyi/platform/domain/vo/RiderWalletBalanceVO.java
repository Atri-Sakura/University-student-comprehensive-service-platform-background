package com.ruoyi.platform.domain.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 钱包余额展示VO
 * 只返回余额字段，符合接口精简响应规范
 */
@Data
public class RiderWalletBalanceVO {
    private BigDecimal balance;
    public RiderWalletBalanceVO(BigDecimal balance) {
        this.balance = balance;
    }
}
