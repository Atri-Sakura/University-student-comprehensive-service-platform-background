package com.ruoyi.platform.merchant.vo;


import lombok.Data;

/**
 * 商家仪表盘综合VO
 */
@Data
public class MerchantDashboardVO {
    /**
     * 订单统计
     */
    private OrderStatsVO orderStats;

    /**
     * 评价分析
     */
    private EvaluationAnalysisVO evaluationAnalysis;

    /**
     * 商品排行
     */
    private ProductRankingVO productRanking;

    // 构造函数
    public MerchantDashboardVO() {}

    public MerchantDashboardVO(OrderStatsVO orderStats, EvaluationAnalysisVO evaluationAnalysis, ProductRankingVO productRanking) {
        this.orderStats = orderStats;
        this.evaluationAnalysis = evaluationAnalysis;
        this.productRanking = productRanking;
    }
}