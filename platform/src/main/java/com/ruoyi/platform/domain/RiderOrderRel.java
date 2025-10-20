package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 骑手接单关联对象 rider_order_rel
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class RiderOrderRel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联记录唯一ID */
    private Long riderOrderRelId;

    /** 骑手ID */
    @Excel(name = "骑手ID")
    private Long riderBaseId;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderId;

    /** 订单类型：1-外卖单 2-跑腿单 */
    @Excel(name = "订单类型：1-外卖单 2-跑腿单")
    private Long orderType;

    /** 接单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveTime;

    /** 取货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "取货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pickUpTime;

    /** 送达时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "送达时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliverTime;

    /** 配送状态：1-待取货 2-配送中 3-已送达 4-异常取消 */
    @Excel(name = "配送状态：1-待取货 2-配送中 3-已送达 4-异常取消")
    private Long deliveryStatus;

    /** 异常原因 */
    @Excel(name = "异常原因")
    private String abnormalReason;

    public void setRiderOrderRelId(Long riderOrderRelId) 
    {
        this.riderOrderRelId = riderOrderRelId;
    }

    public Long getRiderOrderRelId() 
    {
        return riderOrderRelId;
    }

    public void setRiderBaseId(Long riderBaseId) 
    {
        this.riderBaseId = riderBaseId;
    }

    public Long getRiderBaseId() 
    {
        return riderBaseId;
    }

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setOrderType(Long orderType) 
    {
        this.orderType = orderType;
    }

    public Long getOrderType() 
    {
        return orderType;
    }

    public void setReceiveTime(Date receiveTime) 
    {
        this.receiveTime = receiveTime;
    }

    public Date getReceiveTime() 
    {
        return receiveTime;
    }

    public void setPickUpTime(Date pickUpTime) 
    {
        this.pickUpTime = pickUpTime;
    }

    public Date getPickUpTime() 
    {
        return pickUpTime;
    }

    public void setDeliverTime(Date deliverTime) 
    {
        this.deliverTime = deliverTime;
    }

    public Date getDeliverTime() 
    {
        return deliverTime;
    }

    public void setDeliveryStatus(Long deliveryStatus) 
    {
        this.deliveryStatus = deliveryStatus;
    }

    public Long getDeliveryStatus() 
    {
        return deliveryStatus;
    }

    public void setAbnormalReason(String abnormalReason) 
    {
        this.abnormalReason = abnormalReason;
    }

    public String getAbnormalReason() 
    {
        return abnormalReason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("riderOrderRelId", getRiderOrderRelId())
            .append("riderBaseId", getRiderBaseId())
            .append("orderId", getOrderId())
            .append("orderType", getOrderType())
            .append("receiveTime", getReceiveTime())
            .append("pickUpTime", getPickUpTime())
            .append("deliverTime", getDeliverTime())
            .append("deliveryStatus", getDeliveryStatus())
            .append("abnormalReason", getAbnormalReason())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
