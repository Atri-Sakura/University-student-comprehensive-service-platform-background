package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.merchant.dto.MerchantGoodsDTO;

import java.util.List;

public interface IGoodsService {

    Integer upGoods(Long goodsId);

    Integer downGoods(Long goodsId);

    List<MerchantGoodsDTO> getGoodsList(Long merchantId);

    List<MerchantGoodsDTO> getGoodsListWithMainImage(Long merchantId);

    Integer deleteGoods(Long goodsId, Long merchantId);

    Integer updateGoods(Long goodsId, Long merchantId,MerchantGoodsDTO goods);

    Integer addGoods(MerchantGoodsDTO goods, Long merchantId);

    MerchantGoodsDTO getGoodsDetail(Long goodsId, Long merchantId);

    Integer addImage(Long goodsId, Long merchantId, String url);

    Integer deleteImage(Long goodsId, Long merchantId, String url);
}
