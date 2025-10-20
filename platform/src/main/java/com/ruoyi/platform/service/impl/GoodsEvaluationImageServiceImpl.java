package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.GoodsEvaluationImageMapper;
import com.ruoyi.platform.domain.GoodsEvaluationImage;
import com.ruoyi.platform.service.IGoodsEvaluationImageService;

/**
 * 商品评价图片Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class GoodsEvaluationImageServiceImpl implements IGoodsEvaluationImageService 
{
    @Autowired
    private GoodsEvaluationImageMapper goodsEvaluationImageMapper;

    /**
     * 查询商品评价图片
     * 
     * @param goodsEvaluationImageId 商品评价图片主键
     * @return 商品评价图片
     */
    @Override
    public GoodsEvaluationImage selectGoodsEvaluationImageByGoodsEvaluationImageId(Long goodsEvaluationImageId)
    {
        return goodsEvaluationImageMapper.selectGoodsEvaluationImageByGoodsEvaluationImageId(goodsEvaluationImageId);
    }

    /**
     * 查询商品评价图片列表
     * 
     * @param goodsEvaluationImage 商品评价图片
     * @return 商品评价图片
     */
    @Override
    public List<GoodsEvaluationImage> selectGoodsEvaluationImageList(GoodsEvaluationImage goodsEvaluationImage)
    {
        return goodsEvaluationImageMapper.selectGoodsEvaluationImageList(goodsEvaluationImage);
    }

    /**
     * 新增商品评价图片
     * 
     * @param goodsEvaluationImage 商品评价图片
     * @return 结果
     */
    @Override
    public int insertGoodsEvaluationImage(GoodsEvaluationImage goodsEvaluationImage)
    {
        goodsEvaluationImage.setCreateTime(DateUtils.getNowDate());
        return goodsEvaluationImageMapper.insertGoodsEvaluationImage(goodsEvaluationImage);
    }

    /**
     * 修改商品评价图片
     * 
     * @param goodsEvaluationImage 商品评价图片
     * @return 结果
     */
    @Override
    public int updateGoodsEvaluationImage(GoodsEvaluationImage goodsEvaluationImage)
    {
        return goodsEvaluationImageMapper.updateGoodsEvaluationImage(goodsEvaluationImage);
    }

    /**
     * 批量删除商品评价图片
     * 
     * @param goodsEvaluationImageIds 需要删除的商品评价图片主键
     * @return 结果
     */
    @Override
    public int deleteGoodsEvaluationImageByGoodsEvaluationImageIds(Long[] goodsEvaluationImageIds)
    {
        return goodsEvaluationImageMapper.deleteGoodsEvaluationImageByGoodsEvaluationImageIds(goodsEvaluationImageIds);
    }

    /**
     * 删除商品评价图片信息
     * 
     * @param goodsEvaluationImageId 商品评价图片主键
     * @return 结果
     */
    @Override
    public int deleteGoodsEvaluationImageByGoodsEvaluationImageId(Long goodsEvaluationImageId)
    {
        return goodsEvaluationImageMapper.deleteGoodsEvaluationImageByGoodsEvaluationImageId(goodsEvaluationImageId);
    }
}
