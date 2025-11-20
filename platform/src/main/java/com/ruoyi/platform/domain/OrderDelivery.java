package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单配送（含实际配送定位）对象 order_delivery
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class OrderDelivery extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配送记录ID */
    private Long orderDeliveryId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderMainId;

    /** 骑手ID */
    @Excel(name = "骑手ID")
    private Long riderId;

    /** 骑手昵称(冗余) */
    @Excel(name = "骑手昵称(冗余)")
    private String riderNickname;

    /** 配送费（可基于主表取货-送货坐标计算） */
    @Excel(name = "配送费", readConverterExp = "可=基于主表取货-送货坐标计算")
    private BigDecimal deliveryFee;

    /** 实际取货经度 */
    @Excel(name = "实际取货经度")
    private BigDecimal actualPickLongitude;

    /** 实际取货纬度 */
    @Excel(name = "实际取货纬度")
    private BigDecimal actualPickLatitude;

    /** 实际送达经度 */
    @Excel(name = "实际送达经度")
    private BigDecimal actualDeliverLongitude;

    /** 实际送达纬度 */
    @Excel(name = "实际送达纬度")
    private BigDecimal actualDeliverLatitude;

    /** 派单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "派单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date assignTime;

    /** 接单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "接单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiveTime;

    /** 取货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "取货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pickTime;

    /** 送达时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "送达时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliverTime;

    /** 配送状态：0-待分配 1-已接单 2-已取货 3-已送达 */
    @Excel(name = "配送状态：0-待分配 1-已接单 2-已取货 3-已送达")
    private Long deliveryStatus;

    /** 用户支付的配送费 */
    @Excel(name = "用户支付的配送费")
    private BigDecimal deliveryFeeFromUser;

    /** 骑手实际收入 */
    @Excel(name = "骑手实际收入")
    private BigDecimal riderIncome;

    /** 收入发放状态：0-未发放 1-已发放 */
    @Excel(name = "收入发放状态")
    private Long incomeStatus;

    public void setOrderDeliveryId(Long orderDeliveryId) 
    {
        this.orderDeliveryId = orderDeliveryId;
    }

    public Long getOrderDeliveryId() 
    {
        return orderDeliveryId;
    }

    public void setOrderMainId(Long orderMainId) 
    {
        this.orderMainId = orderMainId;
    }

    public Long getOrderMainId() 
    {
        return orderMainId;
    }

    public void setRiderId(Long riderId) 
    {
        this.riderId = riderId;
    }

    public Long getRiderId() 
    {
        return riderId;
    }

    public void setRiderNickname(String riderNickname) 
    {
        this.riderNickname = riderNickname;
    }

    public String getRiderNickname() 
    {
        return riderNickname;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) 
    {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getDeliveryFee() 
    {
        return deliveryFee;
    }

    public void setActualPickLongitude(BigDecimal actualPickLongitude) 
    {
        this.actualPickLongitude = actualPickLongitude;
    }

    public BigDecimal getActualPickLongitude() 
    {
        return actualPickLongitude;
    }

    public void setActualPickLatitude(BigDecimal actualPickLatitude) 
    {
        this.actualPickLatitude = actualPickLatitude;
    }

    public BigDecimal getActualPickLatitude() 
    {
        return actualPickLatitude;
    }

    public void setActualDeliverLongitude(BigDecimal actualDeliverLongitude) 
    {
        this.actualDeliverLongitude = actualDeliverLongitude;
    }

    public BigDecimal getActualDeliverLongitude() 
    {
        return actualDeliverLongitude;
    }

    public void setActualDeliverLatitude(BigDecimal actualDeliverLatitude) 
    {
        this.actualDeliverLatitude = actualDeliverLatitude;
    }

    public BigDecimal getActualDeliverLatitude() 
    {
        return actualDeliverLatitude;
    }

    public void setAssignTime(Date assignTime) 
    {
        this.assignTime = assignTime;
    }

    public Date getAssignTime() 
    {
        return assignTime;
    }

    public void setReceiveTime(Date receiveTime) 
    {
        this.receiveTime = receiveTime;
    }

    public Date getReceiveTime() 
    {
        return receiveTime;
    }

    public void setPickTime(Date pickTime) 
    {
        this.pickTime = pickTime;
    }

    public Date getPickTime() 
    {
        return pickTime;
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

    public void setDeliveryFeeFromUser(BigDecimal deliveryFeeFromUser) {
        this.deliveryFeeFromUser = deliveryFeeFromUser;
    }

    public BigDecimal getDeliveryFeeFromUser() {
        return deliveryFeeFromUser;
    }

    public void setRiderIncome(BigDecimal riderIncome) {
        this.riderIncome = riderIncome;
    }

    public BigDecimal getRiderIncome() {
        return riderIncome;
    }

    public void setIncomeStatus(Long incomeStatus) {
        this.incomeStatus = incomeStatus;
    }

    public Long getIncomeStatus() {
        return incomeStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("orderDeliveryId", getOrderDeliveryId())
                .append("orderMainId", getOrderMainId())
                .append("riderId", getRiderId())
                .append("riderNickname", getRiderNickname())
                .append("deliveryFee", getDeliveryFee())
                .append("deliveryFeeFromUser", getDeliveryFeeFromUser())
                .append("riderIncome", getRiderIncome())
                .append("incomeStatus", getIncomeStatus())
                .append("actualPickLongitude", getActualPickLongitude())
                .append("actualPickLatitude", getActualPickLatitude())
                .append("actualDeliverLongitude", getActualDeliverLongitude())
                .append("actualDeliverLatitude", getActualDeliverLatitude())
                .append("assignTime", getAssignTime())
                .append("receiveTime", getReceiveTime())
                .append("pickTime", getPickTime())
                .append("deliverTime", getDeliverTime())
                .append("deliveryStatus", getDeliveryStatus())
                .toString();
    }
}
