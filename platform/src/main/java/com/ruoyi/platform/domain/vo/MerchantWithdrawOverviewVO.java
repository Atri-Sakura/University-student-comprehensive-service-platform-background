package com.ruoyi.platform.domain.vo;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 提现页顶部信息 VO
 */
@Data
public class MerchantWithdrawOverviewVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 可用余额 */
    private BigDecimal availableBalance;
    /** 当日收入 */
    private BigDecimal todayIncome;
    /** 待结算金额 */
    private BigDecimal settlingAmount;
    /** 提现中金额 */
    private BigDecimal withdrawingAmount;
    /** 提现费率 */
    private BigDecimal feeRate;
    /** 最小提现金额 */
    private BigDecimal minWithdraw;
    /** 最大提现金额 */
    private BigDecimal maxWithdraw;

    @Override
    public String toString()
    {
        return "MerchantWithdrawOverviewVO{" +
                "availableBalance=" + availableBalance +
                ", todayIncome=" + todayIncome +
                ", settlingAmount=" + settlingAmount +
                ", withdrawingAmount=" + withdrawingAmount +
                ", feeRate=" + feeRate +
                ", minWithdraw=" + minWithdraw +
                ", maxWithdraw=" + maxWithdraw +
                '}';
    }
}
