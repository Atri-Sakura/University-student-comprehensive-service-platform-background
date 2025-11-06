package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.SecondhandGoodsImageMapper;
import com.ruoyi.platform.domain.SecondhandGoodsImage;
import com.ruoyi.platform.service.ISecondhandGoodsImageService;

/**
 * 二手商品图片(支持1-9张图片)Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class SecondhandGoodsImageServiceImpl implements ISecondhandGoodsImageService 
{
    @Autowired
    private SecondhandGoodsImageMapper secondhandGoodsImageMapper;

    /**
     * 查询二手商品图片(支持1-9张图片)
     * 
     * @param secondhandGoodsImageId 二手商品图片(支持1-9张图片)主键
     * @return 二手商品图片(支持1-9张图片)
     */
    @Override
    public SecondhandGoodsImage selectSecondhandGoodsImageBySecondhandGoodsImageId(Long secondhandGoodsImageId)
    {
        return secondhandGoodsImageMapper.selectSecondhandGoodsImageBySecondhandGoodsImageId(secondhandGoodsImageId);
    }

    /**
     * 查询二手商品图片(支持1-9张图片)列表
     * 
     * @param secondhandGoodsImage 二手商品图片(支持1-9张图片)
     * @return 二手商品图片(支持1-9张图片)
     */
    @Override
    public List<SecondhandGoodsImage> selectSecondhandGoodsImageList(SecondhandGoodsImage secondhandGoodsImage)
    {
        return secondhandGoodsImageMapper.selectSecondhandGoodsImageList(secondhandGoodsImage);
    }

    /**
     * 新增二手商品图片(支持1-9张图片)
     * 
     * @param secondhandGoodsImage 二手商品图片(支持1-9张图片)
     * @return 结果
     */
    @Override
    public int insertSecondhandGoodsImage(SecondhandGoodsImage secondhandGoodsImage)
    {
        secondhandGoodsImage.setCreateTime(DateUtils.getNowDate());
        return secondhandGoodsImageMapper.insertSecondhandGoodsImage(secondhandGoodsImage);
    }

    /**
     * 修改二手商品图片(支持1-9张图片)
     * 
     * @param secondhandGoodsImage 二手商品图片(支持1-9张图片)
     * @return 结果
     */
    @Override
    public int updateSecondhandGoodsImage(SecondhandGoodsImage secondhandGoodsImage)
    {
        return secondhandGoodsImageMapper.updateSecondhandGoodsImage(secondhandGoodsImage);
    }

    /**
     * 批量删除二手商品图片(支持1-9张图片)
     * 
     * @param secondhandGoodsImageIds 需要删除的二手商品图片(支持1-9张图片)主键
     * @return 结果
     */
    @Override
    public int deleteSecondhandGoodsImageBySecondhandGoodsImageIds(Long[] secondhandGoodsImageIds)
    {
        return secondhandGoodsImageMapper.deleteSecondhandGoodsImageBySecondhandGoodsImageIds(secondhandGoodsImageIds);
    }

    /**
     * 删除二手商品图片(支持1-9张图片)信息
     * 
     * @param secondhandGoodsImageId 二手商品图片(支持1-9张图片)主键
     * @return 结果
     */
    @Override
    public int deleteSecondhandGoodsImageBySecondhandGoodsImageId(Long secondhandGoodsImageId)
    {
        return secondhandGoodsImageMapper.deleteSecondhandGoodsImageBySecondhandGoodsImageId(secondhandGoodsImageId);
    }

    /**
     * 批量插入二手商品图片
     * @param imageList
     * @return
     */
    @Override
    public int insertSecondhandGoodsImages(List<SecondhandGoodsImage> imageList) {
        if (imageList == null || imageList.isEmpty()) {
            return 0;
        }
        return secondhandGoodsImageMapper.insertSecondhandGoodsImages(imageList);
    }
}
