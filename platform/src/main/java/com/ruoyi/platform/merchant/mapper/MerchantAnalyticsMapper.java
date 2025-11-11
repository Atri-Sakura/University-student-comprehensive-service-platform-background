package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.merchant.vo.EvaluationAnalysisVO;
import com.ruoyi.platform.merchant.vo.OrderStatsVO;
import com.ruoyi.platform.merchant.vo.ProductSalesVO;
import com.ruoyi.platform.merchant.vo.RatingDistributionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MerchantAnalyticsMapper {
    /**
     * 获取今日销售数据
     */
    OrderStatsVO selectTodaySalesData(Long merchantBaseId);

    /**
     * 获取昨日销售数据（用于计算百分比变化）
     */
    OrderStatsVO selectYesterdaySalesData(Long merchantBaseId);

    /**
     * 获取评价统计数据
     */
    EvaluationAnalysisVO selectEvaluationStats(Long merchantBaseId);

    /**
     * 获取评分分布数据
     */
    List<RatingDistributionVO> selectRatingDistribution(Long merchantBaseId);

    /**
     * 获取热销商品数据
     */
    List<ProductSalesVO> selectHotSellingProducts(Long merchantBaseId);

    /**
     * 获取滞销商品数据
     */
    List<ProductSalesVO> selectSlowMovingProducts(Long merchantBaseId);

    /**
     * 查询正面评价内容（rating >=4）
     * @param limit 限制查询数量（避免数据量过大）
     */
    List<String> selectPositiveContents(int limit, Long merchantBaseId);

    /**
     * 查询负面评价内容（rating <=2）
     */
    List<String> selectNegativeContents(int limit, Long merchantBaseId);


}
