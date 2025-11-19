// com/ruoyi/platform/domain/RiderWalletRecord.java
package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 骑手钱包流水对象 rider_wallet_record
 *
 * @author ruoyi
 * @date 2025-10-20
 */
public class RiderWalletRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 流水唯一ID */
    private Long riderWalletRecordId;

    /** 所属钱包ID */
    @Excel(name = "所属钱包ID")
    private Long riderWalletId;

    /** 所属骑手ID */
    @Excel(name = "所属骑手ID")
    private Long riderBaseId;

    /** 金额(正数=收入，负数=支出) */
    @Excel(name = "金额(正数=收入，负数=支出)")
    private BigDecimal amount;

    /** 配送费（仅配送收入时有值） */
    @Excel(name = "配送费")
    private BigDecimal deliveryFee;

    /** 交易类型：1-配送收入 2-提现 3-违规扣款 4-平台补贴 */
    @Excel(name = "交易类型：1-配送收入 2-提现 3-违规扣款 4-平台补贴")
    private Long tradeType;

    /** 关联业务ID */
    @Excel(name = "关联业务ID")
    private Long relatedId;

    /** 交易状态：0-处理中 1-成功 2-失败 */
    @Excel(name = "交易状态：0-处理中 1-成功 2-失败")
    private Long tradeStatus;

    /** 交易时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交易时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date tradeTime;

    public void setRiderWalletRecordId(Long riderWalletRecordId) {
        this.riderWalletRecordId = riderWalletRecordId;
    }

    public Long getRiderWalletRecordId() {
        return riderWalletRecordId;
    }

    public void setRiderWalletId(Long riderWalletId) {
        this.riderWalletId = riderWalletId;
    }

    public Long getRiderWalletId() {
        return riderWalletId;
    }

    public void setRiderBaseId(Long riderBaseId) {
        this.riderBaseId = riderBaseId;
    }

    public Long getRiderBaseId() {
        return riderBaseId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setTradeType(Long tradeType) {
        this.tradeType = tradeType;
    }

    public Long getTradeType() {
        return tradeType;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setTradeStatus(Long tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Long getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("riderWalletRecordId", getRiderWalletRecordId())
                .append("riderWalletId", getRiderWalletId())
                .append("riderBaseId", getRiderBaseId())
                .append("amount", getAmount())
                .append("deliveryFee", getDeliveryFee())
                .append("tradeType", getTradeType())
                .append("relatedId", getRelatedId())
                .append("tradeStatus", getTradeStatus())
                .append("tradeTime", getTradeTime())
                .append("remark", getRemark())
                .toString();
    }
}