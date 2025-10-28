package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 商家提现记录对象 merchant_withdraw_record
 *
 * @date 2025-10-24
 */
public class MerchantWithdrawRecord extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /** 提现记录ID */
    @Excel(name = "提现记录ID")
    private Long withdrawId;

    /** 商家ID */
    @Excel(name = "商家ID")
    private Long merchantBaseId;

    /** 提现账户ID */
    @Excel(name = "提现账户ID")
    private Long accountId;

    /** 提现金额 */
    @Excel(name = "提现金额")
    private BigDecimal withdrawAmount;

    /** 手续费 */
    @Excel(name = "手续费")
    private BigDecimal feeAmount;

    /** 实际到账金额 */
    @Excel(name = "实际到账金额")
    private BigDecimal actualAmount;

    /** 状态：PENDING/PROCESSING/SUCCESS/FAILED */
    @Excel(name = "提现状态")
    private String withdrawStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date requestTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date processTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 操作人（后台管理员/系统） */
    @Excel(name = "操作人")
    private String operatorName;

    /** 幂等key */
    @Excel(name = "幂等key")
    private String idempotentKey;

    // ===== Getter / Setter =====
    public Long getWithdrawId() { return withdrawId; }
    public void setWithdrawId(Long withdrawId) { this.withdrawId = withdrawId; }

    public Long getMerchantBaseId() { return merchantBaseId; }
    public void setMerchantBaseId(Long merchantBaseId) { this.merchantBaseId = merchantBaseId; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public BigDecimal getWithdrawAmount() { return withdrawAmount; }
    public void setWithdrawAmount(BigDecimal withdrawAmount) { this.withdrawAmount = withdrawAmount; }

    public BigDecimal getFeeAmount() { return feeAmount; }
    public void setFeeAmount(BigDecimal feeAmount) { this.feeAmount = feeAmount; }

    public BigDecimal getActualAmount() { return actualAmount; }
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }

    public String getWithdrawStatus() { return withdrawStatus; }
    public void setWithdrawStatus(String withdrawStatus) { this.withdrawStatus = withdrawStatus; }

    public Date getRequestTime() { return requestTime; }
    public void setRequestTime(Date requestTime) { this.requestTime = requestTime; }

    public Date getProcessTime() { return processTime; }
    public void setProcessTime(Date processTime) { this.processTime = processTime; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }

    public String getIdempotentKey() { return idempotentKey; }
    public void setIdempotentKey(String idempotentKey) { this.idempotentKey = idempotentKey; }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("withdrawId", getWithdrawId())
                .append("merchantBaseId", getMerchantBaseId())
                .append("accountId", getAccountId())
                .append("withdrawAmount", getWithdrawAmount())
                .append("feeAmount", getFeeAmount())
                .append("actualAmount", getActualAmount())
                .append("withdrawStatus", getWithdrawStatus())
                .append("requestTime", getRequestTime())
                .append("processTime", getProcessTime())
                .append("remark", getRemark())
                .append("operatorName", getOperatorName())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("idempotentKey", getIdempotentKey())
                .toString();
    }
}
