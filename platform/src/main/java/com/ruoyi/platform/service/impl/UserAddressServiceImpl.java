package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserAddressMapper;
import com.ruoyi.platform.domain.UserAddress;
import com.ruoyi.platform.service.IUserAddressService;

/**
 * 用户地址Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class UserAddressServiceImpl implements IUserAddressService 
{
    @Autowired
    private UserAddressMapper userAddressMapper;

    /**
     * 查询用户地址
     * 
     * @param userAddressId 用户地址主键
     * @return 用户地址
     */
    @Override
    public UserAddress selectUserAddressByUserAddressId(Long userAddressId)
    {
        return userAddressMapper.selectUserAddressByUserAddressId(userAddressId);
    }

    /**
     * 查询用户地址列表
     * 
     * @param userAddress 用户地址
     * @return 用户地址
     */
    @Override
    public List<UserAddress> selectUserAddressList(UserAddress userAddress)
    {
        return userAddressMapper.selectUserAddressList(userAddress);
    }

    /**
     * 新增用户地址
     * 
     * @param userAddress 用户地址
     * @return 结果
     */
    @Override
    public int insertUserAddress(UserAddress userAddress)
    {
        userAddress.setCreateTime(DateUtils.getNowDate());
        return userAddressMapper.insertUserAddress(userAddress);
    }

    /**
     * 修改用户地址
     * 
     * @param userAddress 用户地址
     * @return 结果
     */
    @Override
    public int updateUserAddress(UserAddress userAddress)
    {
        userAddress.setUpdateTime(DateUtils.getNowDate());
        return userAddressMapper.updateUserAddress(userAddress);
    }

    /**
     * 批量删除用户地址
     * 
     * @param userAddressIds 需要删除的用户地址主键
     * @return 结果
     */
    @Override
    public int deleteUserAddressByUserAddressIds(Long[] userAddressIds)
    {
        return userAddressMapper.deleteUserAddressByUserAddressIds(userAddressIds);
    }

    /**
     * 删除用户地址信息
     * 
     * @param userAddressId 用户地址主键
     * @return 结果
     */
    @Override
    public int deleteUserAddressByUserAddressId(Long userAddressId)
    {
        return userAddressMapper.deleteUserAddressByUserAddressId(userAddressId);
    }
}
