package com.ruoyi.platform.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.platform.domain.UserFeedback;
import org.apache.ibatis.annotations.Param;

/**
 * 用户反馈Mapper接口
 *
 * @author ruoyi
 * @date 2025-11-25
 */
public interface UserFeedbackMapper
{
    /**
     * 查询用户反馈
     *
     * @param feedbackId 用户反馈主键
     * @return 用户反馈
     */
    public UserFeedback selectUserFeedbackById(Long feedbackId);

    /**
     * 查询用户反馈列表
     *
     * @param userFeedback 用户反馈
     * @return 用户反馈集合
     */
    public List<UserFeedback> selectUserFeedbackList(UserFeedback userFeedback);

    /**
     * 根据用户类型和用户ID查询反馈列表
     *
     * @param userType 用户类型
     * @param userId 用户ID
     * @return 用户反馈集合
     */
    public List<UserFeedback> selectUserFeedbackByUser(@Param("userType") Integer userType,
                                                       @Param("userId") Long userId);

    /**
     * 新增用户反馈
     *
     * @param userFeedback 用户反馈
     * @return 结果
     */
    public int insertUserFeedback(UserFeedback userFeedback);

    /**
     * 修改用户反馈
     *
     * @param userFeedback 用户反馈
     * @return 结果
     */
    public int updateUserFeedback(UserFeedback userFeedback);

    /**
     * 删除用户反馈
     *
     * @param feedbackId 用户反馈主键
     * @return 结果
     */
    public int deleteUserFeedbackById(Long feedbackId);

    /**
     * 批量删除用户反馈
     *
     * @param feedbackIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserFeedbackByIds(Long[] feedbackIds);

    /**
     * 根据反馈类型统计
     *
     * @return 统计结果
     */
    public List<Map<String, Object>> statisticsByType();

    /**
     * 根据处理状态统计
     *
     * @return 统计结果
     */
    public List<Map<String, Object>> statisticsByStatus();

    /**
     * 根据用户类型统计
     *
     * @return 统计结果
     */
    public List<Map<String, Object>> statisticsByUserType();
}