package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户钱包对象 user_wallet
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class UserWallet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 钱包唯一ID */
    private Long userWalletId;

    /** 所属用户ID */
    @Excel(name = "所属用户ID")
    private Long userBaseId;

    /** 可用余额 */
    @Excel(name = "可用余额")
    private BigDecimal balance;

    /** 冻结金额 */
    @Excel(name = "冻结金额")
    private BigDecimal freezeAmount;

    public void setUserWalletId(Long userWalletId) 
    {
        this.userWalletId = userWalletId;
    }

    public Long getUserWalletId() 
    {
        return userWalletId;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
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
            .append("userWalletId", getUserWalletId())
            .append("userBaseId", getUserBaseId())
            .append("balance", getBalance())
            .append("freezeAmount", getFreezeAmount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
