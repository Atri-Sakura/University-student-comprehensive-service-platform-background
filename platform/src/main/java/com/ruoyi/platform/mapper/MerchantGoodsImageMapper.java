package com.ruoyi.platform.mapper;

import java.util.List;

import com.ruoyi.platform.domain.GoodsEvaluationImage;
import com.ruoyi.platform.domain.MerchantGoodsImage;
import org.apache.ibatis.annotations.Select;

/**
 * 商品图片关联（支持多图展示）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface MerchantGoodsImageMapper 
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
     * 删除商品图片关联（支持多图展示）
     * 
     * @param merchantGoodsImageId 商品图片关联（支持多图展示）主键
     * @return 结果
     */
    public int deleteMerchantGoodsImageByMerchantGoodsImageId(Long merchantGoodsImageId);

    /**
     * 批量删除商品图片关联（支持多图展示）
     * 
     * @param merchantGoodsImageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMerchantGoodsImageByMerchantGoodsImageIds(Long[] merchantGoodsImageIds);

    List<GoodsEvaluationImage> selectImagesByGoodsEvaluationId(Long goodsEvaluationId);

    /**
     * 查询商品主图
     * @param goodsId 商品ID
     * @return 主图信息
     */
    @Select("SELECT * FROM merchant_goods_image WHERE merchant_goods_id = #{goodsId} AND is_main = 1 LIMIT 1")
    MerchantGoodsImage selectMainImageByGoodsId(Long goodsId);
}
