package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 商家钱包流水对象 merchant_wallet_flow
 *
 * 记录每次收入、提现、退款等变动
 *
 * @date 2025-10-24
 */
public class MerchantWalletFlow extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 流水ID */
    @Excel(name = "流水ID")
    private Long flowId;

    /** 商家ID */
    @Excel(name = "商家ID")
    private Long merchantBaseId;

    /** 关联订单ID */
    @Excel(name = "订单ID")
    private Long orderMainId;

    /** 关联提现ID */
    @Excel(name = "提现ID")
    private Long withdrawId;

    /** 流水类型（INCOME/WITHDRAW/REFUND） */
    @Excel(name = "流水类型")
    private String flowType;

    /** 金额（正数收入，负数支出） */
    @Excel(name = "金额")
    private BigDecimal flowAmount;

    /** 商品金额（仅订单收入时有值） */
    @Excel(name = "商品金额")
    private BigDecimal goodsAmount;

    /** 变动后的余额 */
    @Excel(name = "变动后余额")
    private BigDecimal balanceAfter;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // ===== Getter / Setter =====
    public Long getFlowId() { return flowId; }
    public void setFlowId(Long flowId) { this.flowId = flowId; }

    public Long getMerchantBaseId() { return merchantBaseId; }
    public void setMerchantBaseId(Long merchantBaseId) { this.merchantBaseId = merchantBaseId; }

    public Long getOrderMainId() { return orderMainId; }
    public void setOrderMainId(Long orderMainId) { this.orderMainId = orderMainId; }

    public Long getWithdrawId() { return withdrawId; }
    public void setWithdrawId(Long withdrawId) { this.withdrawId = withdrawId; }

    public String getFlowType() { return flowType; }
    public void setFlowType(String flowType) { this.flowType = flowType; }

    public BigDecimal getFlowAmount() { return flowAmount; }
    public void setFlowAmount(BigDecimal flowAmount) { this.flowAmount = flowAmount; }

    public BigDecimal getGoodsAmount() { return goodsAmount; }
    public void setGoodsAmount(BigDecimal goodsAmount) { this.goodsAmount = goodsAmount; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("flowId", getFlowId())
                .append("merchantBaseId", getMerchantBaseId())
                .append("orderMainId", getOrderMainId())
                .append("withdrawId", getWithdrawId())
                .append("flowType", getFlowType())
                .append("flowAmount", getFlowAmount())
                .append("goodsAmount", getGoodsAmount())
                .append("balanceAfter", getBalanceAfter())
                .append("description", getDescription())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .toString();
    }
}