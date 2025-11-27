package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com. ruoyi.common.utils. uuid.IdUtils;
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
        return merchantAddressInfoMapper. selectMerchantAddressByMerchantBaseId(merchantBaseId);
    }

    @Override
    public int insertMerchantAddress(MerchantAddress merchantAddress) {
        // 生成地址ID（使用与 AuthServiceImpl 相同的 UUID 转 Long 方案）
        if (merchantAddress.getMerchantAddressId() == null) {
            merchantAddress.setMerchantAddressId(generateLongId());
        }
        merchantAddress.setCreateTime(DateUtils.getNowDate());
        merchantAddress.setUpdateTime(DateUtils.getNowDate());
        return merchantAddressInfoMapper.insertMerchantAddress(merchantAddress);
    }

    @Override
    public int updateMerchantAddress(MerchantAddress merchantAddress) {
        merchantAddress. setUpdateTime(DateUtils.getNowDate());
        return merchantAddressInfoMapper.updateMerchantAddress(merchantAddress);
    }

    /**
     * 生成Long类型的唯一ID（压缩UUID方案）
     * 取UUID的前8位16进制字符串转换为Long
     * 生成的ID约为10-12位数字
     *
     * @return Long类型的唯一ID
     */
    private Long generateLongId() {
        String uuid = IdUtils.fastSimpleUUID();
        String hexString = uuid.substring(0, 8); // 取前8位16进制
        try {
            // 将8位16进制字符串转换为Long（最大值为 4294967295，10位数字）
            return Long.parseLong(hexString, 16);
        } catch (NumberFormatException e) {
            // 如果转换失败，使用时间戳作为备用方案
            return System.currentTimeMillis();
        }
    }
}