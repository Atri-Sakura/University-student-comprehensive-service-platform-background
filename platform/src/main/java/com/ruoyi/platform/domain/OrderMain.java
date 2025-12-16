package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单主（整合地址与定位信息）对象 order_main
 *
 * @author ruoyi
 * @date 2025-10-20
 */
public class OrderMain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单唯一ID */
    private Long orderMainId;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 下单用户ID（关联user_db.user_base.user_base_id） */
    @Excel(name = "下单用户ID", readConverterExp = "关=联user_db.user_base.user_base_id")
    private Long userId;

    /** 用户账号(手机号) */
    @Excel(name = "用户账号")
    private String username;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String userNickname;

    /** 订单类型：1-外卖单 2-跑腿单 3-二手交易单 */
    @Excel(name = "订单类型：1-外卖单 2-跑腿单 3-二手交易单")
    private Long orderType;

    /** 订单总金额 */
    @Excel(name = "订单总金额")
    private BigDecimal totalAmount;

    /** 实付金额 */
    @Excel(name = "实付金额")
    private BigDecimal payAmount;

    /** 优惠金额 */
    @Excel(name = "优惠金额")
    private BigDecimal discountAmount;

    /** 支付状态：0-未支付 1-已支付 2-退款中 3-已退款 */
    @Excel(name = "支付状态：0-未支付 1-已支付 2-退款中 3-已退款")
    private Long payStatus;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 支付方式：1-余额 2-微信 3-支付宝 */
    @Excel(name = "支付方式：1-余额 2-微信 3-支付宝")
    private Long payType;

    /** 订单状态：1-商家待接单 2-骑手待接单 3-骑手待取货 4-配送中 5-已完成 6-已取消 7-异常报备*/
    @Excel(name = "订单状态：1-商家待接单 2-骑手待接单 3-骑手待取货 4-配送中 5-已完成 6-已取消 7-异常报备")
    private Long orderStatus;

    /** 取消原因 */
    @Excel(name = "取消原因")
    private String cancelReason;

    /** 取消操作人 */
    @Excel(name = "取消操作人")
    private String cancelOperator;

    /** 取货地址ID（外卖关联merchant_db.merchant_address.merchant_address_id；其他关联user_db.user_address.user_address_id） */
    @Excel(name = "取货地址ID", readConverterExp = "外=卖关联merchant_db.merchant_address.merchant_address_id；其他关联user_db.user_address.user_address_id")
    private Long pickAddressId;

    /** 取货地址文本（冗余，如"XX食堂3楼奶茶店""XX宿舍2栋101"） */
    @Excel(name = "取货地址文本", readConverterExp = "冗=余，如“XX食堂3楼奶茶店”“XX宿舍2栋101”")
    private String pickAddress;

    /** 取货联系人 */
    @Excel(name = "取货联系人")
    private String pickContact;

    /** 取货电话（AES加密，与user_db加密标准一致） */
    @Excel(name = "取货电话", readConverterExp = "A=ES加密，与user_db加密标准一致")
    private String pickPhone;

    /** 取货经度（定位功能填充，WGS84坐标系，精度1米内） */
    @Excel(name = "取货经度", readConverterExp = "定=位功能填充，WGS84坐标系，精度1米内")
    private BigDecimal pickLongitude;

    /** 取货纬度（定位功能填充，WGS84坐标系，精度1米内） */
    @Excel(name = "取货纬度", readConverterExp = "定=位功能填充，WGS84坐标系，精度1米内")
    private BigDecimal pickLatitude;

    /** 送货地址ID（关联user_db.user_address.user_address_id，线下二手单可空） */
    @Excel(name = "送货地址ID", readConverterExp = "关=联user_db.user_address.user_address_id，线下二手单可空")
    private Long deliverAddressId;

    /** 送货地址文本（冗余，如"XX教学楼503室"） */
    @Excel(name = "送货地址文本", readConverterExp = "冗=余，如“XX教学楼503室”")
    private String deliverAddress;

    /** 收货联系人 */
    @Excel(name = "收货联系人")
    private String deliverContact;

    /** 收货电话（AES加密，与user_db加密标准一致） */
    @Excel(name = "收货电话", readConverterExp = "A=ES加密，与user_db加密标准一致")
    private String deliverPhone;

    /** 送货经度（定位功能填充，WGS84坐标系） */
    @Excel(name = "送货经度", readConverterExp = "定=位功能填充，WGS84坐标系")
    private BigDecimal deliverLongitude;

    /** 送货纬度（定位功能填充，WGS84坐标系） */
    @Excel(name = "送货纬度", readConverterExp = "定=位功能填充，WGS84坐标系")
    private BigDecimal deliverLatitude;

    /** 完成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completeTime;

    /** 商家ID */
    @Excel(name = "商家ID")
    private Long merchantId;

    /** 平台暂存金额 */
    @Excel(name = "平台暂存金额")
    private BigDecimal platformHoldAmount;

    /** 商品金额（不含配送费） */
    @Excel(name = "商品金额")
    private BigDecimal goodsAmount;

    /** 配送费金额 */
    @Excel(name = "配送费金额")
    private BigDecimal deliveryFeeAmount;

    /** 商家名称 */
    @Excel(name = "商家名称")
    private String merchantName;

    /** 商家Logo */
    @Excel(name = "商家Logo")
    private String merchantLogo;

    /** 商家评分 */
    @Excel(name = "商家评分")
    private BigDecimal merchantRating;

    /** 商家电话 */
    @Excel(name = "商家电话")
    private String merchantPhone;

    /** 商家营业时间 */
    @Excel(name = "商家营业时间")
    private String merchantBusinessHours;

    /** 骑手ID（来自 order_delivery.rider_id） */
    @Excel(name = "骑手ID")
    private Long riderId;

    /** 骑手昵称（来自 rider_base.nickname） */
    @Excel(name = "骑手昵称")
    private String riderNickname;

    /** 骑手手机号（来自 rider_base.phone） */
    @Excel(name = "骑手手机号")
    private String riderPhone;

    /** 骑手头像（来自 rider_base.avatar） */
    @Excel(name = "骑手头像")
    private String riderAvatar;

    /** 骑手真实姓名（来自 rider_base.real_name） */
    @Excel(name = "骑手真实姓名")
    private String riderRealName;

    /** 骑手工作状态（来自 rider_base.work_status：0-下线 1-上线 2-忙碌） */
    @Excel(name = "骑手工作状态")
    private Long riderWorkStatus;

    /** 骑手信用分（来自 rider_base.credit_score） */
    @Excel(name = "骑手信用分")
    private Long riderCreditScore;

    /** 配送状态（来自 order_delivery.delivery_status：0-待分配 1-已接单 2-已取货 3-已送达） */
    @Excel(name = "配送状态")
    private Long deliveryStatus;

    /** 接单时间（来自 order_delivery.receive_time） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "接单时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    /** 取货时间（来自 order_delivery.pick_time） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "取货时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date pickTime;

    /** 送达时间（来自 order_delivery.deliver_time） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "送达时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deliverTime;

    /** 配送费（来自 order_delivery. delivery_fee） */
    @Excel(name = "配送费")
    private BigDecimal deliveryFee;

    /** 骑手收入（来自 order_delivery.rider_income） */
    @Excel(name = "骑手收入")
    private BigDecimal riderIncome;

    /** 订单缩略图 */
    @Excel(name = "订单缩略图")
    private String orderThumbnail;

    // ========== 新增：配送信息（关联order_delivery表） ==========
    /** 配送记录对象 */
    private OrderDelivery orderDelivery;

    // ========== 新增：骑手信息（关联rider_base表） ==========
    /** 骑手基础信息对象 */
    private RiderBase riderBase;

    private List<OrderTakeoutDetail> orderTakeoutDetailList;

    private List<OrderErrandDetail> orderErrandDetailList;

    private List<OrderSecondhandDetail> orderSecondhandDetailList;

    public List<OrderTakeoutDetail> getOrderTakeoutDetailList() {
        return orderTakeoutDetailList;
    }

    public void setOrderTakeoutDetailList(List<OrderTakeoutDetail> orderTakeoutDetailList) {
        this.orderTakeoutDetailList = orderTakeoutDetailList;
    }

    public void setOrderMainId(Long orderMainId)
    {
        this.orderMainId = orderMainId;
    }

    public Long getOrderMainId()
    {
        return orderMainId;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
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

    public void setOrderType(Long orderType)
    {
        this.orderType = orderType;
    }

    public Long getOrderType()
    {
        return orderType;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setPayAmount(BigDecimal payAmount)
    {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmount()
    {
        return payAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountAmount()
    {
        return discountAmount;
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

    public void setPayType(Long payType)
    {
        this.payType = payType;
    }

    public Long getPayType()
    {
        return payType;
    }

    public void setOrderStatus(Long orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public Long getOrderStatus()
    {
        return orderStatus;
    }

    public void setCancelReason(String cancelReason)
    {
        this.cancelReason = cancelReason;
    }

    public String getCancelReason()
    {
        return cancelReason;
    }

    public void setCancelOperator(String cancelOperator)
    {
        this.cancelOperator = cancelOperator;
    }

    public String getCancelOperator()
    {
        return cancelOperator;
    }

    public void setPickAddressId(Long pickAddressId)
    {
        this.pickAddressId = pickAddressId;
    }

    public Long getPickAddressId()
    {
        return pickAddressId;
    }

    public void setPickAddress(String pickAddress)
    {
        this.pickAddress = pickAddress;
    }

    public String getPickAddress()
    {
        return pickAddress;
    }

    public void setPickContact(String pickContact)
    {
        this.pickContact = pickContact;
    }

    public String getPickContact()
    {
        return pickContact;
    }

    public void setPickPhone(String pickPhone)
    {
        this.pickPhone = pickPhone;
    }

    public String getPickPhone()
    {
        return pickPhone;
    }

    public void setPickLongitude(BigDecimal pickLongitude)
    {
        this.pickLongitude = pickLongitude;
    }

    public BigDecimal getPickLongitude()
    {
        return pickLongitude;
    }

    public void setPickLatitude(BigDecimal pickLatitude)
    {
        this.pickLatitude = pickLatitude;
    }

    public BigDecimal getPickLatitude()
    {
        return pickLatitude;
    }

    public void setDeliverAddressId(Long deliverAddressId)
    {
        this.deliverAddressId = deliverAddressId;
    }

    public Long getDeliverAddressId()
    {
        return deliverAddressId;
    }

    public void setDeliverAddress(String deliverAddress)
    {
        this.deliverAddress = deliverAddress;
    }

    public String getDeliverAddress()
    {
        return deliverAddress;
    }

    public void setDeliverContact(String deliverContact)
    {
        this.deliverContact = deliverContact;
    }

    public String getDeliverContact()
    {
        return deliverContact;
    }

    public void setDeliverPhone(String deliverPhone)
    {
        this.deliverPhone = deliverPhone;
    }

    public String getDeliverPhone()
    {
        return deliverPhone;
    }

    public void setDeliverLongitude(BigDecimal deliverLongitude)
    {
        this.deliverLongitude = deliverLongitude;
    }

    public BigDecimal getDeliverLongitude()
    {
        return deliverLongitude;
    }

    public void setDeliverLatitude(BigDecimal deliverLatitude)
    {
        this.deliverLatitude = deliverLatitude;
    }

    public BigDecimal getDeliverLatitude()
    {
        return deliverLatitude;
    }

    public void setCompleteTime(Date completeTime)
    {
        this.completeTime = completeTime;
    }

    public Date getCompleteTime()
    {
        return completeTime;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setPlatformHoldAmount(BigDecimal platformHoldAmount) {
        this.platformHoldAmount = platformHoldAmount;
    }

    public BigDecimal getPlatformHoldAmount() {
        return platformHoldAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setDeliveryFeeAmount(BigDecimal deliveryFeeAmount) {
        this.deliveryFeeAmount = deliveryFeeAmount;
    }

    public BigDecimal getDeliveryFeeAmount() {
        return deliveryFeeAmount;
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

    public String getMerchantBusinessHours() {
        return merchantBusinessHours;
    }

    public void setMerchantBusinessHours(String merchantBusinessHours) {
        this.merchantBusinessHours = merchantBusinessHours;
    }

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    public String getRiderNickname() {
        return riderNickname;
    }

    public void setRiderNickname(String riderNickname) {
        this.riderNickname = riderNickname;
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) {
        this.riderPhone = riderPhone;
    }

    public String getRiderAvatar() {
        return riderAvatar;
    }

    public void setRiderAvatar(String riderAvatar) {
        this.riderAvatar = riderAvatar;
    }

    public String getRiderRealName() {
        return riderRealName;
    }

    public void setRiderRealName(String riderRealName) {
        this.riderRealName = riderRealName;
    }

    public Long getRiderWorkStatus() {
        return riderWorkStatus;
    }

    public void setRiderWorkStatus(Long riderWorkStatus) {
        this.riderWorkStatus = riderWorkStatus;
    }

    public Long getRiderCreditScore() {
        return riderCreditScore;
    }

    public void setRiderCreditScore(Long riderCreditScore) {
        this.riderCreditScore = riderCreditScore;
    }

    public Long getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Long deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getRiderIncome() {
        return riderIncome;
    }

    public void setRiderIncome(BigDecimal riderIncome) {
        this.riderIncome = riderIncome;
    }

    public List<OrderErrandDetail> getOrderErrandDetailList() {
        return orderErrandDetailList;
    }

    public void setOrderErrandDetailList(List<OrderErrandDetail> orderErrandDetailList) {
        this.orderErrandDetailList = orderErrandDetailList;
    }

    public List<OrderSecondhandDetail> getOrderSecondhandDetailList() {
        return orderSecondhandDetailList;
    }

    public void setOrderSecondhandDetailList(List<OrderSecondhandDetail> orderSecondhandDetailList) {
        this.orderSecondhandDetailList = orderSecondhandDetailList;
    }

    public String getOrderThumbnail() {
        return orderThumbnail;
    }

    public void setOrderThumbnail(String orderThumbnail) {
        this.orderThumbnail = orderThumbnail;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("orderMainId", getOrderMainId())
                .append("orderNo", getOrderNo())
                .append("userId", getUserId())
                .append("username", getUsername())
                .append("userNickname", getUserNickname())
                .append("merchantId", getMerchantId())
                .append("orderType", getOrderType())
                .append("totalAmount", getTotalAmount())
                .append("payAmount", getPayAmount())
                .append("discountAmount", getDiscountAmount())
                .append("platformHoldAmount", getPlatformHoldAmount())
                .append("goodsAmount", getGoodsAmount())
                .append("deliveryFeeAmount", getDeliveryFeeAmount())
                .append("payStatus", getPayStatus())
                .append("payTime", getPayTime())
                .append("payType", getPayType())
                .append("orderStatus", getOrderStatus())
                .append("cancelReason", getCancelReason())
                .append("cancelOperator", getCancelOperator())
                .append("pickAddressId", getPickAddressId())
                .append("pickAddress", getPickAddress())
                .append("pickContact", getPickContact())
                .append("pickPhone", getPickPhone())
                .append("pickLongitude", getPickLongitude())
                .append("pickLatitude", getPickLatitude())
                .append("deliverAddressId", getDeliverAddressId())
                .append("deliverAddress", getDeliverAddress())
                .append("deliverContact", getDeliverContact())
                .append("deliverPhone", getDeliverPhone())
                .append("deliverLongitude", getDeliverLongitude())
                .append("deliverLatitude", getDeliverLatitude())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("completeTime", getCompleteTime())
                .append("merchantName", getMerchantName())
                .append("merchantLogo", getMerchantLogo())
                .append("merchantRating", getMerchantRating())
                .append("merchantPhone", getMerchantPhone())
                .append("merchantBusinessHours", getMerchantBusinessHours())
                .append("riderId", getRiderId())
                .append("riderNickname", getRiderNickname())
                .append("riderPhone", getRiderPhone())
                .append("riderAvatar", getRiderAvatar())
                .append("riderRealName", getRiderRealName())
                .append("riderWorkStatus", getRiderWorkStatus())
                .append("riderCreditScore", getRiderCreditScore())
                .append("deliveryStatus", getDeliveryStatus())
                .append("receiveTime", getReceiveTime())
                .append("pickTime", getPickTime())
                .append("deliverTime", getDeliverTime())
                .append("deliveryFee", getDeliveryFee())
                .append("riderIncome", getRiderIncome())
                .append("orderThumbnail", getOrderThumbnail())
                .toString();
    }
}