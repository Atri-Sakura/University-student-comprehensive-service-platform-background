package com.ruoyi.platform.merchant.mapper;

import java.util.List;
import com.ruoyi.platform.domain.GoodsEvaluation;

/**
 * 商家端商品评价Mapper
 */
public interface MerchantGoodsEvaluationMapper {

    /**
     * 查询商家门店下的订单评价列表（可按星级筛选）
     * @param merchantBaseId 商家ID
     * @param rating 星级（为null查全部，5查五星，4查四星，1-3查三星及以下）
     * @return 评价列表
     */
    List<GoodsEvaluation> selectGoodsEvaluationByMerchantAndRating(Long merchantBaseId, Integer rating);

    /**
     * 商家回复订单评价
     * @param goodsEvaluationId 评价ID
     * @param merchantReply 回复内容
     * @return 影响行数
     */
    int replyGoodsEvaluation(Long goodsEvaluationId, String merchantReply);
}