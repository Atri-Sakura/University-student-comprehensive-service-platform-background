package com.ruoyi.platform.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.UserCreditScoreRecordMapper;
import com.ruoyi.platform.domain.UserCreditScoreRecord;
import com.ruoyi.platform.service.IUserCreditScoreRecordService;

/**
 * 用户信用分流水Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class UserCreditScoreRecordServiceImpl implements IUserCreditScoreRecordService 
{
    @Autowired
    private UserCreditScoreRecordMapper userCreditScoreRecordMapper;

    /**
     * 查询用户信用分流水
     * 
     * @param id 用户信用分流水主键
     * @return 用户信用分流水
     */
    @Override
    public UserCreditScoreRecord selectUserCreditScoreRecordById(Long id)
    {
        return userCreditScoreRecordMapper.selectUserCreditScoreRecordById(id);
    }

    /**
     * 查询用户信用分流水列表
     * 
     * @param userCreditScoreRecord 用户信用分流水
     * @return 用户信用分流水
     */
    @Override
    public List<UserCreditScoreRecord> selectUserCreditScoreRecordList(UserCreditScoreRecord userCreditScoreRecord)
    {
        return userCreditScoreRecordMapper.selectUserCreditScoreRecordList(userCreditScoreRecord);
    }

    /**
     * 新增用户信用分流水
     * 
     * @param userCreditScoreRecord 用户信用分流水
     * @return 结果
     */
    @Override
    public int insertUserCreditScoreRecord(UserCreditScoreRecord userCreditScoreRecord)
    {
        return userCreditScoreRecordMapper.insertUserCreditScoreRecord(userCreditScoreRecord);
    }

    /**
     * 修改用户信用分流水
     * 
     * @param userCreditScoreRecord 用户信用分流水
     * @return 结果
     */
    @Override
    public int updateUserCreditScoreRecord(UserCreditScoreRecord userCreditScoreRecord)
    {
        return userCreditScoreRecordMapper.updateUserCreditScoreRecord(userCreditScoreRecord);
    }

    /**
     * 批量删除用户信用分流水
     * 
     * @param ids 需要删除的用户信用分流水主键
     * @return 结果
     */
    @Override
    public int deleteUserCreditScoreRecordByIds(Long[] ids)
    {
        return userCreditScoreRecordMapper.deleteUserCreditScoreRecordByIds(ids);
    }

    /**
     * 删除用户信用分流水信息
     * 
     * @param id 用户信用分流水主键
     * @return 结果
     */
    @Override
    public int deleteUserCreditScoreRecordById(Long id)
    {
        return userCreditScoreRecordMapper.deleteUserCreditScoreRecordById(id);
    }
}
