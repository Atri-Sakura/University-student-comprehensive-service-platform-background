package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户隐私设置对象 user_privacy
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class UserPrivacy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设置唯一ID */
    private Long userPrivacyId;

    /** 所属用户ID */
    @Excel(name = "所属用户ID")
    private Long userBaseId;

    /** 个性化推荐：0-关闭 1-开启 */
    @Excel(name = "个性化推荐：0-关闭 1-开启")
    private Long isRecommend;

    /** 位置权限：0-关闭 1-开启 */
    @Excel(name = "位置权限：0-关闭 1-开启")
    private Long isLocationPermit;

    public void setUserPrivacyId(Long userPrivacyId) 
    {
        this.userPrivacyId = userPrivacyId;
    }

    public Long getUserPrivacyId() 
    {
        return userPrivacyId;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
    }

    public void setIsRecommend(Long isRecommend) 
    {
        this.isRecommend = isRecommend;
    }

    public Long getIsRecommend() 
    {
        return isRecommend;
    }

    public void setIsLocationPermit(Long isLocationPermit) 
    {
        this.isLocationPermit = isLocationPermit;
    }

    public Long getIsLocationPermit() 
    {
        return isLocationPermit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userPrivacyId", getUserPrivacyId())
            .append("userBaseId", getUserBaseId())
            .append("isRecommend", getIsRecommend())
            .append("isLocationPermit", getIsLocationPermit())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
