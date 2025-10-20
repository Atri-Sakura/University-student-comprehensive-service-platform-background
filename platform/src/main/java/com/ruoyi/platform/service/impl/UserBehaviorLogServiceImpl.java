package com.ruoyi.platform.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserBehaviorLogMapper;
import com.ruoyi.platform.domain.UserBehaviorLog;
import com.ruoyi.platform.service.IUserBehaviorLogService;

/**
 * 用户行为记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class UserBehaviorLogServiceImpl implements IUserBehaviorLogService 
{
    @Autowired
    private UserBehaviorLogMapper userBehaviorLogMapper;

    /**
     * 查询用户行为记录
     * 
     * @param userBehaviorLogId 用户行为记录主键
     * @return 用户行为记录
     */
    @Override
    public UserBehaviorLog selectUserBehaviorLogByUserBehaviorLogId(Long userBehaviorLogId)
    {
        return userBehaviorLogMapper.selectUserBehaviorLogByUserBehaviorLogId(userBehaviorLogId);
    }

    /**
     * 查询用户行为记录列表
     * 
     * @param userBehaviorLog 用户行为记录
     * @return 用户行为记录
     */
    @Override
    public List<UserBehaviorLog> selectUserBehaviorLogList(UserBehaviorLog userBehaviorLog)
    {
        return userBehaviorLogMapper.selectUserBehaviorLogList(userBehaviorLog);
    }

    /**
     * 新增用户行为记录
     * 
     * @param userBehaviorLog 用户行为记录
     * @return 结果
     */
    @Override
    public int insertUserBehaviorLog(UserBehaviorLog userBehaviorLog)
    {
        return userBehaviorLogMapper.insertUserBehaviorLog(userBehaviorLog);
    }

    /**
     * 修改用户行为记录
     * 
     * @param userBehaviorLog 用户行为记录
     * @return 结果
     */
    @Override
    public int updateUserBehaviorLog(UserBehaviorLog userBehaviorLog)
    {
        return userBehaviorLogMapper.updateUserBehaviorLog(userBehaviorLog);
    }

    /**
     * 批量删除用户行为记录
     * 
     * @param userBehaviorLogIds 需要删除的用户行为记录主键
     * @return 结果
     */
    @Override
    public int deleteUserBehaviorLogByUserBehaviorLogIds(Long[] userBehaviorLogIds)
    {
        return userBehaviorLogMapper.deleteUserBehaviorLogByUserBehaviorLogIds(userBehaviorLogIds);
    }

    /**
     * 删除用户行为记录信息
     * 
     * @param userBehaviorLogId 用户行为记录主键
     * @return 结果
     */
    @Override
    public int deleteUserBehaviorLogByUserBehaviorLogId(Long userBehaviorLogId)
    {
        return userBehaviorLogMapper.deleteUserBehaviorLogByUserBehaviorLogId(userBehaviorLogId);
    }
}
