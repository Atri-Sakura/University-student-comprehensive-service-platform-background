package com.ruoyi. platform.mapper;

import java. util.List;
import com. ruoyi.platform.domain. RiderEvaluation;
import com.ruoyi.platform. domain.vo.RiderEvaluationStatisticsVO;
import org.apache.ibatis. annotations.Param;

/**
 * 骑手评价Mapper接口
 *
 * @author ruoyi
 * @date 2025-11-26
 */
public interface RiderEvaluationMapper
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
     * 删除骑手评价
     *
     * @param riderEvaluationId 骑手评价主键
     * @return 结果
     */
    public int deleteRiderEvaluationByRiderEvaluationId(Long riderEvaluationId);

    /**
     * 批量删除骑手评价
     *
     * @param riderEvaluationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRiderEvaluationByRiderEvaluationIds(Long[] riderEvaluationIds);

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
     * @param minRating 最小评分
     * @param maxRating 最大评分
     * @return 评价列表
     */
    public List<RiderEvaluation> selectRiderEvaluationListByRating(@Param("riderBaseId") Long riderBaseId,
                                                                   @Param("minRating") Integer minRating,
                                                                   @Param("maxRating") Integer maxRating);
}