package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户银行卡绑定对象 user_bank_card
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class UserBankCard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 所属用户ID */
    @Excel(name = "所属用户ID")
    private Long userBaseId;

    /** 银行名称 */
    @Excel(name = "银行名称")
    private String bankName;

    /** 卡类型：1-储蓄卡 2-信用卡 */
    @Excel(name = "卡类型：1-储蓄卡 2-信用卡")
    private Long bankCardType;

    /** 银行卡号（加密存储） */
    @Excel(name = "银行卡号", readConverterExp = "加=密存储")
    private String cardNumber;

    /** 卡号尾号（冗余） */
    @Excel(name = "卡号尾号", readConverterExp = "冗=余")
    private String cardTailNumber;

    /** 持卡人姓名 */
    @Excel(name = "持卡人姓名")
    private String holderName;

    /** 身份证号（加密存储） */
    @Excel(name = "身份证号", readConverterExp = "加=密存储")
    private String idNumber;

    /** 预留手机号（加密存储） */
    @Excel(name = "预留手机号", readConverterExp = "加=密存储")
    private String reservePhone;

    /** 绑定状态：1-正常 0-已解绑 */
    @Excel(name = "绑定状态：1-正常 0-已解绑")
    private Long bindStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
    }

    public void setBankName(String bankName) 
    {
        this.bankName = bankName;
    }

    public String getBankName() 
    {
        return bankName;
    }

    public void setBankCardType(Long bankCardType) 
    {
        this.bankCardType = bankCardType;
    }

    public Long getBankCardType() 
    {
        return bankCardType;
    }

    public void setCardNumber(String cardNumber) 
    {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() 
    {
        return cardNumber;
    }

    public void setCardTailNumber(String cardTailNumber) 
    {
        this.cardTailNumber = cardTailNumber;
    }

    public String getCardTailNumber() 
    {
        return cardTailNumber;
    }

    public void setHolderName(String holderName) 
    {
        this.holderName = holderName;
    }

    public String getHolderName() 
    {
        return holderName;
    }

    public void setIdNumber(String idNumber) 
    {
        this.idNumber = idNumber;
    }

    public String getIdNumber() 
    {
        return idNumber;
    }

    public void setReservePhone(String reservePhone) 
    {
        this.reservePhone = reservePhone;
    }

    public String getReservePhone() 
    {
        return reservePhone;
    }

    public void setBindStatus(Long bindStatus) 
    {
        this.bindStatus = bindStatus;
    }

    public Long getBindStatus() 
    {
        return bindStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userBaseId", getUserBaseId())
            .append("bankName", getBankName())
            .append("bankCardType", getBankCardType())
            .append("cardNumber", getCardNumber())
            .append("cardTailNumber", getCardTailNumber())
            .append("holderName", getHolderName())
            .append("idNumber", getIdNumber())
            .append("reservePhone", getReservePhone())
            .append("bindStatus", getBindStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
