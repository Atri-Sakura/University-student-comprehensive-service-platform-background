package com.ruoyi.platform. merchant.service. impl;

import com.ruoyi.platform.domain.MerchantWalletFlow;
import com.ruoyi.platform.merchant.mapper.MerchantWalletFlowMapper;
import com. ruoyi.platform.merchant. service.IMerchantWalletFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家钱包流水Service业务层处理
 *
 * @author Jinx
 * @date 2025-10-24
 */
@Service
public class MerchantWalletFlowServiceImpl implements IMerchantWalletFlowService {

    @Autowired
    private MerchantWalletFlowMapper merchantWalletFlowMapper;

    @Override
    public void insertWithdrawFreezeFlow(Long merchantBaseId, Long withdrawId, BigDecimal totalAmount) {
        merchantWalletFlowMapper.insertWithdrawFreezeFlow(merchantBaseId, withdrawId, totalAmount);
    }

    @Override
    public MerchantWalletFlow selectMerchantWalletFlowById(Long flowId) {
        return merchantWalletFlowMapper. selectMerchantWalletFlowById(flowId);
    }

    @Override
    public List<MerchantWalletFlow> selectMerchantWalletFlowList(MerchantWalletFlow flow) {
        return merchantWalletFlowMapper.selectMerchantWalletFlowList(flow);
    }

    @Override
    public int insertMerchantWalletFlow(MerchantWalletFlow flow) {
        return merchantWalletFlowMapper.insertMerchantWalletFlow(flow);
    }

    @Override
    public int updateMerchantWalletFlow(MerchantWalletFlow flow) {
        return merchantWalletFlowMapper.updateMerchantWalletFlow(flow);
    }

    @Override
    public int deleteMerchantWalletFlowByIds(Long[] flowIds) {
        return merchantWalletFlowMapper.deleteMerchantWalletFlowByIds(flowIds);
    }

    @Override
    public int deleteMerchantWalletFlowById(Long flowId) {
        return merchantWalletFlowMapper.deleteMerchantWalletFlowById(flowId);
    }

    /**
     * 获取商家流水汇总信息
     *
     * @param merchantBaseId 商家ID
     * @return 汇总数据
     */
    @Override
    public Map<String, Object> getFlowSummary(Long merchantBaseId) {
        Map<String, Object> summary = new HashMap<>();

        // 查询总收入
        BigDecimal totalIncome = merchantWalletFlowMapper.sumAmountByType(merchantBaseId, "INCOME");
        // 查询总提现
        BigDecimal totalWithdraw = merchantWalletFlowMapper.sumAmountByType(merchantBaseId, "WITHDRAW_SUCCESS");
        // 查询总退款
        BigDecimal totalRefund = merchantWalletFlowMapper.sumAmountByType(merchantBaseId, "REFUND");

        summary.put("totalIncome", totalIncome != null ? totalIncome : BigDecimal.ZERO);
        summary.put("totalWithdraw", totalWithdraw != null ? totalWithdraw. abs() : BigDecimal.ZERO);
        summary.put("totalRefund", totalRefund != null ? totalRefund.abs() : BigDecimal.ZERO);

        return summary;
    }
}