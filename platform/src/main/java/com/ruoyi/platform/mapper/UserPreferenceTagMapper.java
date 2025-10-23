package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.UserPreferenceTag;

/**
 * 用户偏好标签Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface UserPreferenceTagMapper 
{
    /**
     * 查询用户偏好标签
     * 
     * @param userPreferenceTagId 用户偏好标签主键
     * @return 用户偏好标签
     */
    public UserPreferenceTag selectUserPreferenceTagByUserPreferenceTagId(Long userPreferenceTagId);

    /**
     * 查询用户偏好标签列表
     * 
     * @param userPreferenceTag 用户偏好标签
     * @return 用户偏好标签集合
     */
    public List<UserPreferenceTag> selectUserPreferenceTagList(UserPreferenceTag userPreferenceTag);

    /**
     * 新增用户偏好标签
     * 
     * @param userPreferenceTag 用户偏好标签
     * @return 结果
     */
    public int insertUserPreferenceTag(UserPreferenceTag userPreferenceTag);

    /**
     * 修改用户偏好标签
     * 
     * @param userPreferenceTag 用户偏好标签
     * @return 结果
     */
    public int updateUserPreferenceTag(UserPreferenceTag userPreferenceTag);

    /**
     * 删除用户偏好标签
     * 
     * @param userPreferenceTagId 用户偏好标签主键
     * @return 结果
     */
    public int deleteUserPreferenceTagByUserPreferenceTagId(Long userPreferenceTagId);

    /**
     * 批量删除用户偏好标签
     * 
     * @param userPreferenceTagIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserPreferenceTagByUserPreferenceTagIds(Long[] userPreferenceTagIds);
}
