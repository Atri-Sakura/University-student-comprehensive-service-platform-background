package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.RiderEvaluation;

/**
 * 骑手评价Service接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IRiderEvaluationService 
{
    /**
     * 查询骑手评价
     * 
     * @param riderEvaluationId 骑手评价主键
     * @return 骑手评价
     */
    public RiderEvaluation selectRiderEvaluationByRiderEvaluationId(Long riderEvaluationId);

    /**
     * 查询骑手评价列表
     * 
     * @param riderEvaluation 骑手评价
     * @return 骑手评价集合
     */
    public List<RiderEvaluation> selectRiderEvaluationList(RiderEvaluation riderEvaluation);

    /**
     * 新增骑手评价
     * 
     * @param riderEvaluation 骑手评价
     * @return 结果
     */
    public int insertRiderEvaluation(RiderEvaluation riderEvaluation);

    /**
     * 修改骑手评价
     * 
     * @param riderEvaluation 骑手评价
     * @return 结果
     */
    public int updateRiderEvaluation(RiderEvaluation riderEvaluation);

    /**
     * 批量删除骑手评价
     * 
     * @param riderEvaluationIds 需要删除的骑手评价主键集合
     * @return 结果
     */
    public int deleteRiderEvaluationByRiderEvaluationIds(Long[] riderEvaluationIds);

    /**
     * 删除骑手评价信息
     * 
     * @param riderEvaluationId 骑手评价主键
     * @return 结果
     */
    public int deleteRiderEvaluationByRiderEvaluationId(Long riderEvaluationId);
}
