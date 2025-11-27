package com.ruoyi.platform.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com. ruoyi.common.utils. DateUtils;
import com.ruoyi.common.utils. SnowflakeIdWorker;
import com.ruoyi. platform.domain.OrderDelivery;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.dto.RiderEvaluationDTO;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.RiderEvaluationMapper;
import com.ruoyi.platform.domain.RiderEvaluation;
import com.ruoyi.platform.service.IRiderEvaluationService;
import com.ruoyi.platform.domain.vo.RiderEvaluationStatisticsVO;
import org.springframework.transaction.annotation. Transactional;
import com.ruoyi.platform.mapper.OrderMainMapper;
import com.ruoyi.platform.mapper.OrderDeliveryMapper;

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

    @Autowired
    private OrderMainMapper orderMainMapper;

    @Autowired
    private OrderDeliveryMapper orderDeliveryMapper;

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;  // 注入雪花ID生成器

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
     * @param evaluationDTO 骑手评价DTO
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRiderEvaluation(RiderEvaluationDTO evaluationDTO, Long userId)
    {
        // 1.  查询订单信息
        OrderMain orderMain = orderMainMapper.selectOrderMainByOrderMainId(evaluationDTO.getOrderId());
        if (orderMain == null) {
            throw new ServiceException("订单不存在");
        }

        // 2. 验证订单是否属于当前用户
        if (!orderMain.getUserId().equals(userId)) {
            throw new ServiceException("无权评价此订单");
        }

        // 3. 验证订单状态是否已完成
        if (orderMain.getOrderStatus() != 4L) {
            throw new ServiceException("订单未完成,无法评价");
        }

        // 4. 查询配送信息获取骑手ID
        OrderDelivery orderDelivery = orderDeliveryMapper.selectOrderDeliveryByOrderMainId(evaluationDTO.getOrderId());
        if (orderDelivery == null || orderDelivery.getRiderId() == null) {
            throw new ServiceException("订单无骑手信息");
        }

        // 5. 检查是否已经评价过
        RiderEvaluation existEvaluation = riderEvaluationMapper.selectRiderEvaluationByOrderId(evaluationDTO.getOrderId());
        if (existEvaluation != null) {
            throw new ServiceException("该订单已评价过");
        }

        // 6.  构建评价对象
        RiderEvaluation riderEvaluation = new RiderEvaluation();
        riderEvaluation.setRiderEvaluationId(snowflakeIdWorker.nextId());  // 使用 nextId() 方法
        riderEvaluation.setRiderBaseId(orderDelivery.getRiderId());
        riderEvaluation.setUserId(userId);
        riderEvaluation.setOrderId(evaluationDTO.getOrderId());
        riderEvaluation.setRating(evaluationDTO.getRating(). longValue());
        riderEvaluation.setContent(evaluationDTO.getContent());
        riderEvaluation.setCreateTime(DateUtils.getNowDate());

        return riderEvaluationMapper. insertRiderEvaluation(riderEvaluation);
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

    /**
     * 根据订单ID查询评价
     *
     * @param orderId 订单ID
     * @return 骑手评价
     */
    @Override
    public RiderEvaluation selectRiderEvaluationByOrderId(Long orderId)
    {
        return riderEvaluationMapper. selectRiderEvaluationByOrderId(orderId);
    }

    /**
     * 查询骑手评价统计信息
     *
     * @param riderBaseId 骑手ID
     * @return 统计信息
     */
    @Override
    public RiderEvaluationStatisticsVO selectRiderEvaluationStatistics(Long riderBaseId)
    {
        return riderEvaluationMapper. selectRiderEvaluationStatistics(riderBaseId);
    }

    /**
     * 根据评分筛选骑手评价列表
     *
     * @param riderBaseId 骑手ID
     * @param filterType 筛选类型(null-全部, 1-好评4-5星, 2-中评2-3星, 3-差评1星)
     * @return 评价列表
     */
    @Override
    public List<RiderEvaluation> selectRiderEvaluationListByRating(Long riderBaseId, Integer filterType)
    {
        Integer minRating = null;
        Integer maxRating = null;

        // 根据筛选类型转换为评分范围
        if (filterType != null) {
            switch (filterType) {
                case 1:  // 好评: 4-5星
                    minRating = 4;
                    maxRating = 5;
                    break;
                case 2:  // 中评: 2-3星
                    minRating = 2;
                    maxRating = 3;
                    break;
                case 3:  // 差评: 1星
                    minRating = 1;
                    maxRating = 1;
                    break;
                default:
                    // 全部，不设置范围
                    break;
            }
        }

        return riderEvaluationMapper.selectRiderEvaluationListByRating(riderBaseId, minRating, maxRating);
    }
}