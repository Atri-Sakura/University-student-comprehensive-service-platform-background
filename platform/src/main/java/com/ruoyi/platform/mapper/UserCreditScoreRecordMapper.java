package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.UserCreditScoreRecord;

/**
 * 用户信用分流水Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface UserCreditScoreRecordMapper 
{
    /**
     * 查询用户信用分流水
     * 
     * @param id 用户信用分流水主键
     * @return 用户信用分流水
     */
    public UserCreditScoreRecord selectUserCreditScoreRecordById(Long id);

    /**
     * 查询用户信用分流水列表
     * 
     * @param userCreditScoreRecord 用户信用分流水
     * @return 用户信用分流水集合
     */
    public List<UserCreditScoreRecord> selectUserCreditScoreRecordList(UserCreditScoreRecord userCreditScoreRecord);

    /**
     * 新增用户信用分流水
     * 
     * @param userCreditScoreRecord 用户信用分流水
     * @return 结果
     */
    public int insertUserCreditScoreRecord(UserCreditScoreRecord userCreditScoreRecord);

    /**
     * 修改用户信用分流水
     * 
     * @param userCreditScoreRecord 用户信用分流水
     * @return 结果
     */
    public int updateUserCreditScoreRecord(UserCreditScoreRecord userCreditScoreRecord);

    /**
     * 删除用户信用分流水
     * 
     * @param id 用户信用分流水主键
     * @return 结果
     */
    public int deleteUserCreditScoreRecordById(Long id);

    /**
     * 批量删除用户信用分流水
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserCreditScoreRecordByIds(Long[] ids);
}
