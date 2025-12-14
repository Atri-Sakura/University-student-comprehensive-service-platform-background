package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.UserBase;

/**
 * 用户基础信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface UserBaseMapper 
{
    /**
     * 查询用户基础信息
     * 
     * @param userBaseId 用户基础信息主键
     * @return 用户基础信息
     */
    public UserBase selectUserBaseByUserBaseId(Long userBaseId);

    /**
     * 查询用户基础信息列表
     * 
     * @param userBase 用户基础信息
     * @return 用户基础信息集合
     */
    public List<UserBase> selectUserBaseList(UserBase userBase);

    /**
     * 新增用户基础信息
     * 
     * @param userBase 用户基础信息
     * @return 结果
     */
    public int insertUserBase(UserBase userBase);

    /**
     * 修改用户基础信息
     * 
     * @param userBase 用户基础信息
     * @return 结果
     */
    public int updateUserBase(UserBase userBase);

    /**
     * 删除用户基础信息
     * 
     * @param userBaseId 用户基础信息主键
     * @return 结果
     */
    public int deleteUserBaseByUserBaseId(Long userBaseId);

    /**
     * 批量删除用户基础信息
     * 
     * @param userBaseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserBaseByUserBaseIds(Long[] userBaseIds);

    /**
     * 通过手机号查询用户
     *
     * @param phone 手机号
     * @return 用户基础信息
     */
    public UserBase selectUserBaseByPhone(String phone);


    /**
     * 根据电话号码查找用户Id
     * @param phone
     *
     * @return
     */
    public Long selectUserIdByPhone(String phone);

}
