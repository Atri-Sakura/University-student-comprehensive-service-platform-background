package com.ruoyi.platform.service.impl;

import java.util.List;
import java.util.Map;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserFeedbackMapper;
import com.ruoyi.platform.domain.UserFeedback;
import com.ruoyi.platform.service.IUserFeedbackService;

/**
 * 用户反馈Service业务层处理
 *
 * @author ruoyi
 * @date 2025-11-25
 */
@Service
public class UserFeedbackServiceImpl implements IUserFeedbackService
{
    @Autowired
    private UserFeedbackMapper userFeedbackMapper;

    /**
     * 查询用户反馈
     *
     * @param feedbackId 用户反馈主键
     * @return 用户反馈
     */
    @Override
    public UserFeedback selectUserFeedbackById(Long feedbackId)
    {
        return userFeedbackMapper.selectUserFeedbackById(feedbackId);
    }

    /**
     * 查询用户反馈列表
     *
     * @param userFeedback 用户反馈
     * @return 用户反馈
     */
    @Override
    public List<UserFeedback> selectUserFeedbackList(UserFeedback userFeedback)
    {
        return userFeedbackMapper.selectUserFeedbackList(userFeedback);
    }

    /**
     * 根据用户类型和用户ID查询反馈列表
     *
     * @param userType 用户类型
     * @param userId 用户ID
     * @return 用户反馈集合
     */
    @Override
    public List<UserFeedback> selectUserFeedbackByUser(Integer userType, Long userId)
    {
        return userFeedbackMapper.selectUserFeedbackByUser(userType, userId);
    }

    /**
     * 新增用户反馈
     *
     * @param userFeedback 用户反馈
     * @return 结果
     */
    @Override
    public int insertUserFeedback(UserFeedback userFeedback)
    {
        userFeedback.setCreateTime(DateUtils.getNowDate());
        userFeedback.setStatus(0); // 默认待处理
        return userFeedbackMapper.insertUserFeedback(userFeedback);
    }

    /**
     * 修改用户反馈
     *
     * @param userFeedback 用户反馈
     * @return 结果
     */
    @Override
    public int updateUserFeedback(UserFeedback userFeedback)
    {
        userFeedback.setUpdateTime(DateUtils.getNowDate());
        return userFeedbackMapper.updateUserFeedback(userFeedback);
    }

    /**
     * 批量删除用户反馈
     *
     * @param feedbackIds 需要删除的用户反馈主键
     * @return 结果
     */
    @Override
    public int deleteUserFeedbackByIds(Long[] feedbackIds)
    {
        return userFeedbackMapper.deleteUserFeedbackByIds(feedbackIds);
    }

    /**
     * 删除用户反馈信息
     *
     * @param feedbackId 用户反馈主键
     * @return 结果
     */
    @Override
    public int deleteUserFeedbackById(Long feedbackId)
    {
        return userFeedbackMapper.deleteUserFeedbackById(feedbackId);
    }

    /**
     * 根据反馈类型统计
     *
     * @return 统计结果
     */
    @Override
    public List<Map<String, Object>> statisticsByType()
    {
        return userFeedbackMapper.statisticsByType();
    }

    /**
     * 根据处理状态统计
     *
     * @return 统计结果
     */
    @Override
    public List<Map<String, Object>> statisticsByStatus()
    {
        return userFeedbackMapper.statisticsByStatus();
    }

    /**
     * 根据用户类型统计
     *
     * @return 统计结果
     */
    @Override
    public List<Map<String, Object>> statisticsByUserType()
    {
        return userFeedbackMapper.statisticsByUserType();
    }
}