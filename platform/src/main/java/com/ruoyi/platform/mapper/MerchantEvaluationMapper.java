package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.MerchantEvaluation;
import com.ruoyi.platform.domain.vo.MerchantEvaluationQueryReq;
import com.ruoyi.platform.domain.vo.MerchantEvaluationStatisticsVO;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 商家评价Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
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

    /**
     * 商家查询评价列表（带高级筛选）
     *
     * @param merchantBaseId 商家ID
     * @param req 查询条件
     * @return 评价列表
     */
    List<MerchantEvaluation> selectMerchantEvaluationListByMerchant(
            @Param("merchantBaseId") Long merchantBaseId,
            @Param("req") MerchantEvaluationQueryReq req);

    /**
     * 商家查询评价统计信息
     *
     * @param merchantBaseId 商家ID
     * @return 统计信息
     */
    MerchantEvaluationStatisticsVO selectEvaluationStatistics(@Param("merchantBaseId") Long merchantBaseId);
}
