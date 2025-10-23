package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.GoodsEvaluationImage;

/**
 * 商品评价图片Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface GoodsEvaluationImageMapper 
{
    /**
     * 查询商品评价图片
     * 
     * @param goodsEvaluationImageId 商品评价图片主键
     * @return 商品评价图片
     */
    public GoodsEvaluationImage selectGoodsEvaluationImageByGoodsEvaluationImageId(Long goodsEvaluationImageId);

    /**
     * 查询商品评价图片列表
     * 
     * @param goodsEvaluationImage 商品评价图片
     * @return 商品评价图片集合
     */
    public List<GoodsEvaluationImage> selectGoodsEvaluationImageList(GoodsEvaluationImage goodsEvaluationImage);

    /**
     * 新增商品评价图片
     * 
     * @param goodsEvaluationImage 商品评价图片
     * @return 结果
     */
    public int insertGoodsEvaluationImage(GoodsEvaluationImage goodsEvaluationImage);

    /**
     * 修改商品评价图片
     * 
     * @param goodsEvaluationImage 商品评价图片
     * @return 结果
     */
    public int updateGoodsEvaluationImage(GoodsEvaluationImage goodsEvaluationImage);

    /**
     * 删除商品评价图片
     * 
     * @param goodsEvaluationImageId 商品评价图片主键
     * @return 结果
     */
    public int deleteGoodsEvaluationImageByGoodsEvaluationImageId(Long goodsEvaluationImageId);

    /**
     * 批量删除商品评价图片
     * 
     * @param goodsEvaluationImageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGoodsEvaluationImageByGoodsEvaluationImageIds(Long[] goodsEvaluationImageIds);
}
