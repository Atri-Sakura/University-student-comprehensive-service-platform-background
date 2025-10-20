package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单支付记录对象 order_pay_record
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class OrderPayRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 支付记录ID */
    private Long orderPayRecordId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderMainId;

    /** 支付单号 */
    @Excel(name = "支付单号")
    private String payNo;

    /** 支付金额 */
    @Excel(name = "支付金额")
    private BigDecimal payAmount;

    /** 支付方式：1-余额 2-微信 3-支付宝 */
    @Excel(name = "支付方式：1-余额 2-微信 3-支付宝")
    private Long payType;

    /** 支付状态：0-处理中 1-成功 2-失败 */
    @Excel(name = "支付状态：0-处理中 1-成功 2-失败")
    private Long payStatus;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 支付回调数据 */
    @Excel(name = "支付回调数据")
    private String callbackData;

    public void setOrderPayRecordId(Long orderPayRecordId) 
    {
        this.orderPayRecordId = orderPayRecordId;
    }

    public Long getOrderPayRecordId() 
    {
        return orderPayRecordId;
    }

    public void setOrderMainId(Long orderMainId) 
    {
        this.orderMainId = orderMainId;
    }

    public Long getOrderMainId() 
    {
        return orderMainId;
    }

    public void setPayNo(String payNo) 
    {
        this.payNo = payNo;
    }

    public String getPayNo() 
    {
        return payNo;
    }

    public void setPayAmount(BigDecimal payAmount) 
    {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmount() 
    {
        return payAmount;
    }

    public void setPayType(Long payType) 
    {
        this.payType = payType;
    }

    public Long getPayType() 
    {
        return payType;
    }

    public void setPayStatus(Long payStatus) 
    {
        this.payStatus = payStatus;
    }

    public Long getPayStatus() 
    {
        return payStatus;
    }

    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }

    public void setCallbackData(String callbackData) 
    {
        this.callbackData = callbackData;
    }

    public String getCallbackData() 
    {
        return callbackData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderPayRecordId", getOrderPayRecordId())
            .append("orderMainId", getOrderMainId())
            .append("payNo", getPayNo())
            .append("payAmount", getPayAmount())
            .append("payType", getPayType())
            .append("payStatus", getPayStatus())
            .append("payTime", getPayTime())
            .append("callbackData", getCallbackData())
            .toString();
    }
}
