package com.ruoyi.platform.merchant.vo;


import lombok.Data;
import java.math.BigDecimal;

/**
 * 评分分布VO
 */
@Data
public class RatingDistributionVO {
    /**
     * 评分等级 (1-5星)
     */
    private Integer ratingLevel;

    /**
     * 评价数量
     */
    private Integer reviewCount;

    /**
     * 占比百分比
     */
    private BigDecimal percentage;

    // 构造函数
    public RatingDistributionVO() {}

    public RatingDistributionVO(Integer ratingLevel, Integer reviewCount, BigDecimal percentage) {
        this.ratingLevel = ratingLevel;
        this.reviewCount = reviewCount;
        this.percentage = percentage;
    }
}