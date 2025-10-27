package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 商家提现账户对象 merchant_withdraw_account
 * @date 2025-10-24
 */
public class MerchantWithdrawAccount extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 提现账户唯一ID */
    @Excel(name = "提现账户ID")
    private Long accountId;

    /** 所属商家ID */
    @Excel(name = "商家ID")
    private Long merchantBaseId;

    /** 账户类型（bank/alipay/wechat） */
    @Excel(name = "账户类型")
    private String accountType;

    /** 账户户名 */
    @Excel(name = "账户户名")
    private String accountName;

    /** 账号（银行卡号/支付宝号/微信号） */
    @Excel(name = "账号")
    private String accountNumber;

    /** 银行名称（若为银行卡类型时填写） */
    @Excel(name = "银行名称")
    private String bankName;

    /** 是否为默认账户：1是 0否 */
    @Excel(name = "默认账户")
    private Integer isDefault;

    /** 账户状态：1启用 0禁用 */
    @Excel(name = "账户状态")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    // ===== Getter / Setter =====

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public Long getMerchantBaseId() { return merchantBaseId; }
    public void setMerchantBaseId(Long merchantBaseId) { this.merchantBaseId = merchantBaseId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public Integer getIsDefault() { return isDefault; }
    public void setIsDefault(Integer isDefault) { this.isDefault = isDefault; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("accountId", getAccountId())
                .append("merchantBaseId", getMerchantBaseId())
                .append("accountType", getAccountType())
                .append("accountName", getAccountName())
                .append("accountNumber", getAccountNumber())
                .append("bankName", getBankName())
                .append("isDefault", getIsDefault())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
