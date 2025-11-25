package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.common.utils.file.MinioFileUtils;
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.merchant.dto.MerchantGoodsDTO;
import com.ruoyi.platform.merchant.mapper.GoodsMapper;
import com.ruoyi.platform.merchant.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoodsServiceImpl implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private MinioFileUtils minioFileUtils;

    @Override
    public Integer upGoods(Long goodsId) {
        MerchantGoodsDTO merchantGoods = goodsMapper.findGoodById(goodsId);
        if (merchantGoods == null){
            throw new RuntimeException("商品不存在");
        }
        if (merchantGoods.getStatus() == 1){
            throw new RuntimeException("商品已上架");
        }
        return goodsMapper.upGoods(goodsId);
    }

    @Override
    public Integer downGoods(Long goodsId) {
        MerchantGoodsDTO merchantGoods = goodsMapper.findGoodById(goodsId);
        if (merchantGoods == null){
            throw new RuntimeException("商品不存在");
        }
        if (merchantGoods.getStatus() == 0){
            throw new RuntimeException("商品已下架");
        }
        return goodsMapper.downGoods(goodsId);
    }


    /**
     * 查询商品列表（包含主图URL）- 推荐使用
     */
    @Override
    public List<MerchantGoodsDTO> getGoodsListWithMainImage(Long merchantId) {
        return goodsMapper.getGoodsListWithMainImage(merchantId);
    }


    /**
     * 查询商品列表（原始方法）
     */
    @Override
    public List<MerchantGoodsDTO> getGoodsList(Long merchantId) {
        List<MerchantGoodsDTO> goodsList = goodsMapper.getGoodsList(merchantId);

        // 为每个商品设置主图URL
        if (goodsList != null && !goodsList.isEmpty()) {
            // 获取商品ID列表
            List<Long> goodsIds = goodsList.stream()
                    .map(MerchantGoodsDTO::getMerchantGoodsId)
                    .collect(Collectors.toList());

            // 批量查询主图URL
            List<Map<String, Object>> imageUrls = goodsMapper.getMainImageUrlsByGoodsIds(goodsIds);
            Map<Long, String> imageUrlMap = imageUrls.stream()
                    .collect(Collectors.toMap(
                            map -> Long.valueOf(map.get("goodsId").toString()),
                            map -> map.get("imageUrl").toString()
                    ));

            // 设置主图URL
            for (MerchantGoodsDTO goods : goodsList) {
                String mainImageUrl = imageUrlMap.get(goods.getMerchantGoodsId());
                goods.setMainImageUrl(mainImageUrl);
            }
        }

        return goodsList;
    }

    @Override
    public Integer deleteGoods(Long goodsId, Long merchantId) {
        MerchantGoodsDTO merchantGoods = goodsMapper.findGoodById(goodsId);
        if (merchantGoods == null){
            throw new RuntimeException("商品不存在");
        }
        if (!merchantGoods.getMerchantBaseId().equals(merchantId)) {
            throw new RuntimeException("商品不属于该商家");
        }
        return goodsMapper.deleteGoods(goodsId,merchantId);
    }

    @Override
    public Integer updateGoods(Long goodsId, Long merchantId,MerchantGoodsDTO goods) {
        MerchantGoodsDTO merchantGoods = goodsMapper.findGoodById(goodsId);
        if (merchantGoods == null){
            throw new RuntimeException("商品不存在");
        }
        if (!merchantGoods.getMerchantBaseId().equals(merchantId)) {
            throw new RuntimeException("商品不属于该商家");
        }
        return goodsMapper.updateGoods(goodsId,merchantId,goods);
    }

    @Override
    public Integer addGoods(MerchantGoodsDTO goods, Long merchantId) {
        goods.setMerchantBaseId(merchantId);
        return goodsMapper.addGoods(goods,merchantId);
    }

    @Override
    public MerchantGoodsDTO getGoodsDetail(Long goodsId, Long merchantId) {
        // 验证商品权限
        Integer count = goodsMapper.checkGoodsBelongsToMerchant(goodsId, merchantId);
        if (count == null || count == 0) {
            throw new RuntimeException("商品不存在或不属于该商家");
        }

        // 查询商品详情（包含图片列表）
        MerchantGoodsDTO goodsDetail = goodsMapper.getGoodsDetailWithImages(goodsId);

        if (goodsDetail == null) {
            throw new RuntimeException("商品不存在");
        }

        return goodsDetail;
    }

    @Override
    public String addImage(MultipartFile file, Long goodsId, Long merchantId, Integer isMain) {
        String imgUrl = null;
        try {
            // 1. 先上传图片到MinIO
            imgUrl = minioFileUtils.upload(file, "merchantgood", merchantId);

            // 2. 插入数据库记录
            int affectedRows = goodsMapper.addImage(imgUrl, goodsId, merchantId, isMain);

            if (affectedRows == 0) {
                // 数据库插入失败，删除已上传的图片
                minioFileUtils.deleteByUrl(imgUrl);
                return "database_error";
            }

            return imgUrl;
        } catch (Exception e) {
            // 发生异常，清理已上传的图片
            if (imgUrl != null) {
                try {
                    minioFileUtils.deleteByUrl(imgUrl);
                } catch (Exception deleteException) {
                    log.error("清理图片失败: {}", imgUrl, deleteException);
                }
            }
            log.error("添加图片失败", e);
            return "error";
        }
    }

    @Override
    public String deleteImage(Long goodsId, Long merchantId, Integer isMain,Integer goodsImageId) {
        String imgUrl = null;
        try {
            imgUrl = goodsMapper.getGoodsImagesByGoodsImageId(goodsImageId);
            minioFileUtils.deleteByUrl(imgUrl);
            int rows = goodsMapper.deleteImage(goodsImageId);
            int count = 0;
            while (rows == 0 ){
                minioFileUtils.deleteByUrl(imgUrl);
                rows = goodsMapper.deleteImage(goodsImageId);
                count++;
                if (count > 10){
                    return "删除失败，请检查数据格式是否正确";
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
