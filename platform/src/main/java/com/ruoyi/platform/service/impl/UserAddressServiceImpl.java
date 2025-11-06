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
 * @date 2025-10-20
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
        // 查询该用户已经有多少收货地址
        UserAddress query = new UserAddress();
        query.setUserBaseId(userAddress.getUserBaseId());
        List<UserAddress> existList = userAddressMapper.selectUserAddressList(query);

        // 如果没有其他地址，自动设为默认
        if (existList == null || existList.isEmpty()) {
            userAddress.setIsDefault(1L);
        } else {
            if (userAddress.getIsDefault() == null) {
                userAddress.setIsDefault(0L);
            }
        }
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
        // 查询这些地址中有没有默认
        boolean hasDefault = false;
        Long userBaseId = null;
        for (Long id : userAddressIds) {
            UserAddress addr = userAddressMapper.selectUserAddressByUserAddressId(id);
            if (addr != null && addr.getIsDefault() != null && addr.getIsDefault() == 1L) {
                hasDefault = true;
                userBaseId = addr.getUserBaseId();
            }
        }

        int result = userAddressMapper.deleteUserAddressByUserAddressIds(userAddressIds);

        // 删除后，若无地址直接返回
        if (hasDefault && userBaseId != null) {
            // 查询剩余地址
            UserAddress query = new UserAddress();
            query.setUserBaseId(userBaseId);
            List<UserAddress> remain = userAddressMapper.selectUserAddressList(query);
            if (remain != null && !remain.isEmpty()) {
                // 找最新的地址（按 create_time 最大）
                UserAddress latest = remain.get(0);
                for (UserAddress a : remain) {
                    if (latest.getCreateTime().before(a.getCreateTime())) {
                        latest = a;
                    }
                }
                // 批量先全部设为非默认
                userAddressMapper.updateAllDefaultToZero(userBaseId);
                // 设置最新为默认
                UserAddress upd = new UserAddress();
                upd.setUserAddressId(latest.getUserAddressId());
                upd.setIsDefault(1L);
                userAddressMapper.updateUserAddress(upd);
            }
        }
        return result;
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

    @Override
    public void updateAllDefaultToZero(Long userBaseId) {
        userAddressMapper.updateAllDefaultToZero(userBaseId);
    }
}
