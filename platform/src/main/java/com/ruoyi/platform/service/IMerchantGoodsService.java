package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.MerchantGoods;

/**
 * 商品Service接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IMerchantGoodsService 
{
    /**
     * 查询商品
     * 
     * @param merchantGoodsId 商品主键
     * @return 商品
     */
    public MerchantGoods selectMerchantGoodsByMerchantGoodsId(Long merchantGoodsId);

    /**
     * 查询商品列表
     * 
     * @param merchantGoods 商品
     * @return 商品集合
     */
    public List<MerchantGoods> selectMerchantGoodsList(MerchantGoods merchantGoods);

    /**
     * 新增商品
     * 
     * @param merchantGoods 商品
     * @return 结果
     */
    public int insertMerchantGoods(MerchantGoods merchantGoods);

    /**
     * 修改商品
     * 
     * @param merchantGoods 商品
     * @return 结果
     */
    public int updateMerchantGoods(MerchantGoods merchantGoods);

    /**
     * 批量删除商品
     * 
     * @param merchantGoodsIds 需要删除的商品主键集合
     * @return 结果
     */
    public int deleteMerchantGoodsByMerchantGoodsIds(Long[] merchantGoodsIds);

    /**
     * 删除商品信息
     * 
     * @param merchantGoodsId 商品主键
     * @return 结果
     */
    public int deleteMerchantGoodsByMerchantGoodsId(Long merchantGoodsId);
}
