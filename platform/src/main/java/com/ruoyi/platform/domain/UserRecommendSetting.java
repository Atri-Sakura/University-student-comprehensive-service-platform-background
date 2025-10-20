package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户个性化推荐设置对象 user_recommend_setting
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class UserRecommendSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一ID */
    private Long userRecommendSettingId;

    /** 所属用户ID（关联user_base.user_base_id） */
    @Excel(name = "所属用户ID", readConverterExp = "关=联user_base.user_base_id")
    private Long userBaseId;

    /** 是否开启个性化推荐：0-关闭 1-开启 */
    @Excel(name = "是否开启个性化推荐：0-关闭 1-开启")
    private Long isRecommendEnabled;

    /** 推荐频率：1-高频 2-中频 3-低频 */
    @Excel(name = "推荐频率：1-高频 2-中频 3-低频")
    private Long recommendFreq;

    /** 屏蔽的标签编码（逗号分隔） */
    @Excel(name = "屏蔽的标签编码", readConverterExp = "逗=号分隔")
    private String shieldedTagCodes;

    /** 偏好推荐场景（逗号分隔） */
    @Excel(name = "偏好推荐场景", readConverterExp = "逗=号分隔")
    private String preferredScene;

    public void setUserRecommendSettingId(Long userRecommendSettingId) 
    {
        this.userRecommendSettingId = userRecommendSettingId;
    }

    public Long getUserRecommendSettingId() 
    {
        return userRecommendSettingId;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
    }

    public void setIsRecommendEnabled(Long isRecommendEnabled) 
    {
        this.isRecommendEnabled = isRecommendEnabled;
    }

    public Long getIsRecommendEnabled() 
    {
        return isRecommendEnabled;
    }

    public void setRecommendFreq(Long recommendFreq) 
    {
        this.recommendFreq = recommendFreq;
    }

    public Long getRecommendFreq() 
    {
        return recommendFreq;
    }

    public void setShieldedTagCodes(String shieldedTagCodes) 
    {
        this.shieldedTagCodes = shieldedTagCodes;
    }

    public String getShieldedTagCodes() 
    {
        return shieldedTagCodes;
    }

    public void setPreferredScene(String preferredScene) 
    {
        this.preferredScene = preferredScene;
    }

    public String getPreferredScene() 
    {
        return preferredScene;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userRecommendSettingId", getUserRecommendSettingId())
            .append("userBaseId", getUserBaseId())
            .append("isRecommendEnabled", getIsRecommendEnabled())
            .append("recommendFreq", getRecommendFreq())
            .append("shieldedTagCodes", getShieldedTagCodes())
            .append("preferredScene", getPreferredScene())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .toString();
    }
}
