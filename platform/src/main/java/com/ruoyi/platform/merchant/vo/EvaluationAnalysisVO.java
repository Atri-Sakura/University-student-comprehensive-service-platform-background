package com.ruoyi.platform.merchant.vo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * 评价分析VO
 */
@Data
public class EvaluationAnalysisVO {
    /**
     * 综合评分
     */
    private BigDecimal avgRating;

    /**
     * 评价总数
     */
    private Integer totalReviews;

    /**
     * 好评关键词列表
     */
    private List<String> positiveKeywords;

    /**
     * 差评关键词列表
     */
    private List<String> negativeKeywords;

    /**
     * 评分分布列表
     */
    private List<RatingDistributionVO> ratingDistributions;

    // 构造函数
    public EvaluationAnalysisVO() {
        this.avgRating = BigDecimal.ZERO;
        this.totalReviews = 0;
        this.positiveKeywords = Collections.emptyList();
        this.negativeKeywords = Collections.emptyList();
        this.ratingDistributions = Collections.emptyList();
    }

    public EvaluationAnalysisVO(BigDecimal avgRating, Integer totalReviews) {
        this.avgRating = avgRating;
        this.totalReviews = totalReviews;
    }
}