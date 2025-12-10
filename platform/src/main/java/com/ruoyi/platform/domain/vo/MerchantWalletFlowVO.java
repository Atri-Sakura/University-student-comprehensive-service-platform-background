package com.ruoyi.platform.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家钱包流水展示对象
 */
public class MerchantWalletFlowVO {

    /** 流水ID */
    private Long flowId;

    /** 商家ID */
    private Long merchantBaseId;

    /** 关联订单ID */
    private Long orderMainId;

    /** 订单编号 */
    private String orderNo;

    /** 流水类型 */
    private String flowType;

    /** 流水类型描述 */
    private String flowTypeDesc;

    /** 变动金额 */
    private BigDecimal flowAmount;

    /** 商品金额 */
    private BigDecimal goodsAmount;

    /** 配送费 */
    private BigDecimal deliveryFeeAmount;

    /** 用户实付金额 */
    private BigDecimal payAmount;

    /** 变动后余额 */
    private BigDecimal balanceAfter;

    /** 描述 */
    private String description;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // ==================== 订单详情字段 ====================

    /** 下单用户昵称 */
    private String userNickname;

    /** 订单类型 */
    private Long orderType;

    /** 订单类型描述 */
    private String orderTypeDesc;

    /** 订单状态 */
    private Long orderStatus;

    /** 订单状态描述 */
    private String orderStatusDesc;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm: ss")
    private Date completeTime;

    /** 商品明细（仅外卖订单） */
    private String goodsDetail;

    // ==================== Getter/Setter ====================

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getMerchantBaseId() {
        return merchantBaseId;
    }

    public void setMerchantBaseId(Long merchantBaseId) {
        this.merchantBaseId = merchantBaseId;
    }

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

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public String getFlowTypeDesc() {
        return flowTypeDesc;
    }

    public void setFlowTypeDesc(String flowTypeDesc) {
        this.flowTypeDesc = flowTypeDesc;
    }

    public BigDecimal getFlowAmount() {
        return flowAmount;
    }

    public void setFlowAmount(BigDecimal flowAmount) {
        this.flowAmount = flowAmount;
    }

    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getDeliveryFeeAmount() {
        return deliveryFeeAmount;
    }

    public void setDeliveryFeeAmount(BigDecimal deliveryFeeAmount) {
        this.deliveryFeeAmount = deliveryFeeAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public Long getOrderType() {
        return orderType;
    }

    public void setOrderType(Long orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeDesc() {
        return orderTypeDesc;
    }

    public void setOrderTypeDesc(String orderTypeDesc) {
        this.orderTypeDesc = orderTypeDesc;
    }

    public Long getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Long orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusDesc() {
        return orderStatusDesc;
    }

    public void setOrderStatusDesc(String orderStatusDesc) {
        this.orderStatusDesc = orderStatusDesc;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }
}