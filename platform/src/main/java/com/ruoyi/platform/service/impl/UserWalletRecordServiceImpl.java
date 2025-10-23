package com.ruoyi.platform.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserWalletRecordMapper;
import com.ruoyi.platform.domain.UserWalletRecord;
import com.ruoyi.platform.service.IUserWalletRecordService;

/**
 * 用户钱包流水Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class UserWalletRecordServiceImpl implements IUserWalletRecordService 
{
    @Autowired
    private UserWalletRecordMapper userWalletRecordMapper;

    /**
     * 查询用户钱包流水
     * 
     * @param userWalletRecordId 用户钱包流水主键
     * @return 用户钱包流水
     */
    @Override
    public UserWalletRecord selectUserWalletRecordByUserWalletRecordId(Long userWalletRecordId)
    {
        return userWalletRecordMapper.selectUserWalletRecordByUserWalletRecordId(userWalletRecordId);
    }

    /**
     * 查询用户钱包流水列表
     * 
     * @param userWalletRecord 用户钱包流水
     * @return 用户钱包流水
     */
    @Override
    public List<UserWalletRecord> selectUserWalletRecordList(UserWalletRecord userWalletRecord)
    {
        return userWalletRecordMapper.selectUserWalletRecordList(userWalletRecord);
    }

    /**
     * 新增用户钱包流水
     * 
     * @param userWalletRecord 用户钱包流水
     * @return 结果
     */
    @Override
    public int insertUserWalletRecord(UserWalletRecord userWalletRecord)
    {
        return userWalletRecordMapper.insertUserWalletRecord(userWalletRecord);
    }

    /**
     * 修改用户钱包流水
     * 
     * @param userWalletRecord 用户钱包流水
     * @return 结果
     */
    @Override
    public int updateUserWalletRecord(UserWalletRecord userWalletRecord)
    {
        return userWalletRecordMapper.updateUserWalletRecord(userWalletRecord);
    }

    /**
     * 批量删除用户钱包流水
     * 
     * @param userWalletRecordIds 需要删除的用户钱包流水主键
     * @return 结果
     */
    @Override
    public int deleteUserWalletRecordByUserWalletRecordIds(Long[] userWalletRecordIds)
    {
        return userWalletRecordMapper.deleteUserWalletRecordByUserWalletRecordIds(userWalletRecordIds);
    }

    /**
     * 删除用户钱包流水信息
     * 
     * @param userWalletRecordId 用户钱包流水主键
     * @return 结果
     */
    @Override
    public int deleteUserWalletRecordByUserWalletRecordId(Long userWalletRecordId)
    {
        return userWalletRecordMapper.deleteUserWalletRecordByUserWalletRecordId(userWalletRecordId);
    }
}
