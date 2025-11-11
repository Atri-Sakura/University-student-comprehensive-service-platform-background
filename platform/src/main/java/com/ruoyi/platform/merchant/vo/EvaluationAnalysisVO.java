package com.ruoyi.platform.merchant.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
     * 好评关键词及其出现次数
     */
    private Map<String, Integer> positiveKeywords;

    /**
     * 差评关键词及其出现次数
     */
    private Map<String, Integer> negativeKeywords;

    /**
     * 评分分布列表
     */
    private List<RatingDistributionVO> ratingDistributions;

    /**
     * 关键词统计摘要
     */
    private KeywordSummary keywordSummary;

    // 构造函数
    public EvaluationAnalysisVO() {
        this.avgRating = BigDecimal.ZERO;
        this.totalReviews = 0;
        this.positiveKeywords = Collections.emptyMap();
        this.negativeKeywords = Collections.emptyMap();
        this.ratingDistributions = Collections.emptyList();
        this.keywordSummary = new KeywordSummary();
    }

    public EvaluationAnalysisVO(BigDecimal avgRating, Integer totalReviews) {
        this();
        this.avgRating = avgRating;
        this.totalReviews = totalReviews;
    }
}

