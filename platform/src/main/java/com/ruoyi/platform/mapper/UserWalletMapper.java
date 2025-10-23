package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.UserWallet;

/**
 * 用户钱包Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface UserWalletMapper 
{
    /**
     * 查询用户钱包
     * 
     * @param userWalletId 用户钱包主键
     * @return 用户钱包
     */
    public UserWallet selectUserWalletByUserWalletId(Long userWalletId);

    /**
     * 查询用户钱包列表
     * 
     * @param userWallet 用户钱包
     * @return 用户钱包集合
     */
    public List<UserWallet> selectUserWalletList(UserWallet userWallet);

    /**
     * 新增用户钱包
     * 
     * @param userWallet 用户钱包
     * @return 结果
     */
    public int insertUserWallet(UserWallet userWallet);

    /**
     * 修改用户钱包
     * 
     * @param userWallet 用户钱包
     * @return 结果
     */
    public int updateUserWallet(UserWallet userWallet);

    /**
     * 删除用户钱包
     * 
     * @param userWalletId 用户钱包主键
     * @return 结果
     */
    public int deleteUserWalletByUserWalletId(Long userWalletId);

    /**
     * 批量删除用户钱包
     * 
     * @param userWalletIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserWalletByUserWalletIds(Long[] userWalletIds);
}
