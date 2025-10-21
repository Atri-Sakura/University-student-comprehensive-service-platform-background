package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.merchant.mapper.MerchantGoodsInfoMapper;
import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.merchant.service.IMerchantGoodsInfoService;

/**
 * 商品基础信息Service实现
 */
@Service
public class MerchantGoodsInfoServiceImpl implements IMerchantGoodsInfoService {

    @Autowired
    private MerchantGoodsInfoMapper merchantGoodsInfoMapper;

    @Override
    public MerchantGoods selectMerchantGoodsByMerchantGoodsId(Long merchantGoodsId) {
        return merchantGoodsInfoMapper.selectMerchantGoodsByMerchantGoodsId(merchantGoodsId);
    }

    @Override
    public int updateMerchantGoods(MerchantGoods merchantGoods) {
        merchantGoods.setUpdateTime(DateUtils.getNowDate());
        return merchantGoodsInfoMapper.updateMerchantGoods(merchantGoods);
    }
}