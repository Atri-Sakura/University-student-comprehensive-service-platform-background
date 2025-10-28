package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.merchant.dto.MerchantGoodsDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsMapper {

    @Update("update merchant_goods set status = 1 where merchant_goods_id = #{goodsId}")
    Integer upGoods(Long goodsId);

    @Update("update merchant_goods set status = 0 where merchant_goods_id = #{goodsId}")
    Integer downGoods(Long goodsId);

    MerchantGoods findGoodById(Long goodsId);

    List<MerchantGoodsDTO> getGoodsList(Long merchantId);

    List<MerchantGoodsDTO> getGoodsListWithMainImage(Long merchantId);

    @Delete("delete from merchant_goods where merchant_goods_id = #{goodsId} and merchant_base_id = #{merchantId}")
    Integer deleteGoods(Long goodsId, Long merchantId);

    Integer updateGoods(Long goodsId, Long merchantId);

    /**
     * 更新商品状态
     */
    Integer updateGoodsStatus(Long merchantGoodsId, Integer status);

    /**
     * 更新商品库存
     */
    Integer updateGoodsStock(Long merchantGoodsId, Integer stock);

    /**
     * 增加商品销量
     */
    Integer increaseSalesCount(Long merchantGoodsId, Long count);

    /**
     * 减少商品库存
     */
    Integer decreaseStock(Long merchantGoodsId, Integer count);

    /**
     * 更新商品评分信息
     */
    Integer updateGoodsRating(Long merchantGoodsId, BigDecimal avgRating, Integer ratingCount, BigDecimal fiveStarRate, BigDecimal fourStarRate, BigDecimal threeStarRate, BigDecimal twoStarRate, BigDecimal oneStarRate);

    /**
     * 批量更新商品状态
     */
    Integer batchUpdateStatus(List<Long> goodsIds, Integer status);

    /**
     * 根据商品ID查询主图URL
     */
    String getMainImageUrlByGoodsId(Long merchantGoodsId);

    /**
     * 批量查询商品主图URL
     */
    List<Map<String, Object>> getMainImageUrlsByGoodsIds(List<Long> goodsIds);


}
