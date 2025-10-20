package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.MerchantAddress;

/**
 * 商家地址Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IMerchantAddressService 
{
    /**
     * 查询商家地址
     * 
     * @param merchantAddressId 商家地址主键
     * @return 商家地址
     */
    public MerchantAddress selectMerchantAddressByMerchantAddressId(Long merchantAddressId);

    /**
     * 查询商家地址列表
     * 
     * @param merchantAddress 商家地址
     * @return 商家地址集合
     */
    public List<MerchantAddress> selectMerchantAddressList(MerchantAddress merchantAddress);

    /**
     * 新增商家地址
     * 
     * @param merchantAddress 商家地址
     * @return 结果
     */
    public int insertMerchantAddress(MerchantAddress merchantAddress);

    /**
     * 修改商家地址
     * 
     * @param merchantAddress 商家地址
     * @return 结果
     */
    public int updateMerchantAddress(MerchantAddress merchantAddress);

    /**
     * 批量删除商家地址
     * 
     * @param merchantAddressIds 需要删除的商家地址主键集合
     * @return 结果
     */
    public int deleteMerchantAddressByMerchantAddressIds(Long[] merchantAddressIds);

    /**
     * 删除商家地址信息
     * 
     * @param merchantAddressId 商家地址主键
     * @return 结果
     */
    public int deleteMerchantAddressByMerchantAddressId(Long merchantAddressId);
}
