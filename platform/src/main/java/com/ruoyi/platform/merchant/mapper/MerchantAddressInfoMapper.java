package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantAddress;

/**
 * 商家地址Mapper接口
 */
public interface MerchantAddressInfoMapper {

    /**
     * 根据商家ID查询商家地址信息
     * @param merchantBaseId 商家ID
     * @return 商家地址信息
     */
    MerchantAddress selectMerchantAddressByMerchantBaseId(Long merchantBaseId);

    /**
     * 修改商家地址信息
     * @param merchantAddress 商家地址对象
     * @return 影响行数
     */
    int updateMerchantAddress(MerchantAddress merchantAddress);
}