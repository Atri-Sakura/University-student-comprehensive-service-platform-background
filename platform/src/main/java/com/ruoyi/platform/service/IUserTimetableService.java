package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.UserTimetable;

/**
 * 个人课Service接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IUserTimetableService 
{
    /**
     * 查询个人课
     * 
     * @param userTimetableId 个人课主键
     * @return 个人课
     */
    public UserTimetable selectUserTimetableByUserTimetableId(Long userTimetableId);

    /**
     * 查询个人课列表
     * 
     * @param userTimetable 个人课
     * @return 个人课集合
     */
    public List<UserTimetable> selectUserTimetableList(UserTimetable userTimetable);

    /**
     * 新增个人课
     * 
     * @param userTimetable 个人课
     * @return 结果
     */
    public int insertUserTimetable(UserTimetable userTimetable);

    /**
     * 修改个人课
     * 
     * @param userTimetable 个人课
     * @return 结果
     */
    public int updateUserTimetable(UserTimetable userTimetable);

    /**
     * 批量删除个人课
     * 
     * @param userTimetableIds 需要删除的个人课主键集合
     * @return 结果
     */
    public int deleteUserTimetableByUserTimetableIds(Long[] userTimetableIds);

    /**
     * 删除个人课信息
     * 
     * @param userTimetableId 个人课主键
     * @return 结果
     */
    public int deleteUserTimetableByUserTimetableId(Long userTimetableId);
}
