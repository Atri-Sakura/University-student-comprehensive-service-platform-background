package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Update("update merchant_goods set status = 1 where merchant_goods_id = #{goodsId}")
    void upGoods(Long goodsId);

    @Update("update merchant_goods set status = 0 where merchant_goods_id = #{goodsId}")
    void downGoods(Long goodsId);

    @Select("select * from merchant_goods where merchant_goods_id = #{goodsId}")
    MerchantGoods findGoodById(Long goodsId);

    @Select("select * from merchant_goods where merchant_base_id = #{merchantId}")
    List<MerchantGoods> getGoodsList(Long merchantId);
}
