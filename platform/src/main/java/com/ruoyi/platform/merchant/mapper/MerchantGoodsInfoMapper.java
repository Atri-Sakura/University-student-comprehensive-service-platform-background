package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantGoods;

import java.util.List;

/**
 * 商品基础信息Mapper接口
 */
public interface MerchantGoodsInfoMapper {

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

    /**
     * 查询商品列表（支持多条件）
     * @param merchantGoods 查询条件
     * @return 商品列表
     */
    List<MerchantGoods> selectMerchantGoodsList(MerchantGoods merchantGoods);
}