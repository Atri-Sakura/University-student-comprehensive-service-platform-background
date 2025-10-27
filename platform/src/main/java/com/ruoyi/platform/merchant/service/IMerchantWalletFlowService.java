package com.ruoyi.platform.merchant.service;


import com.ruoyi.platform.domain.MerchantWalletFlow;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商家钱包流水 Service 接口
 *
 * 定义商家钱包流水的业务逻辑操作接口，包括查询、插入、修改与删除。
 *
 * @author Jinx
 * @date 2025-10-24
 */
public interface IMerchantWalletFlowService {
    /**
     * 插入提现冻结流水
     *
     * @param merchantBaseId 商家ID
     * @param withdrawId 提现记录ID
     * @param totalAmount 冻结金额（含手续费）
     */
    void insertWithdrawFreezeFlow(Long merchantBaseId, Long withdrawId, BigDecimal totalAmount);
    /**
     * 查询商家钱包流水
     *
     * @param flowId 流水记录ID
     * @return 商家钱包流水信息
     */
    MerchantWalletFlow selectMerchantWalletFlowById(Long flowId);

    /**
     * 查询商家钱包流水列表
     *
     * @param flow 查询条件
     * @return 商家钱包流水集合
     */
    List<MerchantWalletFlow> selectMerchantWalletFlowList(MerchantWalletFlow flow);

    /**
     * 新增商家钱包流水
     *
     * @param flow 商家钱包流水对象
     * @return 结果
     */
    int insertMerchantWalletFlow(MerchantWalletFlow flow);

    /**
     * 修改商家钱包流水
     *
     * @param flow 商家钱包流水对象
     * @return 结果
     */
    int updateMerchantWalletFlow(MerchantWalletFlow flow);

    /**
     * 批量删除商家钱包流水
     *
     * @param flowIds 要删除的流水记录ID集合
     * @return 结果
     */
    int deleteMerchantWalletFlowByIds(Long[] flowIds);

    /**
     * 删除单个商家钱包流水
     *
     * @param flowId 流水记录ID
     * @return 结果
     */
    int deleteMerchantWalletFlowById(Long flowId);
}
