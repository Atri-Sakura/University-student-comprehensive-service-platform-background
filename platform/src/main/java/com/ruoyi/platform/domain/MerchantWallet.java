package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商家钱包对象 merchant_wallet
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class MerchantWallet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 钱包唯一ID */
    private Long merchantWalletId;

    /** 所属商家ID */
    @Excel(name = "所属商家ID")
    private Long merchantBaseId;

    /** 可用余额 */
    @Excel(name = "可用余额")
    private BigDecimal balance;

    /** 冻结金额 */
    @Excel(name = "冻结金额")
    private BigDecimal freezeAmount;

    public void setMerchantWalletId(Long merchantWalletId) 
    {
        this.merchantWalletId = merchantWalletId;
    }

    public Long getMerchantWalletId() 
    {
        return merchantWalletId;
    }

    public void setMerchantBaseId(Long merchantBaseId) 
    {
        this.merchantBaseId = merchantBaseId;
    }

    public Long getMerchantBaseId() 
    {
        return merchantBaseId;
    }

    public void setBalance(BigDecimal balance) 
    {
        this.balance = balance;
    }

    public BigDecimal getBalance() 
    {
        return balance;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) 
    {
        this.freezeAmount = freezeAmount;
    }

    public BigDecimal getFreezeAmount() 
    {
        return freezeAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("merchantWalletId", getMerchantWalletId())
            .append("merchantBaseId", getMerchantBaseId())
            .append("balance", getBalance())
            .append("freezeAmount", getFreezeAmount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
