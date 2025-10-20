package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserPrivacyMapper;
import com.ruoyi.platform.domain.UserPrivacy;
import com.ruoyi.platform.service.IUserPrivacyService;

/**
 * 用户隐私设置Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class UserPrivacyServiceImpl implements IUserPrivacyService 
{
    @Autowired
    private UserPrivacyMapper userPrivacyMapper;

    /**
     * 查询用户隐私设置
     * 
     * @param userPrivacyId 用户隐私设置主键
     * @return 用户隐私设置
     */
    @Override
    public UserPrivacy selectUserPrivacyByUserPrivacyId(Long userPrivacyId)
    {
        return userPrivacyMapper.selectUserPrivacyByUserPrivacyId(userPrivacyId);
    }

    /**
     * 查询用户隐私设置列表
     * 
     * @param userPrivacy 用户隐私设置
     * @return 用户隐私设置
     */
    @Override
    public List<UserPrivacy> selectUserPrivacyList(UserPrivacy userPrivacy)
    {
        return userPrivacyMapper.selectUserPrivacyList(userPrivacy);
    }

    /**
     * 新增用户隐私设置
     * 
     * @param userPrivacy 用户隐私设置
     * @return 结果
     */
    @Override
    public int insertUserPrivacy(UserPrivacy userPrivacy)
    {
        return userPrivacyMapper.insertUserPrivacy(userPrivacy);
    }

    /**
     * 修改用户隐私设置
     * 
     * @param userPrivacy 用户隐私设置
     * @return 结果
     */
    @Override
    public int updateUserPrivacy(UserPrivacy userPrivacy)
    {
        userPrivacy.setUpdateTime(DateUtils.getNowDate());
        return userPrivacyMapper.updateUserPrivacy(userPrivacy);
    }

    /**
     * 批量删除用户隐私设置
     * 
     * @param userPrivacyIds 需要删除的用户隐私设置主键
     * @return 结果
     */
    @Override
    public int deleteUserPrivacyByUserPrivacyIds(Long[] userPrivacyIds)
    {
        return userPrivacyMapper.deleteUserPrivacyByUserPrivacyIds(userPrivacyIds);
    }

    /**
     * 删除用户隐私设置信息
     * 
     * @param userPrivacyId 用户隐私设置主键
     * @return 结果
     */
    @Override
    public int deleteUserPrivacyByUserPrivacyId(Long userPrivacyId)
    {
        return userPrivacyMapper.deleteUserPrivacyByUserPrivacyId(userPrivacyId);
    }
}
