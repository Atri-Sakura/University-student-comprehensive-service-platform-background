package com.ruoyi.platform.merchant.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 关键词统计摘要
 */
@Data
public class KeywordSummary {
    /**
     * 总正面关键词数量
     */
    private Integer totalPositiveKeywords = 0;

    /**
     * 总负面关键词数量
     */
    private Integer totalNegativeKeywords = 0;

    /**
     * 最常出现正面关键词
     */
    private String topPositiveKeyword;

    /**
     * 最常出现负面关键词
     */
    private String topNegativeKeyword;

    /**
     * 正面关键词总出现次数
     */
    private Integer positiveKeywordTotalCount = 0;

    /**
     * 负面关键词总出现次数
     */
    private Integer negativeKeywordTotalCount = 0;

    /**
     * 关键词覆盖率（包含关键词的评论比例）
     */
    private BigDecimal keywordCoverage = BigDecimal.ZERO;
}
