package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.merchant.mapper.GoodsMapper;
import com.ruoyi.platform.merchant.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsService implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void upGoods(Long goodsId) {
        MerchantGoods merchantGoods = goodsMapper.findGoodById(goodsId);
        if (merchantGoods == null){
            throw new RuntimeException("商品不存在");
        }
        if (merchantGoods.getStatus() == 1){
            throw new RuntimeException("商品已上架");
        }
        goodsMapper.upGoods(goodsId);
    }

    @Override
    public void downGoods(Long goodsId) {
        MerchantGoods merchantGoods = goodsMapper.findGoodById(goodsId);
        if (merchantGoods == null){
            throw new RuntimeException("商品不存在");
        }
        if (merchantGoods.getStatus() == 0){
            throw new RuntimeException("商品已下架");
        }
        goodsMapper.downGoods(goodsId);
    }
}
