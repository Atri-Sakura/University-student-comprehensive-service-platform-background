package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantWalletFlow;
import com.ruoyi.platform.domain.MerchantWithdrawRecord;
import io.lettuce.core.dynamic.annotation.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商家钱包流水 Mapper 接口
 *
 * @author Jinx
 * @date 2025-10-24
 */
public interface MerchantWalletFlowMapper {
    /**
     * 插入提现冻结流水记录
     *
     * @param merchantBaseId 商家ID
     * @param withdrawId     提现记录ID
     * @param totalAmount    提现金额（含手续费）
     * @return 插入结果
     */
    int insertWithdrawFreezeFlow(@Param("merchantBaseId") Long merchantBaseId,
                                 @Param("withdrawId") Long withdrawId,
                                 @Param("totalAmount") java.math.BigDecimal totalAmount);
    /**
     * 根据流水ID查询流水信息
     * @param flowId 流水ID
     * @return 流水信息
     */
    MerchantWalletFlow selectMerchantWalletFlowById(Long flowId);

    /**
     * 查询商家钱包流水列表
     * @param flow 查询条件
     * @return 列表
     */
    List<MerchantWalletFlow> selectMerchantWalletFlowList(MerchantWalletFlow flow);

    /**
     * 新增商家钱包流水
     * @param flow 数据
     * @return 影响行数
     */
    int insertMerchantWalletFlow(MerchantWalletFlow flow);

    /**
     * 修改商家钱包流水
     * @param flow 数据
     * @return 影响行数
     */
    int updateMerchantWalletFlow(MerchantWalletFlow flow);

    /**
     * 删除商家钱包流水
     * @param flowId 删除条件
     * @return 影响行数
     */
    int deleteMerchantWalletFlowById(Long flowId);

    /**
     * 批量删除商家钱包流水
     * @param flowIds 删除条件
     * @return 影响行数
     */
    int deleteMerchantWalletFlowByIds(Long[] flowIds);

    /**
     * 提现成功流水
     *
     * @param record 提现记录对象
     * @return 插入条数
     */
    int insertWithdrawSuccessFlow(@Param("record") MerchantWithdrawRecord record);

    /**
     * 提现失败流水（回滚）
     *
     * @param record 提现记录对象
     * @return 插入条数
     */
    int insertWithdrawFailFlow(@Param("record") MerchantWithdrawRecord record);

    /**
     * 按类型汇总金额
     *
     * @param merchantBaseId 商家ID
     * @param flowType 流水类型
     * @return 汇总金额
     */
    BigDecimal sumAmountByType(@Param("merchantBaseId") Long merchantBaseId,
                               @Param("flowType") String flowType);
}
