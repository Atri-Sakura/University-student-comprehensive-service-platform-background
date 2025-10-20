package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 二手商品(简化版)对象 secondhand_goods
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class SecondhandGoods extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 二手商品唯一ID */
    private Long secondhandGoodsId;

    /** 发布用户ID(关联user_base.user_base_id) */
    @Excel(name = "发布用户ID(关联user_base.user_base_id)")
    private Long userBaseId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String goodsName;

    /** 商品分类(如数码产品/图书教材/服饰鞋包/生活用品/运动健身/美妆个护) */
    @Excel(name = "商品分类(如数码产品/图书教材/服饰鞋包/生活用品/运动健身/美妆个护)")
    private String category;

    /** 售价/估价 */
    @Excel(name = "售价/估价")
    private BigDecimal price;

    /** 商品描述(详细说明) */
    @Excel(name = "商品描述(详细说明)")
    private String description;

    /** 商品状态:0-已下架 1-在售中 2-已售出 3-已预定 */
    @Excel(name = "商品状态:0-已下架 1-在售中 2-已售出 3-已预定")
    private Long status;

    /** 浏览次数 */
    @Excel(name = "浏览次数")
    private Long viewCount;

    /** 收藏次数 */
    @Excel(name = "收藏次数")
    private Long favoriteCount;

    /** 分享次数 */
    @Excel(name = "分享次数")
    private Long shareCount;

    /** 售出时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "售出时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date soldTime;

    public void setSecondhandGoodsId(Long secondhandGoodsId) 
    {
        this.secondhandGoodsId = secondhandGoodsId;
    }

    public Long getSecondhandGoodsId() 
    {
        return secondhandGoodsId;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
    }

    public void setGoodsName(String goodsName) 
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName() 
    {
        return goodsName;
    }

    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setViewCount(Long viewCount) 
    {
        this.viewCount = viewCount;
    }

    public Long getViewCount() 
    {
        return viewCount;
    }

    public void setFavoriteCount(Long favoriteCount) 
    {
        this.favoriteCount = favoriteCount;
    }

    public Long getFavoriteCount() 
    {
        return favoriteCount;
    }

    public void setShareCount(Long shareCount) 
    {
        this.shareCount = shareCount;
    }

    public Long getShareCount() 
    {
        return shareCount;
    }

    public void setSoldTime(Date soldTime) 
    {
        this.soldTime = soldTime;
    }

    public Date getSoldTime() 
    {
        return soldTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("secondhandGoodsId", getSecondhandGoodsId())
            .append("userBaseId", getUserBaseId())
            .append("goodsName", getGoodsName())
            .append("category", getCategory())
            .append("price", getPrice())
            .append("description", getDescription())
            .append("status", getStatus())
            .append("viewCount", getViewCount())
            .append("favoriteCount", getFavoriteCount())
            .append("shareCount", getShareCount())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("soldTime", getSoldTime())
            .toString();
    }
}
