package com.ruoyi.platform.rider.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 骑手订单列表VO（简化字段）
 *
 * @author ruoyi
 */
public class RiderOrderListVO {

    /** 订单ID */
    private Long orderMainId;

    /** 订单编号 */
    private String orderNo;

    /** 下单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 取货地址（绿点显示） */
    private String pickAddress;

    /** 送货地址（红点显示） */
    private String deliverAddress;

    /** 订单金额 */
    private BigDecimal totalAmount;

    /** 订单状态：1-待接单 2-待取货 3-配送中 4-已完成 5-已取消 */
    private Integer orderStatus;

    /** 订单状态名称 */
    private String orderStatusName;

    /** 订单类型：1-外卖单 2-跑腿单 3-二手交易单 */
    private Integer orderType;

    /** 订单类型名称 */
    private String orderTypeName;

    // Getter和Setter方法
    public Long getOrderMainId() {
        return orderMainId;
    }

    public void setOrderMainId(Long orderMainId) {
        this.orderMainId = orderMainId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPickAddress() {
        return pickAddress;
    }

    public void setPickAddress(String pickAddress) {
        this.pickAddress = pickAddress;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
        // 自动设置状态名称
        this.orderStatusName = getStatusName(orderStatus);
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this. orderStatusName = orderStatusName;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
        // 自动设置类型名称
        this.orderTypeName = getTypeName(orderType);
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    /**
     * 获取订单状态名称
     */
    private String getStatusName(Integer status) {
        if (status == null) return "";
        switch (status) {
            case 1: return "待接单";
            case 2: return "待取货";
            case 3: return "配送中";
            case 4: return "已完成";
            case 5: return "已取消";
            default: return "未知状态";
        }
    }

    /**
     * 获取订单类型名称
     */
    private String getTypeName(Integer type) {
        if (type == null) return "";
        switch (type) {
            case 1: return "外卖";
            case 2: return "跑腿";
            case 3: return "二手";
            default: return "未知";
        }
    }
}