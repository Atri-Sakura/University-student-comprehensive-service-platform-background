package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.MerchantEvaluation;

/**
 * 商家评价Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IMerchantEvaluationService 
{
    /**
     * 查询商家评价
     * 
     * @param merchantEvaluationId 商家评价主键
     * @return 商家评价
     */
    public MerchantEvaluation selectMerchantEvaluationByMerchantEvaluationId(Long merchantEvaluationId);

    /**
     * 查询商家评价列表
     * 
     * @param merchantEvaluation 商家评价
     * @return 商家评价集合
     */
    public List<MerchantEvaluation> selectMerchantEvaluationList(MerchantEvaluation merchantEvaluation);

    /**
     * 新增商家评价
     * 
     * @param merchantEvaluation 商家评价
     * @return 结果
     */
    public int insertMerchantEvaluation(MerchantEvaluation merchantEvaluation);

    /**
     * 修改商家评价
     * 
     * @param merchantEvaluation 商家评价
     * @return 结果
     */
    public int updateMerchantEvaluation(MerchantEvaluation merchantEvaluation);

    /**
     * 批量删除商家评价
     * 
     * @param merchantEvaluationIds 需要删除的商家评价主键集合
     * @return 结果
     */
    public int deleteMerchantEvaluationByMerchantEvaluationIds(Long[] merchantEvaluationIds);

    /**
     * 删除商家评价信息
     * 
     * @param merchantEvaluationId 商家评价主键
     * @return 结果
     */
    public int deleteMerchantEvaluationByMerchantEvaluationId(Long merchantEvaluationId);
}
