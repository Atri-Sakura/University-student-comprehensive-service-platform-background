package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品对象 merchant_goods
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class MerchantGoods extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品唯一ID */
    private Long merchantGoodsId;

    /** 所属商家ID */
    @Excel(name = "所属商家ID")
    private Long merchantBaseId;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String goodsName;

    /** 商品分类 */
    @Excel(name = "商品分类")
    private String category;

    /** 商品子分类 */
    @Excel(name = "商品子分类")
    private String subCategory;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 原价 */
    @Excel(name = "原价")
    private BigDecimal originalPrice;

    /** 库存 */
    @Excel(name = "库存")
    private Long stock;

    /** 销量 */
    @Excel(name = "销量")
    private Long salesCount;

    /** 商品描述 */
    @Excel(name = "商品描述")
    private String description;

    /** 商品标签编码（逗号分隔，如FOOD_SPICY,FAST_FOOD） */
    @Excel(name = "商品标签编码", readConverterExp = "逗=号分隔，如FOOD_SPICY,FAST_FOOD")
    private String tagCodes;

    /** 状态：0-下架 1-上架 */
    @Excel(name = "状态：0-下架 1-上架")
    private Long status;

    /** 商品平均评分 */
    @Excel(name = "商品平均评分")
    private BigDecimal avgRating;

    /** 评分总次数 */
    @Excel(name = "评分总次数")
    private Long ratingCount;

    /** 五星好评率(%) */
    @Excel(name = "五星好评率(%)")
    private BigDecimal fiveStarRate;

    /** 四星好评率(%) */
    @Excel(name = "四星好评率(%)")
    private BigDecimal fourStarRate;

    /** 三星评价率(%) */
    @Excel(name = "三星评价率(%)")
    private BigDecimal threeStarRate;

    /** 二星评价率(%) */
    @Excel(name = "二星评价率(%)")
    private BigDecimal twoStarRate;

    /** 一星差评率(%) */
    @Excel(name = "一星差评率(%)")
    private BigDecimal oneStarRate;

    public void setMerchantGoodsId(Long merchantGoodsId) 
    {
        this.merchantGoodsId = merchantGoodsId;
    }

    public Long getMerchantGoodsId() 
    {
        return merchantGoodsId;
    }

    public void setMerchantBaseId(Long merchantBaseId) 
    {
        this.merchantBaseId = merchantBaseId;
    }

    public Long getMerchantBaseId() 
    {
        return merchantBaseId;
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

    public void setSubCategory(String subCategory) 
    {
        this.subCategory = subCategory;
    }

    public String getSubCategory() 
    {
        return subCategory;
    }

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setOriginalPrice(BigDecimal originalPrice) 
    {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getOriginalPrice() 
    {
        return originalPrice;
    }

    public void setStock(Long stock) 
    {
        this.stock = stock;
    }

    public Long getStock() 
    {
        return stock;
    }

    public void setSalesCount(Long salesCount) 
    {
        this.salesCount = salesCount;
    }

    public Long getSalesCount() 
    {
        return salesCount;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setTagCodes(String tagCodes) 
    {
        this.tagCodes = tagCodes;
    }

    public String getTagCodes() 
    {
        return tagCodes;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setAvgRating(BigDecimal avgRating) 
    {
        this.avgRating = avgRating;
    }

    public BigDecimal getAvgRating() 
    {
        return avgRating;
    }

    public void setRatingCount(Long ratingCount) 
    {
        this.ratingCount = ratingCount;
    }

    public Long getRatingCount() 
    {
        return ratingCount;
    }

    public void setFiveStarRate(BigDecimal fiveStarRate) 
    {
        this.fiveStarRate = fiveStarRate;
    }

    public BigDecimal getFiveStarRate() 
    {
        return fiveStarRate;
    }

    public void setFourStarRate(BigDecimal fourStarRate) 
    {
        this.fourStarRate = fourStarRate;
    }

    public BigDecimal getFourStarRate() 
    {
        return fourStarRate;
    }

    public void setThreeStarRate(BigDecimal threeStarRate) 
    {
        this.threeStarRate = threeStarRate;
    }

    public BigDecimal getThreeStarRate() 
    {
        return threeStarRate;
    }

    public void setTwoStarRate(BigDecimal twoStarRate) 
    {
        this.twoStarRate = twoStarRate;
    }

    public BigDecimal getTwoStarRate() 
    {
        return twoStarRate;
    }

    public void setOneStarRate(BigDecimal oneStarRate) 
    {
        this.oneStarRate = oneStarRate;
    }

    public BigDecimal getOneStarRate() 
    {
        return oneStarRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("merchantGoodsId", getMerchantGoodsId())
            .append("merchantBaseId", getMerchantBaseId())
            .append("goodsName", getGoodsName())
            .append("category", getCategory())
            .append("subCategory", getSubCategory())
            .append("price", getPrice())
            .append("originalPrice", getOriginalPrice())
            .append("stock", getStock())
            .append("salesCount", getSalesCount())
            .append("description", getDescription())
            .append("tagCodes", getTagCodes())
            .append("status", getStatus())
            .append("avgRating", getAvgRating())
            .append("ratingCount", getRatingCount())
            .append("fiveStarRate", getFiveStarRate())
            .append("fourStarRate", getFourStarRate())
            .append("threeStarRate", getThreeStarRate())
            .append("twoStarRate", getTwoStarRate())
            .append("oneStarRate", getOneStarRate())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
