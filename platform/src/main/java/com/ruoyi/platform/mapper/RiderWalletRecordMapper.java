package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.RiderWalletRecord;

/**
 * 骑手钱包流水Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface RiderWalletRecordMapper 
{
    /**
     * 查询骑手钱包流水
     * 
     * @param riderWalletRecordId 骑手钱包流水主键
     * @return 骑手钱包流水
     */
    public RiderWalletRecord selectRiderWalletRecordByRiderWalletRecordId(Long riderWalletRecordId);

    /**
     * 查询骑手钱包流水列表
     * 
     * @param riderWalletRecord 骑手钱包流水
     * @return 骑手钱包流水集合
     */
    public List<RiderWalletRecord> selectRiderWalletRecordList(RiderWalletRecord riderWalletRecord);

    /**
     * 新增骑手钱包流水
     * 
     * @param riderWalletRecord 骑手钱包流水
     * @return 结果
     */
    public int insertRiderWalletRecord(RiderWalletRecord riderWalletRecord);

    /**
     * 修改骑手钱包流水
     * 
     * @param riderWalletRecord 骑手钱包流水
     * @return 结果
     */
    public int updateRiderWalletRecord(RiderWalletRecord riderWalletRecord);

    /**
     * 删除骑手钱包流水
     * 
     * @param riderWalletRecordId 骑手钱包流水主键
     * @return 结果
     */
    public int deleteRiderWalletRecordByRiderWalletRecordId(Long riderWalletRecordId);

    /**
     * 批量删除骑手钱包流水
     * 
     * @param riderWalletRecordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRiderWalletRecordByRiderWalletRecordIds(Long[] riderWalletRecordIds);
}
