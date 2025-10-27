package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantWithdrawRecord;
import io.lettuce.core.dynamic.annotation.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商家提现记录 Mapper 接口
 *
 * @date 2025-10-24
 */
public interface MerchantWithdrawRecordMapper {

    /**
     * 查询商家提现记录
     *
     * @param withdrawId 提现记录ID
     * @return 提现记录
     */
    MerchantWithdrawRecord selectMerchantWithdrawRecordById(Long withdrawId);

    /**
     * 查询商家提现记录列表
     *
     * @param record 查询条件
     * @return 提现记录集合
     */
    List<MerchantWithdrawRecord> selectMerchantWithdrawRecordList(MerchantWithdrawRecord record);

    /**
     * 新增商家提现记录
     *
     * @param record 数据
     * @return 结果
     */
    int insertMerchantWithdrawRecord(MerchantWithdrawRecord record);

    /**
     * 修改商家提现记录
     */
    int updateMerchantWithdrawRecord(MerchantWithdrawRecord record);

    /**
     * 删除商家提现记录
     */
    int deleteMerchantWithdrawRecordById(Long withdrawId);

    /**
     * 批量删除商家提现记录
     */
    int deleteMerchantWithdrawRecordByIds(Long[] withdrawIds);

    /**
     * 统计商家正在提现中的金额
     *
     * @param merchantBaseId 商家ID
     * @return 提现中总金额
     */
    BigDecimal selectWithdrawingAmount(@Param("merchantBaseId") Long merchantBaseId);
}
