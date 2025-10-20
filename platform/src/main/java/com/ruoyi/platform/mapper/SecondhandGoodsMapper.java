package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.SecondhandGoods;

/**
 * 二手商品(简化版)Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface SecondhandGoodsMapper 
{
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
     * 删除二手商品(简化版)
     * 
     * @param secondhandGoodsId 二手商品(简化版)主键
     * @return 结果
     */
    public int deleteSecondhandGoodsBySecondhandGoodsId(Long secondhandGoodsId);

    /**
     * 批量删除二手商品(简化版)
     * 
     * @param secondhandGoodsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSecondhandGoodsBySecondhandGoodsIds(Long[] secondhandGoodsIds);
}
