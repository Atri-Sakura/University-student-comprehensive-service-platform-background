package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.UserTimetable;

/**
 * 个人课Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface UserTimetableMapper 
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
     * 删除个人课
     * 
     * @param userTimetableId 个人课主键
     * @return 结果
     */
    public int deleteUserTimetableByUserTimetableId(Long userTimetableId);

    /**
     * 批量删除个人课
     * 
     * @param userTimetableIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserTimetableByUserTimetableIds(Long[] userTimetableIds);
}
