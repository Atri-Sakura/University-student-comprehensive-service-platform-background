package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商家基础信息对象 merchant_base
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class MerchantBase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商家唯一ID */
    private Long merchantBaseId;

    /** 登录账号 */
    @Excel(name = "登录账号")
    private String username;

    /** 密码(BCrypt加密) */
    @Excel(name = "密码(BCrypt加密)")
    private String password;

    /** 商家名称 */
    @Excel(name = "商家名称")
    private String merchantName;

    private String description;

    /** 商家Logo URL */
    @Excel(name = "商家Logo URL")
    private String logo;

    /** 店铺地址ID */
    @Excel(name = "店铺地址ID")
    private Long merchantAddressId;

    /** 经营范围 */
    @Excel(name = "经营范围")
    private String businessScope;

    /** 营业时间 */
    @Excel(name = "营业时间")
    private String businessHours;

    /** 配送范围(公里) */
    @Excel(name = "配送范围(公里)")
    private BigDecimal deliveryRange;

    /** 起送金额 */
    @Excel(name = "起送金额")
    private BigDecimal minOrderAmount;

    /** 基础配送费 */
    @Excel(name = "基础配送费")
    private BigDecimal deliveryFee;

    /** 营业执照URL */
    @Excel(name = "营业执照URL")
    private String licenseImg;

    /** 商家评分 */
    @Excel(name = "商家评分")
    private BigDecimal rating;

    /** 月销量 */
    @Excel(name = "月销量")
    private Long monthSales;

    /** 审核状态：0-待审核 1-通过 2-拒绝 */
    @Excel(name = "审核状态：0-待审核 1-通过 2-拒绝")
    private Long auditStatus;

    /** 营业状态：0-停业 1-营业 */
    @Excel(name = "营业状态：0-停业 1-营业")
    private Long businessStatus;

    /** 店铺经度 */
    @Excel(name = "店铺经度")
    private BigDecimal longitude;

    /** 店铺纬度 */
    @Excel(name = "店铺纬度")
    private BigDecimal latitude;

    /** 支付密码 */
    @Excel(name = "支付密码")
    private String payPassword;

    /** 对应sys_user表的用户ID */
    private Long userId;

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setMerchantBaseId(Long merchantBaseId) 
    {
        this.merchantBaseId = merchantBaseId;
    }

    public Long getMerchantBaseId() 
    {
        return merchantBaseId;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setMerchantName(String merchantName) 
    {
        this.merchantName = merchantName;
    }

    public String getMerchantName() 
    {
        return merchantName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLogo(String logo) 
    {
        this.logo = logo;
    }

    public String getLogo() 
    {
        return logo;
    }

    public void setMerchantAddressId(Long merchantAddressId) 
    {
        this.merchantAddressId = merchantAddressId;
    }

    public Long getMerchantAddressId() 
    {
        return merchantAddressId;
    }

    public void setBusinessScope(String businessScope) 
    {
        this.businessScope = businessScope;
    }

    public String getBusinessScope() 
    {
        return businessScope;
    }

    public void setBusinessHours(String businessHours) 
    {
        this.businessHours = businessHours;
    }

    public String getBusinessHours() 
    {
        return businessHours;
    }

    public void setDeliveryRange(BigDecimal deliveryRange) 
    {
        this.deliveryRange = deliveryRange;
    }

    public BigDecimal getDeliveryRange() 
    {
        return deliveryRange;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) 
    {
        this.minOrderAmount = minOrderAmount;
    }

    public BigDecimal getMinOrderAmount() 
    {
        return minOrderAmount;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) 
    {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getDeliveryFee() 
    {
        return deliveryFee;
    }

    public void setLicenseImg(String licenseImg) 
    {
        this.licenseImg = licenseImg;
    }

    public String getLicenseImg() 
    {
        return licenseImg;
    }

    public void setRating(BigDecimal rating) 
    {
        this.rating = rating;
    }

    public BigDecimal getRating() 
    {
        return rating;
    }

    public void setMonthSales(Long monthSales) 
    {
        this.monthSales = monthSales;
    }

    public Long getMonthSales() 
    {
        return monthSales;
    }

    public void setAuditStatus(Long auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public Long getAuditStatus() 
    {
        return auditStatus;
    }

    public void setBusinessStatus(Long businessStatus) 
    {
        this.businessStatus = businessStatus;
    }

    public Long getBusinessStatus() 
    {
        return businessStatus;
    }

    public void setLongitude(BigDecimal longitude) 
    {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude() 
    {
        return longitude;
    }

    public void setLatitude(BigDecimal latitude) 
    {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude() 
    {
        return latitude;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("merchantBaseId", getMerchantBaseId())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("merchantName", getMerchantName())
            .append("logo", getLogo())
            .append("merchantAddressId", getMerchantAddressId())
            .append("businessScope", getBusinessScope())
            .append("businessHours", getBusinessHours())
            .append("deliveryRange", getDeliveryRange())
            .append("minOrderAmount", getMinOrderAmount())
            .append("deliveryFee", getDeliveryFee())
            .append("licenseImg", getLicenseImg())
            .append("rating", getRating())
            .append("monthSales", getMonthSales())
            .append("auditStatus", getAuditStatus())
            .append("businessStatus", getBusinessStatus())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
