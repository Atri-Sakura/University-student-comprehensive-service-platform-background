package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.MerchantGoodsImageMapper;
import com.ruoyi.platform.domain.MerchantGoodsImage;
import com.ruoyi.platform.service.IMerchantGoodsImageService;

/**
 * 商品图片关联（支持多图展示）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class MerchantGoodsImageServiceImpl implements IMerchantGoodsImageService 
{
    @Autowired
    private MerchantGoodsImageMapper merchantGoodsImageMapper;

    /**
     * 查询商品图片关联（支持多图展示）
     * 
     * @param merchantGoodsImageId 商品图片关联（支持多图展示）主键
     * @return 商品图片关联（支持多图展示）
     */
    @Override
    public MerchantGoodsImage selectMerchantGoodsImageByMerchantGoodsImageId(Long merchantGoodsImageId)
    {
        return merchantGoodsImageMapper.selectMerchantGoodsImageByMerchantGoodsImageId(merchantGoodsImageId);
    }

    /**
     * 查询商品图片关联（支持多图展示）列表
     * 
     * @param merchantGoodsImage 商品图片关联（支持多图展示）
     * @return 商品图片关联（支持多图展示）
     */
    @Override
    public List<MerchantGoodsImage> selectMerchantGoodsImageList(MerchantGoodsImage merchantGoodsImage)
    {
        return merchantGoodsImageMapper.selectMerchantGoodsImageList(merchantGoodsImage);
    }

    /**
     * 新增商品图片关联（支持多图展示）
     * 
     * @param merchantGoodsImage 商品图片关联（支持多图展示）
     * @return 结果
     */
    @Override
    public int insertMerchantGoodsImage(MerchantGoodsImage merchantGoodsImage)
    {
        merchantGoodsImage.setCreateTime(DateUtils.getNowDate());
        return merchantGoodsImageMapper.insertMerchantGoodsImage(merchantGoodsImage);
    }

    /**
     * 修改商品图片关联（支持多图展示）
     * 
     * @param merchantGoodsImage 商品图片关联（支持多图展示）
     * @return 结果
     */
    @Override
    public int updateMerchantGoodsImage(MerchantGoodsImage merchantGoodsImage)
    {
        merchantGoodsImage.setUpdateTime(DateUtils.getNowDate());
        return merchantGoodsImageMapper.updateMerchantGoodsImage(merchantGoodsImage);
    }

    /**
     * 批量删除商品图片关联（支持多图展示）
     * 
     * @param merchantGoodsImageIds 需要删除的商品图片关联（支持多图展示）主键
     * @return 结果
     */
    @Override
    public int deleteMerchantGoodsImageByMerchantGoodsImageIds(Long[] merchantGoodsImageIds)
    {
        return merchantGoodsImageMapper.deleteMerchantGoodsImageByMerchantGoodsImageIds(merchantGoodsImageIds);
    }

    /**
     * 删除商品图片关联（支持多图展示）信息
     * 
     * @param merchantGoodsImageId 商品图片关联（支持多图展示）主键
     * @return 结果
     */
    @Override
    public int deleteMerchantGoodsImageByMerchantGoodsImageId(Long merchantGoodsImageId)
    {
        return merchantGoodsImageMapper.deleteMerchantGoodsImageByMerchantGoodsImageId(merchantGoodsImageId);
    }
}
