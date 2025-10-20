package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.UserBehaviorLog;

/**
 * 用户行为记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface UserBehaviorLogMapper 
{
    /**
     * 查询用户行为记录
     * 
     * @param userBehaviorLogId 用户行为记录主键
     * @return 用户行为记录
     */
    public UserBehaviorLog selectUserBehaviorLogByUserBehaviorLogId(Long userBehaviorLogId);

    /**
     * 查询用户行为记录列表
     * 
     * @param userBehaviorLog 用户行为记录
     * @return 用户行为记录集合
     */
    public List<UserBehaviorLog> selectUserBehaviorLogList(UserBehaviorLog userBehaviorLog);

    /**
     * 新增用户行为记录
     * 
     * @param userBehaviorLog 用户行为记录
     * @return 结果
     */
    public int insertUserBehaviorLog(UserBehaviorLog userBehaviorLog);

    /**
     * 修改用户行为记录
     * 
     * @param userBehaviorLog 用户行为记录
     * @return 结果
     */
    public int updateUserBehaviorLog(UserBehaviorLog userBehaviorLog);

    /**
     * 删除用户行为记录
     * 
     * @param userBehaviorLogId 用户行为记录主键
     * @return 结果
     */
    public int deleteUserBehaviorLogByUserBehaviorLogId(Long userBehaviorLogId);

    /**
     * 批量删除用户行为记录
     * 
     * @param userBehaviorLogIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserBehaviorLogByUserBehaviorLogIds(Long[] userBehaviorLogIds);
}
