package com.ruoyi.platform.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品数据传输对象
 */
@Data
public class MerchantGoodsDTO {

    /** 商品唯一ID */
    private Long merchantGoodsId;

    /** 所属商家ID */
    private Long merchantBaseId;

    /** 商品名称 */
    private String goodsName;

    /** 商品分类 */
    private String category;

    /** 商品子分类 */
    private String subCategory;

    /** 单价 */
    private BigDecimal price;

    /** 原价 */
    private BigDecimal originalPrice;

    /** 库存 */
    private Integer stock;

    /** 销量 */
    private Long salesCount;

    /** 商品描述 */
    private String description;

    /** 商品标签编码 */
    private String tagCodes;

    /** 状态：0-下架 1-上架 */
    private Integer status;

    /** 商品平均评分 */
    private BigDecimal avgRating;

    /** 评分总次数 */
    private Integer ratingCount;

    /** 五星好评率(%) */
    private BigDecimal fiveStarRate;

    /** 四星好评率(%) */
    private BigDecimal fourStarRate;

    /** 三星评价率(%) */
    private BigDecimal threeStarRate;

    /** 二星评价率(%) */
    private BigDecimal twoStarRate;

    /** 一星差评率(%) */
    private BigDecimal oneStarRate;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 商品图片 */
    private String mainImageUrl;

    // 构造方法
    public MerchantGoodsDTO() {
    }

    // 从实体类转换的构造方法
    public MerchantGoodsDTO(com.ruoyi.platform.domain.MerchantGoods goods) {
        if (goods != null) {
            this.merchantGoodsId = goods.getMerchantGoodsId();
            this.merchantBaseId = goods.getMerchantBaseId();
            this.goodsName = goods.getGoodsName();
            this.category = goods.getCategory();
            this.subCategory = goods.getSubCategory();
            this.price = goods.getPrice();
            this.originalPrice = goods.getOriginalPrice();
            this.stock = goods.getStock() != null ? goods.getStock().intValue() : 0;
            this.salesCount = goods.getSalesCount();
            this.description = goods.getDescription();
            this.tagCodes = goods.getTagCodes();
            this.status = goods.getStatus() != null ? goods.getStatus().intValue() : 1;
            this.avgRating = goods.getAvgRating();
            this.ratingCount = goods.getRatingCount() != null ? goods.getRatingCount().intValue() : 0;
            this.fiveStarRate = goods.getFiveStarRate();
            this.fourStarRate = goods.getFourStarRate();
            this.threeStarRate = goods.getThreeStarRate();
            this.twoStarRate = goods.getTwoStarRate();
            this.oneStarRate = goods.getOneStarRate();
            this.createTime = goods.getCreateTime();
            this.updateTime = goods.getUpdateTime();
        }
    }
}