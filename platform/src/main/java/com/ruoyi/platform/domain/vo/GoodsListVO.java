package com.ruoyi.platform.domain.vo;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 用户端商品列表视图对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoodsListVO {

    /** 商品ID */
    private Long merchantGoodsId;

    /** 商品名称 */
    private String goodsName;

    /** 商品分类 */
    private String category;

    /** 单价 */
    private BigDecimal price;

    /** 原价 */
    private BigDecimal originalPrice;

    /** 库存 */
    private Long stock;

    /** 销量 */
    private Long salesCount;

    /** 商品描述 */
    private String description;

    /** 平均评分 */
    private BigDecimal avgRating;

    /** 主图URL */
    private String mainImageUrl;

    // Getters and Setters
    public Long getMerchantGoodsId() {
        return merchantGoodsId;
    }

    public void setMerchantGoodsId(Long merchantGoodsId) {
        this.merchantGoodsId = merchantGoodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Long salesCount) {
        this.salesCount = salesCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(BigDecimal avgRating) {
        this.avgRating = avgRating;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }
}