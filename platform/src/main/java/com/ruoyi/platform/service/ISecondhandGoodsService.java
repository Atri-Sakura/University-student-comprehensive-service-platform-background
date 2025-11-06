package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.SecondhandGoods;
import com.ruoyi.platform.domain.vo.SecondhandGoodDetailVO;
import com.ruoyi.platform.domain.vo.SecondhandGoodsListVO;

/**
 * 二手商品(简化版)Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface ISecondhandGoodsService
{
    /**
     * 查询已发布二手商品的详情信息
     * @param goodsId
     */
    public SecondhandGoodDetailVO getSecondhandGoodsDetail(Long goodsId);
    /**
     * 查询二手商品(简化版)
     * 
     * @param secondhandGoodsId 二手商品(简化版)主键
     * @return 二手商品(简化版)
     */
    public SecondhandGoods selectSecondhandGoodsBySecondhandGoodsId(Long secondhandGoodsId);

    /**
     * 查询二手商品(简化版)列表
     * 
     * @param secondhandGoods 二手商品(简化版)
     * @return 二手商品(简化版)集合
     */
    public List<SecondhandGoods> selectSecondhandGoodsList(SecondhandGoods secondhandGoods);

    /**
     * 新增二手商品(简化版)
     * 
     * @param secondhandGoods 二手商品(简化版)
     * @return 结果
     */
    public int insertSecondhandGoods(SecondhandGoods secondhandGoods);

    /**
     * 修改二手商品(简化版)
     * 
     * @param secondhandGoods 二手商品(简化版)
     * @return 结果
     */
    public int updateSecondhandGoods(SecondhandGoods secondhandGoods);

    /**
     * 批量删除二手商品(简化版)
     * 
     * @param secondhandGoodsIds 需要删除的二手商品(简化版)主键集合
     * @return 结果
     */
    public int deleteSecondhandGoodsBySecondhandGoodsIds(Long[] secondhandGoodsIds);

    /**
     * 删除二手商品(简化版)信息
     * 
     * @param secondhandGoodsId 二手商品(简化版)主键
     * @return 结果
     */
    public int deleteSecondhandGoodsBySecondhandGoodsId(Long secondhandGoodsId);

    /**
     * 根据商品类别查询发布的二手商品列表
     * @param category
     * @return
     */
    public List<SecondhandGoodsListVO> selectPublishedSecondhandGoodsListByCategory(String category);
}
