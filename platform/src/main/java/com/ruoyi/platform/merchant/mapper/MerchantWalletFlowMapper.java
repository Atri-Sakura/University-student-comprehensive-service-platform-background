package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantWalletFlow;

import java.util.List;

/**
 * 商家钱包流水 Mapper 接口
 *
 * @author Jinx
 * @date 2025-10-24
 */
public interface MerchantWalletFlowMapper {
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
}
