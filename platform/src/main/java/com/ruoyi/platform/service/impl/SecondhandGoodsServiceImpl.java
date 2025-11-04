package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.platform.domain.vo.SecondhandGoodsListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.SecondhandGoodsMapper;
import com.ruoyi.platform.domain.SecondhandGoods;
import com.ruoyi.platform.service.ISecondhandGoodsService;

/**
 * 二手商品(简化版)Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class SecondhandGoodsServiceImpl implements ISecondhandGoodsService 
{
    @Autowired
    private SecondhandGoodsMapper secondhandGoodsMapper;


    /**
     * 查询二手商品(简化版)
     * 
     * @param secondhandGoodsId 二手商品(简化版)主键
     * @return 二手商品(简化版)
     */
    @Override
    public SecondhandGoods selectSecondhandGoodsBySecondhandGoodsId(Long secondhandGoodsId)
    {
        return secondhandGoodsMapper.selectSecondhandGoodsBySecondhandGoodsId(secondhandGoodsId);
    }

    /**
     * 查询二手商品(简化版)列表
     * 
     * @param secondhandGoods 二手商品(简化版)
     * @return 二手商品(简化版)
     */
    @Override
    public List<SecondhandGoods> selectSecondhandGoodsList(SecondhandGoods secondhandGoods)
    {
        return secondhandGoodsMapper.selectSecondhandGoodsList(secondhandGoods);
    }

    /**
     * 新增二手商品(简化版)
     * 
     * @param secondhandGoods 二手商品(简化版)
     * @return 结果
     */
    @Override
    public int insertSecondhandGoods(SecondhandGoods secondhandGoods)
    {
        secondhandGoods.setCreateTime(DateUtils.getNowDate());
        return secondhandGoodsMapper.insertSecondhandGoods(secondhandGoods);
    }

    /**
     * 修改二手商品(简化版)
     * 
     * @param secondhandGoods 二手商品(简化版)
     * @return 结果
     */
    @Override
    public int updateSecondhandGoods(SecondhandGoods secondhandGoods)
    {
        secondhandGoods.setUpdateTime(DateUtils.getNowDate());
        return secondhandGoodsMapper.updateSecondhandGoods(secondhandGoods);
    }

    /**
     * 批量删除二手商品(简化版)
     * 
     * @param secondhandGoodsIds 需要删除的二手商品(简化版)主键
     * @return 结果
     */
    @Override
    public int deleteSecondhandGoodsBySecondhandGoodsIds(Long[] secondhandGoodsIds)
    {
        return secondhandGoodsMapper.deleteSecondhandGoodsBySecondhandGoodsIds(secondhandGoodsIds);
    }

    /**
     * 删除二手商品(简化版)信息
     * 
     * @param secondhandGoodsId 二手商品(简化版)主键
     * @return 结果
     */
    @Override
    public int deleteSecondhandGoodsBySecondhandGoodsId(Long secondhandGoodsId)
    {
        return secondhandGoodsMapper.deleteSecondhandGoodsBySecondhandGoodsId(secondhandGoodsId);
    }

    /**
     * 根据商品分类查询已发布的二手商品
     *
     * @param category 二手商品(简化版)主键
     * @return 结果
     */
    public List<SecondhandGoodsListVO> selectPublishedSecondhandGoodsListByCategory(String category) {
        return secondhandGoodsMapper.selectSecondhandGoodsListByCategory(category);
    }
}
