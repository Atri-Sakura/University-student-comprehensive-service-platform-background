package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.RiderWalletRecord;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 骑手钱包流水Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface RiderWalletRecordMapper 
{
    int updateTradeStatusByRelatedId(@Param("relatedId") Long relatedId,
                                     @Param("tradeStatus") Long tradeStatus);

    // 提现失败/成功按 out_biz_no 对应的 pay_order 再扩展也行
    int updateStatusByRelatedId(
            @Param("relatedId") Long relatedId,
            @Param("tradeStatus") Integer tradeStatus
    );

    // 支付成功后按 related_id 更新状态
    int updateStatusOnRechargeSuccessByRelatedId(
            @Param("relatedId") Long relatedId,
            @Param("tradeStatus") Integer tradeStatus
    );
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
