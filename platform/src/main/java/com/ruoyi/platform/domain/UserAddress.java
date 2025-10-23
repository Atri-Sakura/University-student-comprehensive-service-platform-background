package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户地址对象 user_address
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class UserAddress extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地址唯一ID */
    private Long userAddressId;

    /** 所属用户ID（关联user_base.user_base_id） */
    @Excel(name = "所属用户ID", readConverterExp = "关=联user_base.user_base_id")
    private Long userBaseId;

    /** 收货人姓名 */
    @Excel(name = "收货人姓名")
    private String receiver;

    /** 收货人电话（AES加密） */
    @Excel(name = "收货人电话", readConverterExp = "A=ES加密")
    private String phone;

    /** 省份 */
    @Excel(name = "省份")
    private String province;

    /** 城市 */
    @Excel(name = "城市")
    private String city;

    /** 区县 */
    @Excel(name = "区县")
    private String district;

    /** 详细地址（如XX宿舍3栋201） */
    @Excel(name = "详细地址", readConverterExp = "如=XX宿舍3栋201")
    private String detailAddress;

    /** 地址标签（如DORM-宿舍/CLASSROOM-教室） */
    @Excel(name = "地址标签", readConverterExp = "如=DORM-宿舍/CLASSROOM-教室")
    private String addressTag;

    /** 经度 */
    @Excel(name = "经度")
    private BigDecimal longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private BigDecimal latitude;

    /** 是否默认地址：0-否 1-是 */
    @Excel(name = "是否默认地址：0-否 1-是")
    private Long isDefault;

    public void setUserAddressId(Long userAddressId) 
    {
        this.userAddressId = userAddressId;
    }

    public Long getUserAddressId() 
    {
        return userAddressId;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
    }

    public void setReceiver(String receiver) 
    {
        this.receiver = receiver;
    }

    public String getReceiver() 
    {
        return receiver;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
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

    public void setAddressTag(String addressTag) 
    {
        this.addressTag = addressTag;
    }

    public String getAddressTag() 
    {
        return addressTag;
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

    public void setIsDefault(Long isDefault) 
    {
        this.isDefault = isDefault;
    }

    public Long getIsDefault() 
    {
        return isDefault;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userAddressId", getUserAddressId())
            .append("userBaseId", getUserBaseId())
            .append("receiver", getReceiver())
            .append("phone", getPhone())
            .append("province", getProvince())
            .append("city", getCity())
            .append("district", getDistrict())
            .append("detailAddress", getDetailAddress())
            .append("addressTag", getAddressTag())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("isDefault", getIsDefault())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
