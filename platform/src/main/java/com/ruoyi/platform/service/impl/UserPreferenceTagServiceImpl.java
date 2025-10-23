package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserPreferenceTagMapper;
import com.ruoyi.platform.domain.UserPreferenceTag;
import com.ruoyi.platform.service.IUserPreferenceTagService;

/**
 * 用户偏好标签Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class UserPreferenceTagServiceImpl implements IUserPreferenceTagService 
{
    @Autowired
    private UserPreferenceTagMapper userPreferenceTagMapper;

    /**
     * 查询用户偏好标签
     * 
     * @param userPreferenceTagId 用户偏好标签主键
     * @return 用户偏好标签
     */
    @Override
    public UserPreferenceTag selectUserPreferenceTagByUserPreferenceTagId(Long userPreferenceTagId)
    {
        return userPreferenceTagMapper.selectUserPreferenceTagByUserPreferenceTagId(userPreferenceTagId);
    }

    /**
     * 查询用户偏好标签列表
     * 
     * @param userPreferenceTag 用户偏好标签
     * @return 用户偏好标签
     */
    @Override
    public List<UserPreferenceTag> selectUserPreferenceTagList(UserPreferenceTag userPreferenceTag)
    {
        return userPreferenceTagMapper.selectUserPreferenceTagList(userPreferenceTag);
    }

    /**
     * 新增用户偏好标签
     * 
     * @param userPreferenceTag 用户偏好标签
     * @return 结果
     */
    @Override
    public int insertUserPreferenceTag(UserPreferenceTag userPreferenceTag)
    {
        userPreferenceTag.setCreateTime(DateUtils.getNowDate());
        return userPreferenceTagMapper.insertUserPreferenceTag(userPreferenceTag);
    }

    /**
     * 修改用户偏好标签
     * 
     * @param userPreferenceTag 用户偏好标签
     * @return 结果
     */
    @Override
    public int updateUserPreferenceTag(UserPreferenceTag userPreferenceTag)
    {
        userPreferenceTag.setUpdateTime(DateUtils.getNowDate());
        return userPreferenceTagMapper.updateUserPreferenceTag(userPreferenceTag);
    }

    /**
     * 批量删除用户偏好标签
     * 
     * @param userPreferenceTagIds 需要删除的用户偏好标签主键
     * @return 结果
     */
    @Override
    public int deleteUserPreferenceTagByUserPreferenceTagIds(Long[] userPreferenceTagIds)
    {
        return userPreferenceTagMapper.deleteUserPreferenceTagByUserPreferenceTagIds(userPreferenceTagIds);
    }

    /**
     * 删除用户偏好标签信息
     * 
     * @param userPreferenceTagId 用户偏好标签主键
     * @return 结果
     */
    @Override
    public int deleteUserPreferenceTagByUserPreferenceTagId(Long userPreferenceTagId)
    {
        return userPreferenceTagMapper.deleteUserPreferenceTagByUserPreferenceTagId(userPreferenceTagId);
    }
}
