package com.ruoyi.platform.merchant.vo;


import lombok.Data;

import java.util.List;

/**
 * 商品排行VO
 */
@Data
public class ProductRankingVO {
    /**
     * 热销商品列表
     */
    private List<ProductSalesVO> hotSellingProducts;

    /**
     * 滞销商品列表
     */
    private List<ProductSalesVO> slowMovingProducts;

    // 构造函数
    public ProductRankingVO() {}

    public ProductRankingVO(List<ProductSalesVO> hotSellingProducts, List<ProductSalesVO> slowMovingProducts) {
        this.hotSellingProducts = hotSellingProducts;
        this.slowMovingProducts = slowMovingProducts;
    }
}