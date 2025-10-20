package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 骑手钱包对象 rider_wallet
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class RiderWallet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 钱包唯一ID */
    private Long riderWalletId;

    /** 所属骑手ID */
    @Excel(name = "所属骑手ID")
    private Long riderBaseId;

    /** 可用余额 */
    @Excel(name = "可用余额")
    private BigDecimal balance;

    /** 冻结金额 */
    @Excel(name = "冻结金额")
    private BigDecimal freezeAmount;

    public void setRiderWalletId(Long riderWalletId) 
    {
        this.riderWalletId = riderWalletId;
    }

    public Long getRiderWalletId() 
    {
        return riderWalletId;
    }

    public void setRiderBaseId(Long riderBaseId) 
    {
        this.riderBaseId = riderBaseId;
    }

    public Long getRiderBaseId() 
    {
        return riderBaseId;
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
            .append("riderWalletId", getRiderWalletId())
            .append("riderBaseId", getRiderBaseId())
            .append("balance", getBalance())
            .append("freezeAmount", getFreezeAmount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
