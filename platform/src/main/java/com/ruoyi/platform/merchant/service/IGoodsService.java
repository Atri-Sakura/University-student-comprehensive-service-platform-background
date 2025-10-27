package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.domain.MerchantGoods;

import java.util.List;

public interface IGoodsService {

    void upGoods(Long goodsId);

    void downGoods(Long goodsId);

    List<MerchantGoods> getGoodsList(Long merchantId);
}
