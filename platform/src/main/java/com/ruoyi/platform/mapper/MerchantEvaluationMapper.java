package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.MerchantEvaluation;

/**
 * 商家评价Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface MerchantEvaluationMapper 
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
     * 删除商家评价
     * 
     * @param merchantEvaluationId 商家评价主键
     * @return 结果
     */
    public int deleteMerchantEvaluationByMerchantEvaluationId(Long merchantEvaluationId);

    /**
     * 批量删除商家评价
     * 
     * @param merchantEvaluationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMerchantEvaluationByMerchantEvaluationIds(Long[] merchantEvaluationIds);
}
