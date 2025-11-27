package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.domain.MerchantAddress;

/**
 * 商家地址Service接口
 */
public interface IMerchantAddressInfoService {

    /**
     * 根据商家ID查询商家地址信息
     * @param merchantBaseId 商家ID
     * @return 商家地址信息
     */
    MerchantAddress selectMerchantAddressByMerchantBaseId(Long merchantBaseId);

    /**
     * 新增商家地址信息
     * @param merchantAddress 商家地址信息
     * @return 影响行数
     */
    int insertMerchantAddress(MerchantAddress merchantAddress);

    /**
     * 修改商家地址信息
     * @param merchantAddress 商家地址信息
     * @return 影响行数
     */
    int updateMerchantAddress(MerchantAddress merchantAddress);
}