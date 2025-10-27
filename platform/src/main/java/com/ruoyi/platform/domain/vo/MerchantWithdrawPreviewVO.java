package com.ruoyi.platform.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提现预估结果 VO
 */
@Data
public class MerchantWithdrawPreviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 提现金额 */
    private BigDecimal amount;
    /** 手续费 */
    private BigDecimal fee;
    /** 实际到账金额 */
    private BigDecimal actualAmount;

    @Override
    public String toString()
    {
        return "MerchantWithdrawPreviewVO{" +
                "amount=" + amount +
                ", fee=" + fee +
                ", actualAmount=" + actualAmount +
                '}';
    }
}
