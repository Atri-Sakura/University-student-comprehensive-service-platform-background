package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantGoodsImage;

/**
 * 商品图片信息Mapper接口
 */
public interface MerchantGoodsImageInfoMapper {

    /**
     * 查询商品图片信息
     * @param merchantGoodsImageId 图片主键
     * @return 商品图片信息
     */
    MerchantGoodsImage selectMerchantGoodsImageByMerchantGoodsImageId(Long merchantGoodsImageId);

    /**
     * 修改商品图片信息
     * @param merchantGoodsImage 商品图片
     * @return 影响行数
     */
    int updateMerchantGoodsImage(MerchantGoodsImage merchantGoodsImage);
}