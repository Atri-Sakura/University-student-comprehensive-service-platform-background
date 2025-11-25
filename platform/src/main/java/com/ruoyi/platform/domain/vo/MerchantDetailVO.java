package com.ruoyi.platform.domain.vo;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 用户端商家详情视图对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantDetailVO {

    /** 商家ID */
    private Long merchantBaseId;

    /** 商家名称 */
    private String merchantName;

    /** 商家简介 */
    private String description;

    /** 商家Logo */
    private String logo;

    /** 经营范围 */
    private String businessScope;

    /** 营业时间 */
    private String businessHours;

    /** 配送范围(公里) */
    private BigDecimal deliveryRange;

    /** 起送金额 */
    private BigDecimal minOrderAmount;

    /** 配送费 */
    private BigDecimal deliveryFee;

    /** 商家评分 */
    private BigDecimal rating;

    /** 月销量 */
    private Long monthSales;

    /** 营业状态 */
    private Long businessStatus;

    /** 联系电话 */
    private String phone;

    /** 店铺经度 */
    private BigDecimal longitude;

    /** 店铺纬度 */
    private BigDecimal latitude;

    // Getters and Setters
    public Long getMerchantBaseId() {
        return merchantBaseId;
    }

    public void setMerchantBaseId(Long merchantBaseId) {
        this.merchantBaseId = merchantBaseId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public BigDecimal getDeliveryRange() {
        return deliveryRange;
    }

    public void setDeliveryRange(BigDecimal deliveryRange) {
        this.deliveryRange = deliveryRange;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Long getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(Long monthSales) {
        this.monthSales = monthSales;
    }

    public Long getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(Long businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}