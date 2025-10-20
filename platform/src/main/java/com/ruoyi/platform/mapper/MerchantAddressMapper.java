package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.MerchantAddress;

/**
 * 商家地址Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface MerchantAddressMapper 
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
     * 删除商家地址
     * 
     * @param merchantAddressId 商家地址主键
     * @return 结果
     */
    public int deleteMerchantAddressByMerchantAddressId(Long merchantAddressId);

    /**
     * 批量删除商家地址
     * 
     * @param merchantAddressIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMerchantAddressByMerchantAddressIds(Long[] merchantAddressIds);
}
