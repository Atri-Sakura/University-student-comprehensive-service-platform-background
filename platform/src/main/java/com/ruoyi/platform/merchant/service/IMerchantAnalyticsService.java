package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.merchant.vo.EvaluationAnalysisVO;
import com.ruoyi.platform.merchant.vo.OrderStatsVO;
import com.ruoyi.platform.merchant.vo.ProductRankingVO;

public interface IMerchantAnalyticsService {
    /**
     * 获取销售数据（订单量、营业额等）
     */
    OrderStatsVO getSalesData(Long merchantBaseId);

    /**
     * 获取评价数据
     */
    EvaluationAnalysisVO getRatingsData(Long merchantBaseId);

    /**
     * 获取热销商品排行榜
     */
    ProductRankingVO getTopGoods(Long merchantBaseId);
}
