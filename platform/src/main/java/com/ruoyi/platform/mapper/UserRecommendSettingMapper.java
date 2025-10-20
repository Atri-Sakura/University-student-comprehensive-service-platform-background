package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.UserRecommendSetting;

/**
 * 用户个性化推荐设置Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface UserRecommendSettingMapper 
{
    /**
     * 查询用户个性化推荐设置
     * 
     * @param userRecommendSettingId 用户个性化推荐设置主键
     * @return 用户个性化推荐设置
     */
    public UserRecommendSetting selectUserRecommendSettingByUserRecommendSettingId(Long userRecommendSettingId);

    /**
     * 查询用户个性化推荐设置列表
     * 
     * @param userRecommendSetting 用户个性化推荐设置
     * @return 用户个性化推荐设置集合
     */
    public List<UserRecommendSetting> selectUserRecommendSettingList(UserRecommendSetting userRecommendSetting);

    /**
     * 新增用户个性化推荐设置
     * 
     * @param userRecommendSetting 用户个性化推荐设置
     * @return 结果
     */
    public int insertUserRecommendSetting(UserRecommendSetting userRecommendSetting);

    /**
     * 修改用户个性化推荐设置
     * 
     * @param userRecommendSetting 用户个性化推荐设置
     * @return 结果
     */
    public int updateUserRecommendSetting(UserRecommendSetting userRecommendSetting);

    /**
     * 删除用户个性化推荐设置
     * 
     * @param userRecommendSettingId 用户个性化推荐设置主键
     * @return 结果
     */
    public int deleteUserRecommendSettingByUserRecommendSettingId(Long userRecommendSettingId);

    /**
     * 批量删除用户个性化推荐设置
     * 
     * @param userRecommendSettingIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserRecommendSettingByUserRecommendSettingIds(Long[] userRecommendSettingIds);
}
