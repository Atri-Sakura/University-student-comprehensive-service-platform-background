package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.UserWalletRecord;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 用户钱包流水Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IUserWalletRecordService 
{

    /**
     * 查询用户钱包流水
     * 
     * @param userWalletRecordId 用户钱包流水主键
     * @return 用户钱包流水
     */
    public UserWalletRecord selectUserWalletRecordByUserWalletRecordId(Long userWalletRecordId);

    /**
     * 查询用户钱包流水列表
     * 
     * @param userWalletRecord 用户钱包流水
     * @return 用户钱包流水集合
     */
    public List<UserWalletRecord> selectUserWalletRecordList(UserWalletRecord userWalletRecord);

    /**
     * 新增用户钱包流水
     * 
     * @param userWalletRecord 用户钱包流水
     * @return 结果
     */
    public int insertUserWalletRecord(UserWalletRecord userWalletRecord);

    /**
     * 修改用户钱包流水
     * 
     * @param userWalletRecord 用户钱包流水
     * @return 结果
     */
    public int updateUserWalletRecord(UserWalletRecord userWalletRecord);

    /**
     * 批量删除用户钱包流水
     * 
     * @param userWalletRecordIds 需要删除的用户钱包流水主键集合
     * @return 结果
     */
    public int deleteUserWalletRecordByUserWalletRecordIds(Long[] userWalletRecordIds);

    /**
     * 删除用户钱包流水信息
     * 
     * @param userWalletRecordId 用户钱包流水主键
     * @return 结果
     */
    public int deleteUserWalletRecordByUserWalletRecordId(Long userWalletRecordId);
}
