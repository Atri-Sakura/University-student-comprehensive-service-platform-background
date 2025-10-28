package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantWithdrawRecord;
import com.ruoyi.platform.domain.vo.MerchantWithdrawRecordVO;
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

    /**
     * 统计商家正在提现中的金额
     *
     * @param merchantBaseId 商家ID
     * @return 提现中总金额
     */
    MerchantWithdrawRecord selectByMerchantAndKey(@Param("merchantBaseId") Long merchantBaseId, @Param("idempotentKey") String idempotentKey);

    /**
     * 更新提现状态（成功/失败）
     *
     * @param withdrawId 提现记录ID
     * @param status     状态（SUCCESS / FAILED）
     * @param remark     备注（失败原因，可空）
     * @return 更新条数
     */
    int updateStatus(@Param("withdrawId") Long withdrawId,
                     @Param("status") String status,
                     @Param("remark") String remark);

    /**
     * 根据提现记录ID查询提现记录
     *
     * @param withdrawId 提现记录ID
     * @return 提现记录
     */
    MerchantWithdrawRecord selectWithdrawRecordById(Long withdrawId);

    /**
     * 查询商家提现列表
     *
     * @param merchantBaseId 提现记录列表
     * @param status   提现状态
     * @return 插入条数
     */
    List<MerchantWithdrawRecordVO> selectWithdrawRecordVOList(@Param("merchantBaseId") Long merchantBaseId,
                                                          @Param("status") String status);


}
