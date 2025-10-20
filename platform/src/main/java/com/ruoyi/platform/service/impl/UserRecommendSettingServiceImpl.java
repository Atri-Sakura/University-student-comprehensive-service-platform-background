package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserRecommendSettingMapper;
import com.ruoyi.platform.domain.UserRecommendSetting;
import com.ruoyi.platform.service.IUserRecommendSettingService;

/**
 * 用户个性化推荐设置Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class UserRecommendSettingServiceImpl implements IUserRecommendSettingService 
{
    @Autowired
    private UserRecommendSettingMapper userRecommendSettingMapper;

    /**
     * 查询用户个性化推荐设置
     * 
     * @param userRecommendSettingId 用户个性化推荐设置主键
     * @return 用户个性化推荐设置
     */
    @Override
    public UserRecommendSetting selectUserRecommendSettingByUserRecommendSettingId(Long userRecommendSettingId)
    {
        return userRecommendSettingMapper.selectUserRecommendSettingByUserRecommendSettingId(userRecommendSettingId);
    }

    /**
     * 查询用户个性化推荐设置列表
     * 
     * @param userRecommendSetting 用户个性化推荐设置
     * @return 用户个性化推荐设置
     */
    @Override
    public List<UserRecommendSetting> selectUserRecommendSettingList(UserRecommendSetting userRecommendSetting)
    {
        return userRecommendSettingMapper.selectUserRecommendSettingList(userRecommendSetting);
    }

    /**
     * 新增用户个性化推荐设置
     * 
     * @param userRecommendSetting 用户个性化推荐设置
     * @return 结果
     */
    @Override
    public int insertUserRecommendSetting(UserRecommendSetting userRecommendSetting)
    {
        userRecommendSetting.setCreateTime(DateUtils.getNowDate());
        return userRecommendSettingMapper.insertUserRecommendSetting(userRecommendSetting);
    }

    /**
     * 修改用户个性化推荐设置
     * 
     * @param userRecommendSetting 用户个性化推荐设置
     * @return 结果
     */
    @Override
    public int updateUserRecommendSetting(UserRecommendSetting userRecommendSetting)
    {
        userRecommendSetting.setUpdateTime(DateUtils.getNowDate());
        return userRecommendSettingMapper.updateUserRecommendSetting(userRecommendSetting);
    }

    /**
     * 批量删除用户个性化推荐设置
     * 
     * @param userRecommendSettingIds 需要删除的用户个性化推荐设置主键
     * @return 结果
     */
    @Override
    public int deleteUserRecommendSettingByUserRecommendSettingIds(Long[] userRecommendSettingIds)
    {
        return userRecommendSettingMapper.deleteUserRecommendSettingByUserRecommendSettingIds(userRecommendSettingIds);
    }

    /**
     * 删除用户个性化推荐设置信息
     * 
     * @param userRecommendSettingId 用户个性化推荐设置主键
     * @return 结果
     */
    @Override
    public int deleteUserRecommendSettingByUserRecommendSettingId(Long userRecommendSettingId)
    {
        return userRecommendSettingMapper.deleteUserRecommendSettingByUserRecommendSettingId(userRecommendSettingId);
    }
}
