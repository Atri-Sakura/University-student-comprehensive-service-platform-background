package com.ruoyi.platform.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家钱包信息 VO
 */
@Data
public class MerchantWalletVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 可用余额 */
    private BigDecimal availableBalance;

    /** 冻结金额 */
    private BigDecimal freezeAmount;

    /** 更新时间 */
    private Date updateTime;

    @Override
    public String toString()
    {
        return "MerchantWalletVO{" +
                "availableBalance=" + availableBalance +
                ", freezeAmount=" + freezeAmount +
                ", updateTime=" + updateTime +
                '}';
    }
}
