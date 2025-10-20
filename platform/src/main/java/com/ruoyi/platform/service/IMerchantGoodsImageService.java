package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.MerchantGoodsImage;

/**
 * 商品图片关联（支持多图展示）Service接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IMerchantGoodsImageService 
{
    /**
     * 查询商品图片关联（支持多图展示）
     * 
     * @param merchantGoodsImageId 商品图片关联（支持多图展示）主键
     * @return 商品图片关联（支持多图展示）
     */
    public MerchantGoodsImage selectMerchantGoodsImageByMerchantGoodsImageId(Long merchantGoodsImageId);

    /**
     * 查询商品图片关联（支持多图展示）列表
     * 
     * @param merchantGoodsImage 商品图片关联（支持多图展示）
     * @return 商品图片关联（支持多图展示）集合
     */
    public List<MerchantGoodsImage> selectMerchantGoodsImageList(MerchantGoodsImage merchantGoodsImage);

    /**
     * 新增商品图片关联（支持多图展示）
     * 
     * @param merchantGoodsImage 商品图片关联（支持多图展示）
     * @return 结果
     */
    public int insertMerchantGoodsImage(MerchantGoodsImage merchantGoodsImage);

    /**
     * 修改商品图片关联（支持多图展示）
     * 
     * @param merchantGoodsImage 商品图片关联（支持多图展示）
     * @return 结果
     */
    public int updateMerchantGoodsImage(MerchantGoodsImage merchantGoodsImage);

    /**
     * 批量删除商品图片关联（支持多图展示）
     * 
     * @param merchantGoodsImageIds 需要删除的商品图片关联（支持多图展示）主键集合
     * @return 结果
     */
    public int deleteMerchantGoodsImageByMerchantGoodsImageIds(Long[] merchantGoodsImageIds);

    /**
     * 删除商品图片关联（支持多图展示）信息
     * 
     * @param merchantGoodsImageId 商品图片关联（支持多图展示）主键
     * @return 结果
     */
    public int deleteMerchantGoodsImageByMerchantGoodsImageId(Long merchantGoodsImageId);
}
