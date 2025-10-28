package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.merchant.dto.MerchantGoodsDTO;
import com.ruoyi.platform.merchant.mapper.GoodsMapper;
import com.ruoyi.platform.merchant.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodsService implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Integer upGoods(Long goodsId) {
        MerchantGoods merchantGoods = goodsMapper.findGoodById(goodsId);
        if (merchantGoods == null){
            throw new RuntimeException("商品不存在");
        }
        if (merchantGoods.getStatus() == 1){
            throw new RuntimeException("商品已上架");
        }
        return goodsMapper.upGoods(goodsId);
    }

    @Override
    public Integer downGoods(Long goodsId) {
        MerchantGoods merchantGoods = goodsMapper.findGoodById(goodsId);
        if (merchantGoods == null){
            throw new RuntimeException("商品不存在");
        }
        if (merchantGoods.getStatus() == 0){
            throw new RuntimeException("商品已下架");
        }
        return goodsMapper.downGoods(goodsId);
    }


    /**
     * 查询商品列表（包含主图URL）- 推荐使用
     */
    @Override
    public List<MerchantGoodsDTO> getGoodsListWithMainImage(Long merchantId) {
        return goodsMapper.getGoodsListWithMainImage(merchantId);
    }


    /**
     * 查询商品列表（原始方法）
     */
    @Override
    public List<MerchantGoodsDTO> getGoodsList(Long merchantId) {
        List<MerchantGoodsDTO> goodsList = goodsMapper.getGoodsList(merchantId);

        // 为每个商品设置主图URL
        if (goodsList != null && !goodsList.isEmpty()) {
            // 获取商品ID列表
            List<Long> goodsIds = goodsList.stream()
                    .map(MerchantGoodsDTO::getMerchantGoodsId)
                    .collect(Collectors.toList());

            // 批量查询主图URL
            List<Map<String, Object>> imageUrls = goodsMapper.getMainImageUrlsByGoodsIds(goodsIds);
            Map<Long, String> imageUrlMap = imageUrls.stream()
                    .collect(Collectors.toMap(
                            map -> Long.valueOf(map.get("goodsId").toString()),
                            map -> map.get("imageUrl").toString()
                    ));

            // 设置主图URL
            for (MerchantGoodsDTO goods : goodsList) {
                String mainImageUrl = imageUrlMap.get(goods.getMerchantGoodsId());
                goods.setMainImageUrl(mainImageUrl);
            }
        }

        return goodsList;
    }

    @Override
    public Integer deleteGoods(Long goodsId, Long merchantId) {
        MerchantGoods merchantGoods = goodsMapper.findGoodById(goodsId);
        if (merchantGoods == null){
            throw new RuntimeException("商品不存在");
        }
        if (merchantGoods.getMerchantBaseId() != merchantId) {
            throw new RuntimeException("商品不属于该商家");
        }
        return goodsMapper.deleteGoods(goodsId,merchantId);
    }

    @Override
    public Integer updateGoods(Long goodsId, Long merchantId) {
        MerchantGoods merchantGoods = goodsMapper.findGoodById(goodsId);
        if (merchantGoods == null){
            throw new RuntimeException("商品不存在");
        }
        if (merchantGoods.getMerchantBaseId() != merchantId) {
            throw new RuntimeException("商品不属于该商家");
        }
        return goodsMapper.updateGoods(goodsId,merchantId);
    }
}
