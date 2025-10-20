package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户钱包流水对象 user_wallet_record
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class UserWalletRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流水唯一ID */
    private Long userWalletRecordId;

    /** 所属钱包ID */
    @Excel(name = "所属钱包ID")
    private Long userWalletId;

    /** 所属用户ID */
    @Excel(name = "所属用户ID")
    private Long userBaseId;

    /** 金额(正数=收入，负数=支出) */
    @Excel(name = "金额(正数=收入，负数=支出)")
    private BigDecimal amount;

    /** 交易类型：1-充值 2-提现 3-外卖支付 4-跑腿支付 5-退款 */
    @Excel(name = "交易类型：1-充值 2-提现 3-外卖支付 4-跑腿支付 5-退款")
    private Long tradeType;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long relatedId;

    /** 交易状态：0-处理中 1-成功 2-失败 */
    @Excel(name = "交易状态：0-处理中 1-成功 2-失败")
    private Long tradeStatus;

    /** 交易时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交易时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date tradeTime;

    public void setUserWalletRecordId(Long userWalletRecordId) 
    {
        this.userWalletRecordId = userWalletRecordId;
    }

    public Long getUserWalletRecordId() 
    {
        return userWalletRecordId;
    }

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

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setTradeType(Long tradeType) 
    {
        this.tradeType = tradeType;
    }

    public Long getTradeType() 
    {
        return tradeType;
    }

    public void setRelatedId(Long relatedId) 
    {
        this.relatedId = relatedId;
    }

    public Long getRelatedId() 
    {
        return relatedId;
    }

    public void setTradeStatus(Long tradeStatus) 
    {
        this.tradeStatus = tradeStatus;
    }

    public Long getTradeStatus() 
    {
        return tradeStatus;
    }

    public void setTradeTime(Date tradeTime) 
    {
        this.tradeTime = tradeTime;
    }

    public Date getTradeTime() 
    {
        return tradeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userWalletRecordId", getUserWalletRecordId())
            .append("userWalletId", getUserWalletId())
            .append("userBaseId", getUserBaseId())
            .append("amount", getAmount())
            .append("tradeType", getTradeType())
            .append("relatedId", getRelatedId())
            .append("tradeStatus", getTradeStatus())
            .append("tradeTime", getTradeTime())
            .append("remark", getRemark())
            .toString();
    }
}
