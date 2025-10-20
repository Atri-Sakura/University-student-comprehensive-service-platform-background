package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.UserPrivacy;

/**
 * 用户隐私设置Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface UserPrivacyMapper 
{
    /**
     * 查询用户隐私设置
     * 
     * @param userPrivacyId 用户隐私设置主键
     * @return 用户隐私设置
     */
    public UserPrivacy selectUserPrivacyByUserPrivacyId(Long userPrivacyId);

    /**
     * 查询用户隐私设置列表
     * 
     * @param userPrivacy 用户隐私设置
     * @return 用户隐私设置集合
     */
    public List<UserPrivacy> selectUserPrivacyList(UserPrivacy userPrivacy);

    /**
     * 新增用户隐私设置
     * 
     * @param userPrivacy 用户隐私设置
     * @return 结果
     */
    public int insertUserPrivacy(UserPrivacy userPrivacy);

    /**
     * 修改用户隐私设置
     * 
     * @param userPrivacy 用户隐私设置
     * @return 结果
     */
    public int updateUserPrivacy(UserPrivacy userPrivacy);

    /**
     * 删除用户隐私设置
     * 
     * @param userPrivacyId 用户隐私设置主键
     * @return 结果
     */
    public int deleteUserPrivacyByUserPrivacyId(Long userPrivacyId);

    /**
     * 批量删除用户隐私设置
     * 
     * @param userPrivacyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserPrivacyByUserPrivacyIds(Long[] userPrivacyIds);
}
