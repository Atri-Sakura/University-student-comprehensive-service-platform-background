package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserBaseMapper;
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.service.IUserBaseService;

/**
 * 用户基础信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class UserBaseServiceImpl implements IUserBaseService 
{
    @Autowired
    private UserBaseMapper userBaseMapper;

    /**
     * 查询用户基础信息
     * 
     * @param userBaseId 用户基础信息主键
     * @return 用户基础信息
     */
    @Override
    public UserBase selectUserBaseByUserBaseId(Long userBaseId)
    {
        return userBaseMapper.selectUserBaseByUserBaseId(userBaseId);
    }

    /**
     * 查询用户基础信息列表
     * 
     * @param userBase 用户基础信息
     * @return 用户基础信息
     */
    @Override
    public List<UserBase> selectUserBaseList(UserBase userBase)
    {
        return userBaseMapper.selectUserBaseList(userBase);
    }

    /**
     * 新增用户基础信息
     * 
     * @param userBase 用户基础信息
     * @return 结果
     */
    @Override
    public int insertUserBase(UserBase userBase)
    {
        userBase.setCreateTime(DateUtils.getNowDate());
        return userBaseMapper.insertUserBase(userBase);
    }

    /**
     * 修改用户基础信息
     * 
     * @param userBase 用户基础信息
     * @return 结果
     */
    @Override
    public int updateUserBase(UserBase userBase)
    {
        userBase.setUpdateTime(DateUtils.getNowDate());
        return userBaseMapper.updateUserBase(userBase);
    }

    /**
     * 批量删除用户基础信息
     * 
     * @param userBaseIds 需要删除的用户基础信息主键
     * @return 结果
     */
    @Override
    public int deleteUserBaseByUserBaseIds(Long[] userBaseIds)
    {
        return userBaseMapper.deleteUserBaseByUserBaseIds(userBaseIds);
    }

    /**
     * 删除用户基础信息信息
     * 
     * @param userBaseId 用户基础信息主键
     * @return 结果
     */
    @Override
    public int deleteUserBaseByUserBaseId(Long userBaseId)
    {
        return userBaseMapper.deleteUserBaseByUserBaseId(userBaseId);
    }
}
