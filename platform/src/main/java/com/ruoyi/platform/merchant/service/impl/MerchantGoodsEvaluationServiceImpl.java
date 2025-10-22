package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.platform.merchant.mapper.MerchantGoodsEvaluationMapper;
import com.ruoyi.platform.domain.GoodsEvaluation;
import com.ruoyi.platform.merchant.service.IMerchantGoodsEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantGoodsEvaluationServiceImpl implements IMerchantGoodsEvaluationService {

    @Autowired
    private MerchantGoodsEvaluationMapper merchantGoodsEvaluationMapper;

    @Override
    public List<GoodsEvaluation> getGoodsEvaluationList(Long merchantBaseId, Integer category, Boolean hasImage) {
        return merchantGoodsEvaluationMapper.selectGoodsEvaluationByMerchantAndRating(merchantBaseId, category, hasImage);
    }

    @Override
    public int replyGoodsEvaluation(Long goodsEvaluationId, String merchantReply) {
        return merchantGoodsEvaluationMapper.replyGoodsEvaluation(goodsEvaluationId, merchantReply);
    }
}