package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.domain.MerchantBase;
import org.springframework.web.multipart.MultipartFile;

/**
 * 商家基础信息Service接口
 * 定义商家信息的查询与修改方法
 */
public interface IMerchantInfoService
{
    /**
     * 查询商家基础信息
     * @param merchantBaseId 商家基础信息主键
     * @return 商家基础信息
     */
    MerchantBase selectMerchantBaseByMerchantBaseId(Long merchantBaseId);

    /**
     * 修改商家基础信息
     * @param merchantBase 商家基础信息
     * @return 结果
     */
    int updateMerchantBase(MerchantBase merchantBase);


}