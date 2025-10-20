package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.MerchantEvaluationMapper;
import com.ruoyi.platform.domain.MerchantEvaluation;
import com.ruoyi.platform.service.IMerchantEvaluationService;

/**
 * 商家评价Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class MerchantEvaluationServiceImpl implements IMerchantEvaluationService 
{
    @Autowired
    private MerchantEvaluationMapper merchantEvaluationMapper;

    /**
     * 查询商家评价
     * 
     * @param merchantEvaluationId 商家评价主键
     * @return 商家评价
     */
    @Override
    public MerchantEvaluation selectMerchantEvaluationByMerchantEvaluationId(Long merchantEvaluationId)
    {
        return merchantEvaluationMapper.selectMerchantEvaluationByMerchantEvaluationId(merchantEvaluationId);
    }

    /**
     * 查询商家评价列表
     * 
     * @param merchantEvaluation 商家评价
     * @return 商家评价
     */
    @Override
    public List<MerchantEvaluation> selectMerchantEvaluationList(MerchantEvaluation merchantEvaluation)
    {
        return merchantEvaluationMapper.selectMerchantEvaluationList(merchantEvaluation);
    }

    /**
     * 新增商家评价
     * 
     * @param merchantEvaluation 商家评价
     * @return 结果
     */
    @Override
    public int insertMerchantEvaluation(MerchantEvaluation merchantEvaluation)
    {
        merchantEvaluation.setCreateTime(DateUtils.getNowDate());
        return merchantEvaluationMapper.insertMerchantEvaluation(merchantEvaluation);
    }

    /**
     * 修改商家评价
     * 
     * @param merchantEvaluation 商家评价
     * @return 结果
     */
    @Override
    public int updateMerchantEvaluation(MerchantEvaluation merchantEvaluation)
    {
        return merchantEvaluationMapper.updateMerchantEvaluation(merchantEvaluation);
    }

    /**
     * 批量删除商家评价
     * 
     * @param merchantEvaluationIds 需要删除的商家评价主键
     * @return 结果
     */
    @Override
    public int deleteMerchantEvaluationByMerchantEvaluationIds(Long[] merchantEvaluationIds)
    {
        return merchantEvaluationMapper.deleteMerchantEvaluationByMerchantEvaluationIds(merchantEvaluationIds);
    }

    /**
     * 删除商家评价信息
     * 
     * @param merchantEvaluationId 商家评价主键
     * @return 结果
     */
    @Override
    public int deleteMerchantEvaluationByMerchantEvaluationId(Long merchantEvaluationId)
    {
        return merchantEvaluationMapper.deleteMerchantEvaluationByMerchantEvaluationId(merchantEvaluationId);
    }
}
