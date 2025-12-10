package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantWalletFlow;
import com.ruoyi.platform.domain.vo.MerchantWalletFlowVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商家钱包流水Mapper接口
 *
 * @author Jinx
 * @date 2025-10-24
 */
public interface MerchantWalletFlowMapper {

    /**
     * 查询流水列表（含订单详情）
     */
    List<MerchantWalletFlowVO> selectMerchantWalletFlowListWithOrder(@Param("merchantBaseId") Long merchantBaseId,
                                                                     @Param("flowType") String flowType,
                                                                     @Param("orderNo") String orderNo,
                                                                     @Param("startTime") String startTime,
                                                                     @Param("endTime") String endTime);

    /**
     * 查询流水详情（含订单详情）
     */
    MerchantWalletFlowVO selectMerchantWalletFlowWithOrderById(@Param("flowId") Long flowId,
                                                               @Param("merchantBaseId") Long merchantBaseId);

    /**
     * 查询单条记录
     */
    MerchantWalletFlow selectMerchantWalletFlowById(Long flowId);

    /**
     * 查询流水列表
     */
    List<MerchantWalletFlow> selectMerchantWalletFlowList(MerchantWalletFlow flow);

    /**
     * 插入流水记录
     */
    int insertMerchantWalletFlow(MerchantWalletFlow flow);

    /**
     * 修改流水记录
     */
    int updateMerchantWalletFlow(MerchantWalletFlow flow);

    /**
     * 删除单条流水
     */
    int deleteMerchantWalletFlowById(Long flowId);

    /**
     * 批量删除流水
     */
    int deleteMerchantWalletFlowByIds(Long[] flowIds);

    /**
     * 插入提现冻结流水
     */
    int insertWithdrawFreezeFlow(@Param("merchantBaseId") Long merchantBaseId,
                                 @Param("withdrawId") Long withdrawId,
                                 @Param("totalAmount") BigDecimal totalAmount);

    /**
     * 插入提现成功流水
     */
    int insertWithdrawSuccessFlow(@Param("record") Object record);

    /**
     * 插入提现失败流水
     */
    int insertWithdrawFailFlow(@Param("record") Object record);

    /**
     * 按类型汇总金额
     */
    BigDecimal sumAmountByType(@Param("merchantBaseId") Long merchantBaseId,
                               @Param("flowType") String flowType);
}