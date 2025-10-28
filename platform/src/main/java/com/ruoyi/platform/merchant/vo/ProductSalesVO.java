package com.ruoyi.platform.merchant.vo;


import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品销售VO
 */
@Data
public class ProductSalesVO {
    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 销售数量
     */
    private Integer salesCount;

    /**
     * 销售总额
     */
    private BigDecimal totalRevenue;

    /**
     * 排名
     */
    private Integer rank;

    // 构造函数
    public ProductSalesVO() {}

    public ProductSalesVO(String goodsName, BigDecimal price, Integer salesCount) {
        this.goodsName = goodsName;
        this.price = price;
        this.salesCount = salesCount;
    }
}