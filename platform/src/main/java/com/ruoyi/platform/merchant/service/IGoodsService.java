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

    Integer updateGoods(Long goodsId, Long merchantId);
}
