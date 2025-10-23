package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserTimetableMapper;
import com.ruoyi.platform.domain.UserTimetable;
import com.ruoyi.platform.service.IUserTimetableService;

/**
 * 个人课Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class UserTimetableServiceImpl implements IUserTimetableService 
{
    @Autowired
    private UserTimetableMapper userTimetableMapper;

    /**
     * 查询个人课
     * 
     * @param userTimetableId 个人课主键
     * @return 个人课
     */
    @Override
    public UserTimetable selectUserTimetableByUserTimetableId(Long userTimetableId)
    {
        return userTimetableMapper.selectUserTimetableByUserTimetableId(userTimetableId);
    }

    /**
     * 查询个人课列表
     * 
     * @param userTimetable 个人课
     * @return 个人课
     */
    @Override
    public List<UserTimetable> selectUserTimetableList(UserTimetable userTimetable)
    {
        return userTimetableMapper.selectUserTimetableList(userTimetable);
    }

    /**
     * 新增个人课
     * 
     * @param userTimetable 个人课
     * @return 结果
     */
    @Override
    public int insertUserTimetable(UserTimetable userTimetable)
    {
        userTimetable.setCreateTime(DateUtils.getNowDate());
        return userTimetableMapper.insertUserTimetable(userTimetable);
    }

    /**
     * 修改个人课
     * 
     * @param userTimetable 个人课
     * @return 结果
     */
    @Override
    public int updateUserTimetable(UserTimetable userTimetable)
    {
        return userTimetableMapper.updateUserTimetable(userTimetable);
    }

    /**
     * 批量删除个人课
     * 
     * @param userTimetableIds 需要删除的个人课主键
     * @return 结果
     */
    @Override
    public int deleteUserTimetableByUserTimetableIds(Long[] userTimetableIds)
    {
        return userTimetableMapper.deleteUserTimetableByUserTimetableIds(userTimetableIds);
    }

    /**
     * 删除个人课信息
     * 
     * @param userTimetableId 个人课主键
     * @return 结果
     */
    @Override
    public int deleteUserTimetableByUserTimetableId(Long userTimetableId)
    {
        return userTimetableMapper.deleteUserTimetableByUserTimetableId(userTimetableId);
    }
}
