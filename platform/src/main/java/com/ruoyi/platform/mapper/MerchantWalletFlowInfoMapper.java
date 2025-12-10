package com.ruoyi.platform.mapper;

import com.ruoyi.platform.domain.MerchantWalletFlow;
import io.lettuce.core.dynamic.annotation.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商家钱包流水Mapper接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface MerchantWalletFlowInfoMapper {

    /**
     * 新增商家钱包流水
     *
     * @param merchantWalletFlow 商家钱包流水
     * @return 影响行数
     */
    int insertMerchantWalletFlow(MerchantWalletFlow merchantWalletFlow);

    /**
     * 查询流水记录
     */
    MerchantWalletFlow selectMerchantWalletFlowById(Long flowId);

    /**
     * 查询流水列表
     */
    List<MerchantWalletFlow> selectMerchantWalletFlowList(MerchantWalletFlow flow);

    /**
     * 按类型汇总金额
     */
    BigDecimal sumAmountByType(@Param("merchantBaseId") Long merchantBaseId,
                               @Param("flowType") String flowType);
}