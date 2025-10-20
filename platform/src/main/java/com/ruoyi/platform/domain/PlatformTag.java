package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 平台标签体系（管理用户和商品标签）对象 platform_tag
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class PlatformTag extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 标签ID */
    private Long platformTagId;

    /** 标签编码（唯一） */
    @Excel(name = "标签编码", readConverterExp = "唯=一")
    private String tagCode;

    /** 标签名称 */
    @Excel(name = "标签名称")
    private String tagName;

    /** 标签类型 */
    @Excel(name = "标签类型")
    private String tagType;

    /** 标签描述 */
    @Excel(name = "标签描述")
    private String tagDesc;

    /** 父标签编码 */
    @Excel(name = "父标签编码")
    private String parentCode;

    /** 状态：0-禁用 1-启用 */
    @Excel(name = "状态：0-禁用 1-启用")
    private Long status;

    public void setPlatformTagId(Long platformTagId) 
    {
        this.platformTagId = platformTagId;
    }

    public Long getPlatformTagId() 
    {
        return platformTagId;
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

    public void setTagDesc(String tagDesc) 
    {
        this.tagDesc = tagDesc;
    }

    public String getTagDesc() 
    {
        return tagDesc;
    }

    public void setParentCode(String parentCode) 
    {
        this.parentCode = parentCode;
    }

    public String getParentCode() 
    {
        return parentCode;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("platformTagId", getPlatformTagId())
            .append("tagCode", getTagCode())
            .append("tagName", getTagName())
            .append("tagType", getTagType())
            .append("tagDesc", getTagDesc())
            .append("parentCode", getParentCode())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
