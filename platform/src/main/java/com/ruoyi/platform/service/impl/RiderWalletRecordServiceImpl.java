package com.ruoyi.platform.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.RiderWalletRecordMapper;
import com.ruoyi.platform.domain.RiderWalletRecord;
import com.ruoyi.platform.service.IRiderWalletRecordService;

/**
 * 骑手钱包流水Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class RiderWalletRecordServiceImpl implements IRiderWalletRecordService 
{
    @Autowired
    private RiderWalletRecordMapper riderWalletRecordMapper;

    /**
     * 查询骑手钱包流水
     * 
     * @param riderWalletRecordId 骑手钱包流水主键
     * @return 骑手钱包流水
     */
    @Override
    public RiderWalletRecord selectRiderWalletRecordByRiderWalletRecordId(Long riderWalletRecordId)
    {
        return riderWalletRecordMapper.selectRiderWalletRecordByRiderWalletRecordId(riderWalletRecordId);
    }

    /**
     * 查询骑手钱包流水列表
     * 
     * @param riderWalletRecord 骑手钱包流水
     * @return 骑手钱包流水
     */
    @Override
    public List<RiderWalletRecord> selectRiderWalletRecordList(RiderWalletRecord riderWalletRecord)
    {
        return riderWalletRecordMapper.selectRiderWalletRecordList(riderWalletRecord);
    }

    /**
     * 新增骑手钱包流水
     * 
     * @param riderWalletRecord 骑手钱包流水
     * @return 结果
     */
    @Override
    public int insertRiderWalletRecord(RiderWalletRecord riderWalletRecord)
    {
        return riderWalletRecordMapper.insertRiderWalletRecord(riderWalletRecord);
    }

    /**
     * 修改骑手钱包流水
     * 
     * @param riderWalletRecord 骑手钱包流水
     * @return 结果
     */
    @Override
    public int updateRiderWalletRecord(RiderWalletRecord riderWalletRecord)
    {
        return riderWalletRecordMapper.updateRiderWalletRecord(riderWalletRecord);
    }

    /**
     * 批量删除骑手钱包流水
     * 
     * @param riderWalletRecordIds 需要删除的骑手钱包流水主键
     * @return 结果
     */
    @Override
    public int deleteRiderWalletRecordByRiderWalletRecordIds(Long[] riderWalletRecordIds)
    {
        return riderWalletRecordMapper.deleteRiderWalletRecordByRiderWalletRecordIds(riderWalletRecordIds);
    }

    /**
     * 删除骑手钱包流水信息
     * 
     * @param riderWalletRecordId 骑手钱包流水主键
     * @return 结果
     */
    @Override
    public int deleteRiderWalletRecordByRiderWalletRecordId(Long riderWalletRecordId)
    {
        return riderWalletRecordMapper.deleteRiderWalletRecordByRiderWalletRecordId(riderWalletRecordId);
    }
}
