package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.merchant.dto.MerchantGoodsDTO;
import org.springframework.web.multipart.MultipartFile;

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

    String addImage(MultipartFile file, Long goodsId, Long merchantId, Integer isMain);

    String deleteImage(Long goodsId, Long merchantId, Integer isMain,Integer goodsImageId);
}
