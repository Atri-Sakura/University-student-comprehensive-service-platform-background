package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.MerchantEvaluation;
import com.ruoyi.platform.domain.vo.*;

/**
 * 商家评价Service接口
 *
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IMerchantEvaluationService
{
    /**
     * 用户新增评价（带业务逻辑校验 + 图片上传）
     *
     * @param req 评价请求参数
     * @param userBaseId 当前用户ID
     * @return 结果
     */
    public int insertUserEvaluation(MerchantEvaluationAddReq req, Long userBaseId);

    /**
     * 用户修改评价（带权限校验 + 图片处理）
     *
     * @param req 修改请求参数
     * @param userBaseId 当前用户ID
     * @return 结果
     */
    public int updateUserEvaluation(MerchantEvaluationUpdateReq req, Long userBaseId);

    /**
     * 用户删除评价（带权限校验 + 图片删除）
     *
     * @param merchantEvaluationId 评价ID
     * @param userBaseId 当前用户ID
     * @return 结果
     */
    public int deleteUserEvaluation(Long merchantEvaluationId, Long userBaseId);

    /**
     * 用户批量删除评价（带权限校验 + 图片删除）
     *
     * @param merchantEvaluationIds 评价ID数组
     * @param userBaseId 当前用户ID
     * @return 结果
     */
    public int deleteUserEvaluationBatch(Long[] merchantEvaluationIds, Long userBaseId);

    /**
     * 查询商家评价
     */
    public MerchantEvaluation selectMerchantEvaluationByMerchantEvaluationId(Long merchantEvaluationId);

    /**
     * 查询商家评价列表
     */
    public List<MerchantEvaluation> selectMerchantEvaluationList(MerchantEvaluation merchantEvaluation);

    /**
     * 新增商家评价
     */
    public int insertMerchantEvaluation(MerchantEvaluation merchantEvaluation);

    /**
     * 修改商家评价
     */
    public int updateMerchantEvaluation(MerchantEvaluation merchantEvaluation);

    /**
     * 批量删除商家评价
     */
    public int deleteMerchantEvaluationByMerchantEvaluationIds(Long[] merchantEvaluationIds);

    /**
     * 删除商家评价信息
     */
    public int deleteMerchantEvaluationByMerchantEvaluationId(Long merchantEvaluationId);

    /**
     * 商家查询评价列表（带高级筛选）
     *
     * @param req 查询条件
     * @param merchantBaseId 当前商家ID
     * @return 评价详情列表
     */
    List<MerchantEvaluationDetailVO> getMerchantEvaluationList(
            MerchantEvaluationQueryReq req, Long merchantBaseId);

    /**
     * 商家查询评价统计信息
     *
     * @param merchantBaseId 当前商家ID
     * @return 统计信息
     */
    MerchantEvaluationStatisticsVO getEvaluationStatistics(Long merchantBaseId);

    /**
     * 商家回复评价
     *
     * @param req 回复请求
     * @param merchantBaseId 当前商家ID
     * @return 结果
     */
    int replyEvaluation(MerchantEvaluationReplyReq req, Long merchantBaseId);

    /**
     * 商家查询评价详情
     *
     * @param merchantEvaluationId 评价ID
     * @param merchantBaseId 当前商家ID
     * @return 评价详情
     */
    MerchantEvaluationDetailVO getEvaluationDetail(
            Long merchantEvaluationId, Long merchantBaseId);
}