package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 商家每日收入汇总对象 merchant_daily_summary
 *
 * @date 2025-10-24
 */
public class MerchantDailySummary extends BaseEntity{

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "ID")
    private Long id;

    /** 商家ID */
    @Excel(name = "商家ID")
    private Long merchantBaseId;

    /** 统计日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计日期", width = 20, dateFormat = "yyyy-MM-dd")
    private Date summaryDate;

    /** 当日收入 */
    @Excel(name = "当日收入")
    private BigDecimal incomeAmount;

    /** 当日退款 */
    @Excel(name = "当日退款")
    private BigDecimal refundAmount;

    /** 当日提现 */
    @Excel(name = "当日提现")
    private BigDecimal withdrawAmount;

    /** 净收入（收入 - 退款 - 提现） */
    @Excel(name = "净收入")
    private BigDecimal netIncome;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // ===== Getter / Setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMerchantBaseId() { return merchantBaseId; }
    public void setMerchantBaseId(Long merchantBaseId) { this.merchantBaseId = merchantBaseId; }

    public Date getSummaryDate() { return summaryDate; }
    public void setSummaryDate(Date summaryDate) { this.summaryDate = summaryDate; }

    public BigDecimal getIncomeAmount() { return incomeAmount; }
    public void setIncomeAmount(BigDecimal incomeAmount) { this.incomeAmount = incomeAmount; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public BigDecimal getWithdrawAmount() { return withdrawAmount; }
    public void setWithdrawAmount(BigDecimal withdrawAmount) { this.withdrawAmount = withdrawAmount; }

    public BigDecimal getNetIncome() { return netIncome; }
    public void setNetIncome(BigDecimal netIncome) { this.netIncome = netIncome; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("merchantBaseId", getMerchantBaseId())
                .append("summaryDate", getSummaryDate())
                .append("incomeAmount", getIncomeAmount())
                .append("refundAmount", getRefundAmount())
                .append("withdrawAmount", getWithdrawAmount())
                .append("netIncome", getNetIncome())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateBy", getUpdateBy())
                .append("remark", getRemark())
                .toString();
    }

}
