package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品图片关联（支持多图展示）对象 merchant_goods_image
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class MerchantGoodsImage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图片ID */
    private Long merchantGoodsImageId;

    private Long merchantBaseId;

    /** 关联商品ID */
    @Excel(name = "关联商品ID")
    private Long merchantGoodsId;

    /** 图片URL */
    @Excel(name = "图片URL")
    private String imageUrl;

    /** 图片描述（如"商品正面图"） */
    @Excel(name = "图片描述", readConverterExp = "如=商品正面图")
    private String imageDesc;

    /** 排序序号（值越小越靠前） */
    @Excel(name = "排序序号", readConverterExp = "值=越小越靠前")
    private Long sortOrder;

    /** 是否主图：0-否 1-是 */
    @Excel(name = "是否主图：0-否 1-是")
    private Long isMain;

    public void setMerchantGoodsImageId(Long merchantGoodsImageId) 
    {
        this.merchantGoodsImageId = merchantGoodsImageId;
    }

    public Long getMerchantBaseId() {
        return merchantBaseId;
    }

    public void setMerchantBaseId(Long merchantBaseId) {
        this.merchantBaseId = merchantBaseId;
    }

    public Long getMerchantGoodsImageId() 
    {
        return merchantGoodsImageId;
    }

    public void setMerchantGoodsId(Long merchantGoodsId) 
    {
        this.merchantGoodsId = merchantGoodsId;
    }

    public Long getMerchantGoodsId() 
    {
        return merchantGoodsId;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setImageDesc(String imageDesc) 
    {
        this.imageDesc = imageDesc;
    }

    public String getImageDesc() 
    {
        return imageDesc;
    }

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    public void setIsMain(Long isMain) 
    {
        this.isMain = isMain;
    }

    public Long getIsMain() 
    {
        return isMain;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("merchantGoodsImageId", getMerchantGoodsImageId())
            .append("merchantGoodsId", getMerchantGoodsId())
            .append("imageUrl", getImageUrl())
            .append("imageDesc", getImageDesc())
            .append("sortOrder", getSortOrder())
            .append("isMain", getIsMain())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
