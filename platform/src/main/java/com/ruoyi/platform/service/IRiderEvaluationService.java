package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.RiderEvaluation;
import com.ruoyi.platform.domain.dto.RiderEvaluationDTO;
import com.ruoyi.platform.domain.vo.RiderEvaluationStatisticsVO;

/**
 * 骑手评价Service接口
 *
 * @author ruoyi
 * @date 2025-10-20
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
     * @param evaluationDTO 骑手评价DTO
     * @param userId 用户ID
     * @return 结果
     */
    public int insertRiderEvaluation(RiderEvaluationDTO evaluationDTO, Long userId);

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

    /**
     * 根据订单ID查询评价
     *
     * @param orderId 订单ID
     * @return 骑手评价
     */
    public RiderEvaluation selectRiderEvaluationByOrderId(Long orderId);

    /**
     * 查询骑手评价统计信息
     *
     * @param riderBaseId 骑手ID
     * @return 统计信息
     */
    public RiderEvaluationStatisticsVO selectRiderEvaluationStatistics(Long riderBaseId);

    /**
     * 根据评分筛选骑手评价列表
     *
     * @param riderBaseId 骑手ID
     * @param filterType 筛选类型(null-全部, 1-好评4-5星, 2-中评2-3星, 3-差评1星)
     * @return 评价列表
     */
    public List<RiderEvaluation> selectRiderEvaluationListByRating(Long riderBaseId, Integer filterType);
}