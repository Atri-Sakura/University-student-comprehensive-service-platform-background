package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserWalletMapper;
import com.ruoyi.platform.domain.UserWallet;
import com.ruoyi.platform.service.IUserWalletService;

/**
 * 用户钱包Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class UserWalletServiceImpl implements IUserWalletService 
{
    @Autowired
    private UserWalletMapper userWalletMapper;

    /**
     * 查询用户钱包
     * 
     * @param userWalletId 用户钱包主键
     * @return 用户钱包
     */
    @Override
    public UserWallet selectUserWalletByUserWalletId(Long userWalletId)
    {
        return userWalletMapper.selectUserWalletByUserWalletId(userWalletId);
    }

    /**
     * 查询用户钱包列表
     * 
     * @param userWallet 用户钱包
     * @return 用户钱包
     */
    @Override
    public List<UserWallet> selectUserWalletList(UserWallet userWallet)
    {
        return userWalletMapper.selectUserWalletList(userWallet);
    }

    /**
     * 新增用户钱包
     * 
     * @param userWallet 用户钱包
     * @return 结果
     */
    @Override
    public int insertUserWallet(UserWallet userWallet)
    {
        userWallet.setCreateTime(DateUtils.getNowDate());
        return userWalletMapper.insertUserWallet(userWallet);
    }

    /**
     * 修改用户钱包
     * 
     * @param userWallet 用户钱包
     * @return 结果
     */
    @Override
    public int updateUserWallet(UserWallet userWallet)
    {
        userWallet.setUpdateTime(DateUtils.getNowDate());
        return userWalletMapper.updateUserWallet(userWallet);
    }

    /**
     * 批量删除用户钱包
     * 
     * @param userWalletIds 需要删除的用户钱包主键
     * @return 结果
     */
    @Override
    public int deleteUserWalletByUserWalletIds(Long[] userWalletIds)
    {
        return userWalletMapper.deleteUserWalletByUserWalletIds(userWalletIds);
    }

    /**
     * 删除用户钱包信息
     * 
     * @param userWalletId 用户钱包主键
     * @return 结果
     */
    @Override
    public int deleteUserWalletByUserWalletId(Long userWalletId)
    {
        return userWalletMapper.deleteUserWalletByUserWalletId(userWalletId);
    }
}
