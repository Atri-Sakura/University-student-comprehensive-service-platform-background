package com.ruoyi.platform.merchant.service;

import java.util.List;
import com.ruoyi.platform.domain.GoodsEvaluation;

public interface IMerchantGoodsEvaluationService {
    List<GoodsEvaluation> getGoodsEvaluationList(Long merchantBaseId, Integer rating);

    int replyGoodsEvaluation(Long goodsEvaluationId, String merchantReply);
}