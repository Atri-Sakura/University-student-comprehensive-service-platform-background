package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.UserAddress;

/**
 * 用户地址Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface UserAddressMapper 
{
    /**
     * 查询用户地址
     * 
     * @param userAddressId 用户地址主键
     * @return 用户地址
     */
    public UserAddress selectUserAddressByUserAddressId(Long userAddressId);

    /**
     * 查询用户地址列表
     * 
     * @param userAddress 用户地址
     * @return 用户地址集合
     */
    public List<UserAddress> selectUserAddressList(UserAddress userAddress);

    /**
     * 新增用户地址
     * 
     * @param userAddress 用户地址
     * @return 结果
     */
    public int insertUserAddress(UserAddress userAddress);

    /**
     * 修改用户地址
     * 
     * @param userAddress 用户地址
     * @return 结果
     */
    public int updateUserAddress(UserAddress userAddress);

    /**
     * 删除用户地址
     * 
     * @param userAddressId 用户地址主键
     * @return 结果
     */
    public int deleteUserAddressByUserAddressId(Long userAddressId);

    /**
     * 批量删除用户地址
     * 
     * @param userAddressIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserAddressByUserAddressIds(Long[] userAddressIds);
}
