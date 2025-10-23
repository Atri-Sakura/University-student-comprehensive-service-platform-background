package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.RiderEvaluationMapper;
import com.ruoyi.platform.domain.RiderEvaluation;
import com.ruoyi.platform.service.IRiderEvaluationService;

/**
 * 骑手评价Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class RiderEvaluationServiceImpl implements IRiderEvaluationService 
{
    @Autowired
    private RiderEvaluationMapper riderEvaluationMapper;

    /**
     * 查询骑手评价
     * 
     * @param riderEvaluationId 骑手评价主键
     * @return 骑手评价
     */
    @Override
    public RiderEvaluation selectRiderEvaluationByRiderEvaluationId(Long riderEvaluationId)
    {
        return riderEvaluationMapper.selectRiderEvaluationByRiderEvaluationId(riderEvaluationId);
    }

    /**
     * 查询骑手评价列表
     * 
     * @param riderEvaluation 骑手评价
     * @return 骑手评价
     */
    @Override
    public List<RiderEvaluation> selectRiderEvaluationList(RiderEvaluation riderEvaluation)
    {
        return riderEvaluationMapper.selectRiderEvaluationList(riderEvaluation);
    }

    /**
     * 新增骑手评价
     * 
     * @param riderEvaluation 骑手评价
     * @return 结果
     */
    @Override
    public int insertRiderEvaluation(RiderEvaluation riderEvaluation)
    {
        riderEvaluation.setCreateTime(DateUtils.getNowDate());
        return riderEvaluationMapper.insertRiderEvaluation(riderEvaluation);
    }

    /**
     * 修改骑手评价
     * 
     * @param riderEvaluation 骑手评价
     * @return 结果
     */
    @Override
    public int updateRiderEvaluation(RiderEvaluation riderEvaluation)
    {
        return riderEvaluationMapper.updateRiderEvaluation(riderEvaluation);
    }

    /**
     * 批量删除骑手评价
     * 
     * @param riderEvaluationIds 需要删除的骑手评价主键
     * @return 结果
     */
    @Override
    public int deleteRiderEvaluationByRiderEvaluationIds(Long[] riderEvaluationIds)
    {
        return riderEvaluationMapper.deleteRiderEvaluationByRiderEvaluationIds(riderEvaluationIds);
    }

    /**
     * 删除骑手评价信息
     * 
     * @param riderEvaluationId 骑手评价主键
     * @return 结果
     */
    @Override
    public int deleteRiderEvaluationByRiderEvaluationId(Long riderEvaluationId)
    {
        return riderEvaluationMapper.deleteRiderEvaluationByRiderEvaluationId(riderEvaluationId);
    }
}
