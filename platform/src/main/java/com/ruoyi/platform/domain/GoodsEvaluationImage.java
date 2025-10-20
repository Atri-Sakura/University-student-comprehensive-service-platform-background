package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品评价图片对象 goods_evaluation_image
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class GoodsEvaluationImage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图片ID */
    private Long goodsEvaluationImageId;

    /** 关联评价ID */
    @Excel(name = "关联评价ID")
    private Long goodsEvaluationId;

    /** 图片URL */
    @Excel(name = "图片URL")
    private String imageUrl;

    /** 排序序号 */
    @Excel(name = "排序序号")
    private Long sortOrder;

    public void setGoodsEvaluationImageId(Long goodsEvaluationImageId) 
    {
        this.goodsEvaluationImageId = goodsEvaluationImageId;
    }

    public Long getGoodsEvaluationImageId() 
    {
        return goodsEvaluationImageId;
    }

    public void setGoodsEvaluationId(Long goodsEvaluationId) 
    {
        this.goodsEvaluationId = goodsEvaluationId;
    }

    public Long getGoodsEvaluationId() 
    {
        return goodsEvaluationId;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
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
            .append("goodsEvaluationImageId", getGoodsEvaluationImageId())
            .append("goodsEvaluationId", getGoodsEvaluationId())
            .append("imageUrl", getImageUrl())
            .append("sortOrder", getSortOrder())
            .append("createTime", getCreateTime())
            .toString();
    }
}
