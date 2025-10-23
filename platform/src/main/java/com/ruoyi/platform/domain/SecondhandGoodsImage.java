package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 二手商品图片(支持1-9张图片)对象 secondhand_goods_image
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class SecondhandGoodsImage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图片唯一ID */
    private Long secondhandGoodsImageId;

    /** 关联商品ID */
    @Excel(name = "关联商品ID")
    private Long secondhandGoodsId;

    /** 图片URL(建议使用OSS存储) */
    @Excel(name = "图片URL(建议使用OSS存储)")
    private String imageUrl;

    /** 是否主图:0-否 1-是(每个商品仅一张主图) */
    @Excel(name = "是否主图:0-否 1-是(每个商品仅一张主图)")
    private Long isMain;

    /** 排序序号(升序排列) */
    @Excel(name = "排序序号(升序排列)")
    private Long sortOrder;

    public void setSecondhandGoodsImageId(Long secondhandGoodsImageId) 
    {
        this.secondhandGoodsImageId = secondhandGoodsImageId;
    }

    public Long getSecondhandGoodsImageId() 
    {
        return secondhandGoodsImageId;
    }

    public void setSecondhandGoodsId(Long secondhandGoodsId) 
    {
        this.secondhandGoodsId = secondhandGoodsId;
    }

    public Long getSecondhandGoodsId() 
    {
        return secondhandGoodsId;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setIsMain(Long isMain) 
    {
        this.isMain = isMain;
    }

    public Long getIsMain() 
    {
        return isMain;
    }

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("secondhandGoodsImageId", getSecondhandGoodsImageId())
            .append("secondhandGoodsId", getSecondhandGoodsId())
            .append("imageUrl", getImageUrl())
            .append("isMain", getIsMain())
            .append("sortOrder", getSortOrder())
            .append("createTime", getCreateTime())
            .toString();
    }
}
