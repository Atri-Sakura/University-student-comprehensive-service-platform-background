package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户偏好标签对象 user_preference_tag
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class UserPreferenceTag extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一ID */
    private Long userPreferenceTagId;

    /** 所属用户ID（关联user_base.user_base_id） */
    @Excel(name = "所属用户ID", readConverterExp = "关=联user_base.user_base_id")
    private Long userBaseId;

    /** 标签编码（唯一标识，如FOOD_SPICY/STATIONERY） */
    @Excel(name = "标签编码", readConverterExp = "唯=一标识，如FOOD_SPICY/STATIONERY")
    private String tagCode;

    /** 标签名称（如"爱吃辣""文具刚需"） */
    @Excel(name = "标签名称", readConverterExp = "如=爱吃辣,文具刚需")
    private String tagName;

    /** 标签类型（如FOOD-美食偏好/SHOPPING-购物偏好） */
    @Excel(name = "标签类型", readConverterExp = "如=FOOD-美食偏好/SHOPPING-购物偏好")
    private String tagType;

    /** 偏好分数（1-100，分数越高偏好越强） */
    @Excel(name = "偏好分数", readConverterExp = "1=-100，分数越高偏好越强")
    private Long score;

    /** 标签来源：1-用户主动设置 2-系统行为分析 3-人工标注 */
    @Excel(name = "标签来源：1-用户主动设置 2-系统行为分析 3-人工标注")
    private Long source;

    public void setUserPreferenceTagId(Long userPreferenceTagId) 
    {
        this.userPreferenceTagId = userPreferenceTagId;
    }

    public Long getUserPreferenceTagId() 
    {
        return userPreferenceTagId;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
    }

    public void setTagCode(String tagCode) 
    {
        this.tagCode = tagCode;
    }

    public String getTagCode() 
    {
        return tagCode;
    }

    public void setTagName(String tagName) 
    {
        this.tagName = tagName;
    }

    public String getTagName() 
    {
        return tagName;
    }

    public void setTagType(String tagType) 
    {
        this.tagType = tagType;
    }

    public String getTagType() 
    {
        return tagType;
    }

    public void setScore(Long score) 
    {
        this.score = score;
    }

    public Long getScore() 
    {
        return score;
    }

    public void setSource(Long source) 
    {
        this.source = source;
    }

    public Long getSource() 
    {
        return source;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userPreferenceTagId", getUserPreferenceTagId())
            .append("userBaseId", getUserBaseId())
            .append("tagCode", getTagCode())
            .append("tagName", getTagName())
            .append("tagType", getTagType())
            .append("score", getScore())
            .append("source", getSource())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
