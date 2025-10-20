package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.GoodsEvaluationMapper;
import com.ruoyi.platform.domain.GoodsEvaluation;
import com.ruoyi.platform.service.IGoodsEvaluationService;

/**
 * 商品评价Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class GoodsEvaluationServiceImpl implements IGoodsEvaluationService 
{
    @Autowired
    private GoodsEvaluationMapper goodsEvaluationMapper;

    /**
     * 查询商品评价
     * 
     * @param goodsEvaluationId 商品评价主键
     * @return 商品评价
     */
    @Override
    public GoodsEvaluation selectGoodsEvaluationByGoodsEvaluationId(Long goodsEvaluationId)
    {
        return goodsEvaluationMapper.selectGoodsEvaluationByGoodsEvaluationId(goodsEvaluationId);
    }

    /**
     * 查询商品评价列表
     * 
     * @param goodsEvaluation 商品评价
     * @return 商品评价
     */
    @Override
    public List<GoodsEvaluation> selectGoodsEvaluationList(GoodsEvaluation goodsEvaluation)
    {
        return goodsEvaluationMapper.selectGoodsEvaluationList(goodsEvaluation);
    }

    /**
     * 新增商品评价
     * 
     * @param goodsEvaluation 商品评价
     * @return 结果
     */
    @Override
    public int insertGoodsEvaluation(GoodsEvaluation goodsEvaluation)
    {
        goodsEvaluation.setCreateTime(DateUtils.getNowDate());
        return goodsEvaluationMapper.insertGoodsEvaluation(goodsEvaluation);
    }

    /**
     * 修改商品评价
     * 
     * @param goodsEvaluation 商品评价
     * @return 结果
     */
    @Override
    public int updateGoodsEvaluation(GoodsEvaluation goodsEvaluation)
    {
        return goodsEvaluationMapper.updateGoodsEvaluation(goodsEvaluation);
    }

    /**
     * 批量删除商品评价
     * 
     * @param goodsEvaluationIds 需要删除的商品评价主键
     * @return 结果
     */
    @Override
    public int deleteGoodsEvaluationByGoodsEvaluationIds(Long[] goodsEvaluationIds)
    {
        return goodsEvaluationMapper.deleteGoodsEvaluationByGoodsEvaluationIds(goodsEvaluationIds);
    }

    /**
     * 删除商品评价信息
     * 
     * @param goodsEvaluationId 商品评价主键
     * @return 结果
     */
    @Override
    public int deleteGoodsEvaluationByGoodsEvaluationId(Long goodsEvaluationId)
    {
        return goodsEvaluationMapper.deleteGoodsEvaluationByGoodsEvaluationId(goodsEvaluationId);
    }
}
