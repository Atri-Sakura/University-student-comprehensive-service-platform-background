package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.merchant.mapper.MerchantAddressInfoMapper;
import com.ruoyi.platform.domain.MerchantAddress;
import com.ruoyi.platform.merchant.service.IMerchantAddressInfoService;

/**
 * 商家地址信息Service实现
 */
@Service
public class MerchantAddressInfoServiceImpl implements IMerchantAddressInfoService {

    @Autowired
    private MerchantAddressInfoMapper merchantAddressInfoMapper;

    @Override
    public MerchantAddress selectMerchantAddressByMerchantBaseId(Long merchantBaseId) {
        return merchantAddressInfoMapper.selectMerchantAddressByMerchantBaseId(merchantBaseId);
    }

    @Override
    public int updateMerchantAddress(MerchantAddress merchantAddress) {
        merchantAddress.setUpdateTime(DateUtils.getNowDate());
        return merchantAddressInfoMapper.updateMerchantAddress(merchantAddress);
    }
}