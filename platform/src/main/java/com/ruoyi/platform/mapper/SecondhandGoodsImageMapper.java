package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.SecondhandGoodsImage;
import com.ruoyi.platform.domain.vo.SecondhandGoodsListVO;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 二手商品图片(支持1-9张图片)Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface SecondhandGoodsImageMapper
{
    /**
     * 批量插入二手商品图片
     * @param imageList 图片列表
     * @return
     */
    public int insertSecondhandGoodsImages(@Param("list") List<SecondhandGoodsImage> imageList);

    /**
     * 查询二手商品图片(支持1-9张图片)
     * 
     * @param secondhandGoodsImageId 二手商品图片(支持1-9张图片)主键
     * @return 二手商品图片(支持1-9张图片)
     */
    public SecondhandGoodsImage selectSecondhandGoodsImageBySecondhandGoodsImageId(Long secondhandGoodsImageId);

    /**
     * 查询二手商品图片(支持1-9张图片)列表
     * 
     * @param secondhandGoodsImage 二手商品图片(支持1-9张图片)
     * @return 二手商品图片(支持1-9张图片)集合
     */
    public List<SecondhandGoodsImage> selectSecondhandGoodsImageList(SecondhandGoodsImage secondhandGoodsImage);

    /**
     * 新增二手商品图片(支持1-9张图片)
     * 
     * @param secondhandGoodsImage 二手商品图片(支持1-9张图片)
     * @return 结果
     */
    public int insertSecondhandGoodsImage(SecondhandGoodsImage secondhandGoodsImage);

    /**
     * 修改二手商品图片(支持1-9张图片)
     * 
     * @param secondhandGoodsImage 二手商品图片(支持1-9张图片)
     * @return 结果
     */
    public int updateSecondhandGoodsImage(SecondhandGoodsImage secondhandGoodsImage);

    /**
     * 删除二手商品图片(支持1-9张图片)
     * 
     * @param secondhandGoodsImageId 二手商品图片(支持1-9张图片)主键
     * @return 结果
     */
    public int deleteSecondhandGoodsImageBySecondhandGoodsImageId(Long secondhandGoodsImageId);

    /**
     * 批量删除二手商品图片(支持1-9张图片)
     * 
     * @param secondhandGoodsImageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSecondhandGoodsImageBySecondhandGoodsImageIds(Long[] secondhandGoodsImageIds);


}
