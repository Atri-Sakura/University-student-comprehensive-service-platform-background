package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.platform.domain.MerchantWalletFlow;
import com.ruoyi.platform.merchant.mapper.MerchantWalletFlowMapper;
import com.ruoyi.platform.merchant.service.IMerchantWalletFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商家钱包流水 Service 业务层实现
 *
 * 封装商家钱包流水的增删改查业务逻辑。
 *
 * @author Jinx
 * @date 2025-10-24
 */
@Service
public class MerchantWalletFlowServiceImpl implements IMerchantWalletFlowService {
    @Autowired
    private MerchantWalletFlowMapper merchantWalletFlowMapper;

    /**
     * 查询商家钱包流水
     *
     * @param flowId 流水记录ID
     * @return 商家钱包流水
     */
    @Override
    public MerchantWalletFlow selectMerchantWalletFlowById(Long flowId)
    {
        return merchantWalletFlowMapper.selectMerchantWalletFlowById(flowId);
    }

    /**
     * 查询商家钱包流水列表
     *
     * @param flow 查询条件
     * @return 商家钱包流水集合
     */
    @Override
    public List<MerchantWalletFlow> selectMerchantWalletFlowList(MerchantWalletFlow flow)
    {
        return merchantWalletFlowMapper.selectMerchantWalletFlowList(flow);
    }

    /**
     * 新增商家钱包流水
     *
     * @param flow 商家钱包流水对象
     * @return 结果
     */
    @Override
    public int insertMerchantWalletFlow(MerchantWalletFlow flow)
    {
        return merchantWalletFlowMapper.insertMerchantWalletFlow(flow);
    }

    /**
     * 修改商家钱包流水
     *
     * @param flow 商家钱包流水对象
     * @return 结果
     */
    @Override
    public int updateMerchantWalletFlow(MerchantWalletFlow flow)
    {
        return merchantWalletFlowMapper.updateMerchantWalletFlow(flow);
    }

    /**
     * 批量删除商家钱包流水
     *
     * @param flowIds 要删除的ID数组
     * @return 结果
     */
    @Override
    public int deleteMerchantWalletFlowByIds(Long[] flowIds)
    {
        return merchantWalletFlowMapper.deleteMerchantWalletFlowByIds(flowIds);
    }

    /**
     * 删除单个商家钱包流水
     *
     * @param flowId 流水记录ID
     * @return 结果
     */
    @Override
    public int deleteMerchantWalletFlowById(Long flowId)
    {
        return merchantWalletFlowMapper.deleteMerchantWalletFlowById(flowId);
    }
}
