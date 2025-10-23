package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.domain.MerchantGoods;

/**
 * 商品基础信息Service接口
 */
public interface IMerchantGoodsInfoService {

    /**
     * 查询商品基础信息
     * @param merchantGoodsId 商品主键
     * @return 商品信息
     */
    MerchantGoods selectMerchantGoodsByMerchantGoodsId(Long merchantGoodsId);

    /**
     * 修改商品基础信息
     * @param merchantGoods 商品信息
     * @return 影响行数
     */
    int updateMerchantGoods(MerchantGoods merchantGoods);
}