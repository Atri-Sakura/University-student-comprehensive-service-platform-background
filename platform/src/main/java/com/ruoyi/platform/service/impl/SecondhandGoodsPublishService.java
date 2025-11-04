package com.ruoyi.platform.service.impl;

import com.ruoyi.common.utils.file.MinioFileUtils;
import com.ruoyi.platform.domain.SecondhandGoods;
import com.ruoyi.platform.domain.SecondhandGoodsImage;
import com.ruoyi.platform.service.ISecondhandGoodsImageService;
import com.ruoyi.platform.service.ISecondhandGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecondhandGoodsPublishService {

    private final ISecondhandGoodsService secondhandGoodsService;
    private final ISecondhandGoodsImageService secondhandGoodsImageService;
    private final MinioFileUtils minioFileUtils;

    @Transactional(rollbackFor = Exception.class)
    public void publishGood(Long userId, String goodsName,
                            BigDecimal price, String category, String description, MultipartFile[] files) throws  Exception {
        // Step 1️⃣ 插入商品信息
        SecondhandGoods goods = new SecondhandGoods();
        goods.setUserBaseId(userId);
        goods.setGoodsName(goodsName);
        goods.setCategory(category);
        goods.setPrice(price);
        goods.setDescription(description);
        goods.setStatus(1L);
        secondhandGoodsService.insertSecondhandGoods(goods);

        // Step 2️⃣ 上传图片并插入图片信息
        List<SecondhandGoodsImage> imageList = new ArrayList<>();
        Long sortOrder = 0L;
        for (MultipartFile file : files) {
            // 若上传失败抛出异常，事务会整体回滚
            String imageUrl = minioFileUtils.upload(file, "secondhand", userId);

            SecondhandGoodsImage image = new SecondhandGoodsImage();
            image.setSecondhandGoodsId(goods.getSecondhandGoodsId());
            image.setImageUrl(imageUrl);
            image.setIsMain(sortOrder == 0L ? 1L : 0L);
            image.setSortOrder(sortOrder++);
            imageList.add(image);
        }

        secondhandGoodsImageService.insertSecondhandGoodsImages(imageList);
    }
}
