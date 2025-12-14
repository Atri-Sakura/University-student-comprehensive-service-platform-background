package com.ruoyi.platform.rider.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 骑手订单列表VO
 */
public class RiderOrderListVO {

    /** 订单ID */
    private Long orderMainId;

    /** 订单编号 */
    private String orderNo;

    /** 下单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 订单缩略图 */
    private String orderThumbnail;

    /** 取货地址 */
    private String pickAddress;

    /** 送货地址 */
    private String deliverAddress;

    /** 订单金额 */
    private BigDecimal totalAmount;

    /** 订单状态 */
    private Integer orderStatus;

    /** 订单状态名称 */
    private String orderStatusName;

    /** 订单类型 */
    private Integer orderType;

    /** 订单类型名称 */
    private String orderTypeName;

    /** 商家ID */
    private Long merchantId;

    /** 商家名称 */
    private String merchantName;

    /** 商家Logo */
    private String merchantLogo;

    /** 商家评分 */
    private BigDecimal merchantRating;

    /** 商家电话 */
    private String merchantPhone;

    /** 用户账号(手机号) */
    private String username;

    /** 用户昵称 */
    private String userNickname;

    // ==================== Getter和Setter方法 ====================

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

    public String getOrderThumbnail() {
        return orderThumbnail;
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
        this.orderStatusName = getStatusName(orderStatus);
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
        this.orderTypeName = getTypeName(orderType);
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public BigDecimal getMerchantRating() {
        return merchantRating;
    }

    public void setMerchantRating(BigDecimal merchantRating) {
        this.merchantRating = merchantRating;
    }

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    // ==================== 辅助方法 ====================

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