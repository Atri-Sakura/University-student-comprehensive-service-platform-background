package com.ruoyi.platform.mapper;

import com.ruoyi.platform.domain.MerchantWalletFlow;

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
}