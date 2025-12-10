package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.platform.domain.MerchantWalletFlow;
import com.ruoyi.platform.domain.vo.MerchantWalletFlowVO;
import com.ruoyi.platform.merchant.mapper.MerchantWalletFlowMapper;
import com.ruoyi.platform.merchant.service.IMerchantWalletFlowService;
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
    public List<MerchantWalletFlowVO> selectMerchantWalletFlowListWithOrder(Long merchantBaseId,
                                                                            String flowType,
                                                                            String orderNo,
                                                                            String startTime,
                                                                            String endTime) {
        List<MerchantWalletFlowVO> list = merchantWalletFlowMapper.selectMerchantWalletFlowListWithOrder(
                merchantBaseId, flowType, orderNo, startTime, endTime
        );

        // 补充类型描述
        for (MerchantWalletFlowVO vo : list) {
            vo.setFlowTypeDesc(getFlowTypeDesc(vo.getFlowType()));
            if (vo.getOrderType() != null) {
                vo.setOrderTypeDesc(getOrderTypeDesc(vo.getOrderType()));
            }
            if (vo.getOrderStatus() != null) {
                vo. setOrderStatusDesc(getOrderStatusDesc(vo.getOrderStatus()));
            }
        }

        return list;
    }

    @Override
    public MerchantWalletFlowVO selectMerchantWalletFlowWithOrderById(Long flowId, Long merchantBaseId) {
        MerchantWalletFlowVO vo = merchantWalletFlowMapper.selectMerchantWalletFlowWithOrderById(flowId, merchantBaseId);
        if (vo != null) {
            vo.setFlowTypeDesc(getFlowTypeDesc(vo.getFlowType()));
            if (vo. getOrderType() != null) {
                vo.setOrderTypeDesc(getOrderTypeDesc(vo. getOrderType()));
            }
            if (vo.getOrderStatus() != null) {
                vo.setOrderStatusDesc(getOrderStatusDesc(vo.getOrderStatus()));
            }
        }
        return vo;
    }

    @Override
    public void insertWithdrawFreezeFlow(Long merchantBaseId, Long withdrawId, BigDecimal totalAmount) {
        merchantWalletFlowMapper.insertWithdrawFreezeFlow(merchantBaseId, withdrawId, totalAmount);
    }

    @Override
    public MerchantWalletFlow selectMerchantWalletFlowById(Long flowId) {
        return merchantWalletFlowMapper.selectMerchantWalletFlowById(flowId);
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

    @Override
    public Map<String, Object> getFlowSummary(Long merchantBaseId) {
        Map<String, Object> summary = new HashMap<>();

        BigDecimal totalIncome = merchantWalletFlowMapper.sumAmountByType(merchantBaseId, "INCOME");
        BigDecimal totalWithdraw = merchantWalletFlowMapper.sumAmountByType(merchantBaseId, "WITHDRAW_SUCCESS");
        BigDecimal totalRefund = merchantWalletFlowMapper.sumAmountByType(merchantBaseId, "REFUND");

        summary.put("totalIncome", totalIncome != null ? totalIncome :  BigDecimal.ZERO);
        summary.put("totalWithdraw", totalWithdraw != null ?  totalWithdraw. abs() : BigDecimal.ZERO);
        summary.put("totalRefund", totalRefund != null ? totalRefund.abs() : BigDecimal.ZERO);

        return summary;
    }

    // ==================== 辅助方法 ====================

    private String getFlowTypeDesc(String flowType) {
        if (flowType == null) return "";
        switch (flowType) {
            case "INCOME":  return "订单收入";
            case "WITHDRAW_FREEZE": return "提现冻结";
            case "WITHDRAW_SUCCESS": return "提现成功";
            case "WITHDRAW_ROLLBACK": return "提现失败退款";
            case "REFUND": return "订单退款";
            default:  return flowType;
        }
    }

    private String getOrderTypeDesc(Long orderType) {
        if (orderType == null) return "";
        switch (orderType. intValue()) {
            case 1: return "外卖订单";
            case 2: return "跑腿订单";
            case 3: return "二手交易";
            default: return "未知";
        }
    }

    private String getOrderStatusDesc(Long orderStatus) {
        if (orderStatus == null) return "";
        switch (orderStatus.intValue()) {
            case 1: return "待接单";
            case 2: return "待取货";
            case 3: return "配送中";
            case 4: return "已完成";
            case 5: return "已取消";
            default: return "未知";
        }
    }
}