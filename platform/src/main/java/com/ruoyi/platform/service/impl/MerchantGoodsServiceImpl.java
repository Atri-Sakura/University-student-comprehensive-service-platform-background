package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.MerchantGoodsMapper;
import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.service.IMerchantGoodsService;

/**
 * 商品Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class MerchantGoodsServiceImpl implements IMerchantGoodsService 
{
    @Autowired
    private MerchantGoodsMapper merchantGoodsMapper;

    /**
     * 查询商品
     * 
     * @param merchantGoodsId 商品主键
     * @return 商品
     */
    @Override
    public MerchantGoods selectMerchantGoodsByMerchantGoodsId(Long merchantGoodsId)
    {
        return merchantGoodsMapper.selectMerchantGoodsByMerchantGoodsId(merchantGoodsId);
    }

    /**
     * 查询商品列表
     * 
     * @param merchantGoods 商品
     * @return 商品
     */
    @Override
    public List<MerchantGoods> selectMerchantGoodsList(MerchantGoods merchantGoods)
    {
        return merchantGoodsMapper.selectMerchantGoodsList(merchantGoods);
    }

    /**
     * 新增商品
     * 
     * @param merchantGoods 商品
     * @return 结果
     */
    @Override
    public int insertMerchantGoods(MerchantGoods merchantGoods)
    {
        merchantGoods.setCreateTime(DateUtils.getNowDate());
        return merchantGoodsMapper.insertMerchantGoods(merchantGoods);
    }

    /**
     * 修改商品
     * 
     * @param merchantGoods 商品
     * @return 结果
     */
    @Override
    public int updateMerchantGoods(MerchantGoods merchantGoods)
    {
        merchantGoods.setUpdateTime(DateUtils.getNowDate());
        return merchantGoodsMapper.updateMerchantGoods(merchantGoods);
    }

    /**
     * 批量删除商品
     * 
     * @param merchantGoodsIds 需要删除的商品主键
     * @return 结果
     */
    @Override
    public int deleteMerchantGoodsByMerchantGoodsIds(Long[] merchantGoodsIds)
    {
        return merchantGoodsMapper.deleteMerchantGoodsByMerchantGoodsIds(merchantGoodsIds);
    }

    /**
     * 删除商品信息
     * 
     * @param merchantGoodsId 商品主键
     * @return 结果
     */
    @Override
    public int deleteMerchantGoodsByMerchantGoodsId(Long merchantGoodsId)
    {
        return merchantGoodsMapper.deleteMerchantGoodsByMerchantGoodsId(merchantGoodsId);
    }
}
