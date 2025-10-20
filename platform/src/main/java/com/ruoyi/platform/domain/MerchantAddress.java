package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商家地址对象 merchant_address
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class MerchantAddress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地址ID */
    private Long merchantAddressId;

    /** 商家ID */
    @Excel(name = "商家ID")
    private Long merchantBaseId;

    /** 省份 */
    @Excel(name = "省份")
    private String province;

    /** 城市 */
    @Excel(name = "城市")
    private String city;

    /** 区县 */
    @Excel(name = "区县")
    private String district;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String detailAddress;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactPerson;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String contactPhone;

    public void setMerchantAddressId(Long merchantAddressId) 
    {
        this.merchantAddressId = merchantAddressId;
    }

    public Long getMerchantAddressId() 
    {
        return merchantAddressId;
    }

    public void setMerchantBaseId(Long merchantBaseId) 
    {
        this.merchantBaseId = merchantBaseId;
    }

    public Long getMerchantBaseId() 
    {
        return merchantBaseId;
    }

    public void setProvince(String province) 
    {
        this.province = province;
    }

    public String getProvince() 
    {
        return province;
    }

    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }

    public void setDistrict(String district) 
    {
        this.district = district;
    }

    public String getDistrict() 
    {
        return district;
    }

    public void setDetailAddress(String detailAddress) 
    {
        this.detailAddress = detailAddress;
    }

    public String getDetailAddress() 
    {
        return detailAddress;
    }

    public void setContactPerson(String contactPerson) 
    {
        this.contactPerson = contactPerson;
    }

    public String getContactPerson() 
    {
        return contactPerson;
    }

    public void setContactPhone(String contactPhone) 
    {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() 
    {
        return contactPhone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("merchantAddressId", getMerchantAddressId())
            .append("merchantBaseId", getMerchantBaseId())
            .append("province", getProvince())
            .append("city", getCity())
            .append("district", getDistrict())
            .append("detailAddress", getDetailAddress())
            .append("contactPerson", getContactPerson())
            .append("contactPhone", getContactPhone())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
