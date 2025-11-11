package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.platform.merchant.mapper.MerchantAnalyticsMapper;
import com.ruoyi.platform.merchant.service.IMerchantAnalyticsService;
import com.ruoyi.platform.merchant.vo.*;
import com.ruoyi.platform.utils.AdvancedKeywordExtractionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MerchantAnalyticsServiceImpl implements IMerchantAnalyticsService {

    @Autowired
    private MerchantAnalyticsMapper merchantAnalyticsMapper;

    @Autowired
    private AdvancedKeywordExtractionUtils keywordExtractionService;


    @Override
    public OrderStatsVO getSalesData(Long merchantBaseId) {
        // 获取今日数据
        OrderStatsVO todayStats = merchantAnalyticsMapper.selectTodaySalesData(merchantBaseId);
        // 获取昨日数据用于计算变化百分比
        OrderStatsVO yesterdayStats = merchantAnalyticsMapper.selectYesterdaySalesData(merchantBaseId);

        System.out.println(todayStats);
        System.out.println("");
        System.out.println(yesterdayStats);

        // 确保对象不为null
        if (todayStats == null) {
            todayStats = new OrderStatsVO(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
        if (yesterdayStats == null) {
            yesterdayStats = new OrderStatsVO(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }



        // 确保数值字段不为null
        todayStats = ensureOrderStatsNotNull(todayStats);
        yesterdayStats = ensureOrderStatsNotNull(yesterdayStats);

        // 计算百分比变化
        calculatePercentageChanges(todayStats, yesterdayStats);
        todayStats.setStatsDate(LocalDateTime.now());

        return todayStats;
    }

    @Override
    public EvaluationAnalysisVO getRatingsData(Long merchantBaseId) {
        // 获取基础评价统计
        EvaluationAnalysisVO evaluationStats = merchantAnalyticsMapper.selectEvaluationStats(merchantBaseId);

        if (evaluationStats == null) {
            evaluationStats = new EvaluationAnalysisVO(BigDecimal.ZERO, 0);
        }

        // 确保数值字段不为null
        evaluationStats = ensureEvaluationAnalysisNotNull(evaluationStats);

        // 获取评分分布
        List<RatingDistributionVO> ratingDistributions = merchantAnalyticsMapper.selectRatingDistribution(merchantBaseId);
        if (ratingDistributions == null) {
            ratingDistributions = Collections.emptyList();
        }
        evaluationStats.setRatingDistributions(ratingDistributions);

        // 获取正面评价内容限制100个
        List<String> positiveContents = merchantAnalyticsMapper.selectPositiveContents(100, merchantBaseId);
        // 获取负面评价内容限制100个
        List<String> negativeContents = merchantAnalyticsMapper.selectNegativeContents(100, merchantBaseId);

        // 合并所有评论用于关键词提取
        List<String> allContents = new ArrayList<>();
        if (positiveContents != null) {
            allContents.addAll(positiveContents);
        }
        if (negativeContents != null) {
            allContents.addAll(negativeContents);
        }

        // 使用高级关键词提取服务提取关键词及其出现次数
        if (!allContents.isEmpty()) {
            keywordExtractionService.extractKeywordsWithFrequency(evaluationStats, allContents);
        } else {
            // 如果没有评论内容，设置空的关键词映射
            evaluationStats.setPositiveKeywords(Collections.emptyMap());
            evaluationStats.setNegativeKeywords(Collections.emptyMap());
            evaluationStats.setKeywordSummary(new KeywordSummary());
        }

        return evaluationStats;
    }


    @Override
    public ProductRankingVO getTopGoods(Long merchantBaseId) {
        // 获取热销商品
        List<ProductSalesVO> hotSellingProducts = merchantAnalyticsMapper.selectHotSellingProducts(merchantBaseId);
        if (hotSellingProducts == null) {
            hotSellingProducts = Collections.emptyList();
        }

        // 获取滞销商品
        List<ProductSalesVO> slowMovingProducts = merchantAnalyticsMapper.selectSlowMovingProducts(merchantBaseId);
        if (slowMovingProducts == null) {
            slowMovingProducts = Collections.emptyList();
        }

        ProductRankingVO productRanking = new ProductRankingVO();
        productRanking.setHotSellingProducts(hotSellingProducts);
        productRanking.setSlowMovingProducts(slowMovingProducts);

        return productRanking;
    }

    /**
     * 计算百分比变化
     */
    private void calculatePercentageChanges(OrderStatsVO todayStats, OrderStatsVO yesterdayStats) {
        // 确保所有BigDecimal字段不为null
        todayStats = ensureOrderStatsNotNull(todayStats);
        yesterdayStats = ensureOrderStatsNotNull(yesterdayStats);

        // 订单量变化百分比
        if (yesterdayStats.getOrderCount() > 0) {
            BigDecimal orderChange = BigDecimal.valueOf(todayStats.getOrderCount() - yesterdayStats.getOrderCount())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(yesterdayStats.getOrderCount()), 2, RoundingMode.HALF_UP);
            todayStats.setOrderCountChangePercent(orderChange);
        } else {
            todayStats.setOrderCountChangePercent(todayStats.getOrderCount() > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO);
        }

        // 营业额变化百分比
        if (yesterdayStats.getTotalRevenue().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal revenueChange = todayStats.getTotalRevenue().subtract(yesterdayStats.getTotalRevenue())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(yesterdayStats.getTotalRevenue(), 2, RoundingMode.HALF_UP);
            todayStats.setRevenueChangePercent(revenueChange);
        } else {
            todayStats.setRevenueChangePercent(todayStats.getTotalRevenue().compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO);
        }

        // 实收金额变化百分比
        if (yesterdayStats.getActualIncome().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal incomeChange = todayStats.getActualIncome().subtract(yesterdayStats.getActualIncome())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(yesterdayStats.getActualIncome(), 2, RoundingMode.HALF_UP);
            todayStats.setIncomeChangePercent(incomeChange);
        } else {
            todayStats.setIncomeChangePercent(todayStats.getActualIncome().compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO);
        }

        // 单均价变化百分比
        if (yesterdayStats.getAvgOrderAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal avgAmountChange = todayStats.getAvgOrderAmount().subtract(yesterdayStats.getAvgOrderAmount())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(yesterdayStats.getAvgOrderAmount(), 2, RoundingMode.HALF_UP);
            todayStats.setAvgAmountChangePercent(avgAmountChange);
        } else {
            todayStats.setAvgAmountChangePercent(todayStats.getAvgOrderAmount().compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO);
        }
    }

    /**
     * 确保OrderStatsVO中的数值字段不为null
     */
    private OrderStatsVO ensureOrderStatsNotNull(OrderStatsVO stats) {
        if (stats == null) {
            return new OrderStatsVO(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        if (stats.getTotalRevenue() == null) {
            stats.setTotalRevenue(BigDecimal.ZERO);
        }
        if (stats.getActualIncome() == null) {
            stats.setActualIncome(BigDecimal.ZERO);
        }
        if (stats.getAvgOrderAmount() == null) {
            stats.setAvgOrderAmount(BigDecimal.ZERO);
        }
        if (stats.getOrderCountChangePercent() == null) {
            stats.setOrderCountChangePercent(BigDecimal.ZERO);
        }
        if (stats.getRevenueChangePercent() == null) {
            stats.setRevenueChangePercent(BigDecimal.ZERO);
        }
        if (stats.getIncomeChangePercent() == null) {
            stats.setIncomeChangePercent(BigDecimal.ZERO);
        }
        if (stats.getAvgAmountChangePercent() == null) {
            stats.setAvgAmountChangePercent(BigDecimal.ZERO);
        }

        return stats;
    }

    /**
     * 确保EvaluationAnalysisVO中的数值字段不为null
     */
    private EvaluationAnalysisVO ensureEvaluationAnalysisNotNull(EvaluationAnalysisVO evaluation) {
        if (evaluation == null) {
            return new EvaluationAnalysisVO(BigDecimal.ZERO, 0);
        }

        if (evaluation.getAvgRating() == null) {
            evaluation.setAvgRating(BigDecimal.ZERO);
        }
        if (evaluation.getTotalReviews() == null) {
            evaluation.setTotalReviews(0);
        }

        return evaluation;
    }
}