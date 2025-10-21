package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.merchant.mapper.MerchantGoodsImageInfoMapper;
import com.ruoyi.platform.domain.MerchantGoodsImage;
import com.ruoyi.platform.merchant.service.IMerchantGoodsImageInfoService;

/**
 * 商品图片信息Service实现
 */
@Service
public class MerchantGoodsImageInfoServiceImpl implements IMerchantGoodsImageInfoService {

    @Autowired
    private MerchantGoodsImageInfoMapper merchantGoodsImageInfoMapper;

    @Override
    public MerchantGoodsImage selectMerchantGoodsImageByMerchantGoodsImageId(Long merchantGoodsImageId) {
        return merchantGoodsImageInfoMapper.selectMerchantGoodsImageByMerchantGoodsImageId(merchantGoodsImageId);
    }

    @Override
    public int updateMerchantGoodsImage(MerchantGoodsImage merchantGoodsImage) {
        merchantGoodsImage.setUpdateTime(DateUtils.getNowDate());
        return merchantGoodsImageInfoMapper.updateMerchantGoodsImage(merchantGoodsImage);
    }
}