package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.MerchantAddressMapper;
import com.ruoyi.platform.domain.MerchantAddress;
import com.ruoyi.platform.service.IMerchantAddressService;

/**
 * 商家地址Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class MerchantAddressServiceImpl implements IMerchantAddressService 
{
    @Autowired
    private MerchantAddressMapper merchantAddressMapper;

    /**
     * 查询商家地址
     * 
     * @param merchantAddressId 商家地址主键
     * @return 商家地址
     */
    @Override
    public MerchantAddress selectMerchantAddressByMerchantAddressId(Long merchantAddressId)
    {
        return merchantAddressMapper.selectMerchantAddressByMerchantAddressId(merchantAddressId);
    }

    /**
     * 查询商家地址列表
     * 
     * @param merchantAddress 商家地址
     * @return 商家地址
     */
    @Override
    public List<MerchantAddress> selectMerchantAddressList(MerchantAddress merchantAddress)
    {
        return merchantAddressMapper.selectMerchantAddressList(merchantAddress);
    }

    /**
     * 新增商家地址
     * 
     * @param merchantAddress 商家地址
     * @return 结果
     */
    @Override
    public int insertMerchantAddress(MerchantAddress merchantAddress)
    {
        merchantAddress.setCreateTime(DateUtils.getNowDate());
        return merchantAddressMapper.insertMerchantAddress(merchantAddress);
    }

    /**
     * 修改商家地址
     * 
     * @param merchantAddress 商家地址
     * @return 结果
     */
    @Override
    public int updateMerchantAddress(MerchantAddress merchantAddress)
    {
        merchantAddress.setUpdateTime(DateUtils.getNowDate());
        return merchantAddressMapper.updateMerchantAddress(merchantAddress);
    }

    /**
     * 批量删除商家地址
     * 
     * @param merchantAddressIds 需要删除的商家地址主键
     * @return 结果
     */
    @Override
    public int deleteMerchantAddressByMerchantAddressIds(Long[] merchantAddressIds)
    {
        return merchantAddressMapper.deleteMerchantAddressByMerchantAddressIds(merchantAddressIds);
    }

    /**
     * 删除商家地址信息
     * 
     * @param merchantAddressId 商家地址主键
     * @return 结果
     */
    @Override
    public int deleteMerchantAddressByMerchantAddressId(Long merchantAddressId)
    {
        return merchantAddressMapper.deleteMerchantAddressByMerchantAddressId(merchantAddressId);
    }
}
