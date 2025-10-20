package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单主（整合地址与定位信息）对象 order_main
 * 
 * @author ruoyi
 * @date 2025-10-16
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

    /** 用户昵称(冗余) */
    @Excel(name = "用户昵称(冗余)")
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

    /** 订单状态：1-待接单 2-待取货 3-配送中 4-已完成 5-已取消 */
    @Excel(name = "订单状态：1-待接单 2-待取货 3-配送中 4-已完成 5-已取消")
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

    /** 取货地址文本（冗余，如“XX食堂3楼奶茶店”“XX宿舍2栋101”） */
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

    /** 送货地址文本（冗余，如“XX教学楼503室”） */
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

    public void setUserNickname(String userNickname) 
    {
        this.userNickname = userNickname;
    }

    public String getUserNickname() 
    {
        return userNickname;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderMainId", getOrderMainId())
            .append("orderNo", getOrderNo())
            .append("userId", getUserId())
            .append("userNickname", getUserNickname())
            .append("orderType", getOrderType())
            .append("totalAmount", getTotalAmount())
            .append("payAmount", getPayAmount())
            .append("discountAmount", getDiscountAmount())
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
            .toString();
    }
}
