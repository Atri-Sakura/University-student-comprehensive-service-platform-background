package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantBase;

/**
 * 商家基础信息Mapper接口
 * 负责商家基础信息的数据访问
 */
public interface MerchantInfoMapper
{
    /**
     * 根据商家ID查询商家基础信息
     * @param merchantBaseId 商家ID
     * @return 商家基础信息
     */
    MerchantBase selectMerchantBaseByMerchantBaseId(Long merchantBaseId);

    /**
     * 修改商家基础信息
     * @param merchantBase 商家基础信息
     * @return 修改结果（受影响的行数）
     */
    int updateMerchantBase(MerchantBase merchantBase);
}