package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.merchant.dto.GoodsImageDTO;
import com.ruoyi.platform.merchant.dto.MerchantGoodsDTO;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsMapper {

    @Update("update merchant_goods set status = 1 where merchant_goods_id = #{goodsId}")
    Integer upGoods(Long goodsId);

    @Update("update merchant_goods set status = 0 where merchant_goods_id = #{goodsId}")
    Integer downGoods(Long goodsId);

    MerchantGoodsDTO findGoodById(Long goodsId);

    List<MerchantGoodsDTO> getGoodsList(Long merchantId);

    List<MerchantGoodsDTO> getGoodsListWithMainImage(Long merchantId);

    @Delete("delete from merchant_goods where merchant_goods_id = #{goodsId} and merchant_base_id = #{merchantId}")
    Integer deleteGoods(Long goodsId, Long merchantId);

    Integer updateGoods(Long goodsId, Long merchantId,MerchantGoodsDTO goods);

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


    /**
     * 添加商品
     * @param goods
     * @param merchantId
     * @return
     */
    Integer addGoods(MerchantGoodsDTO goods, Long merchantId);

    /**
     * 查询商品的所有图片（包括主图和其他图片）
     */
    List<GoodsImageDTO> getGoodsImagesByGoodsId(Long goodsId);

    /**
     * 查询商品详情（包含所有图片信息）
     */
    MerchantGoodsDTO getGoodsDetailWithImages(Long goodsId);

    /**
     * 检查商品是否存在且属于指定商家
     */
    @Select("SELECT COUNT(1) FROM merchant_goods WHERE merchant_goods_id = #{goodsId} AND merchant_base_id = #{merchantId}")
    Integer checkGoodsBelongsToMerchant(Long goodsId, Long merchantId);

    @Insert("INSERT INTO merchant_goods_image (image_url, merchant_goods_id, is_main) VALUES (#{imgUrl}, #{goodsId}, #{isMain})")
    int addImage(String imgUrl, Long goodsId, Long merchantId, Integer isMain);
}
