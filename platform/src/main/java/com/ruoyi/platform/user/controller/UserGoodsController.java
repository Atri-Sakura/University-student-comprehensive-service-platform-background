package com.ruoyi.platform.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.domain.MerchantGoodsImage;
import com.ruoyi.platform.domain.vo.GoodsListVO;
import com.ruoyi.platform.service.IMerchantBaseService;
import com.ruoyi.platform.service.IMerchantGoodsService;
import com.ruoyi.platform.service.IMerchantGoodsImageService;

/**
 * 用户端商品查询Controller (精简版)
 */
@RestController
@RequestMapping("/user/goods")
public class UserGoodsController extends BaseController {

    @Autowired
    private IMerchantGoodsService merchantGoodsService;

    @Autowired
    private IMerchantGoodsImageService merchantGoodsImageService;

    @Autowired
    private IMerchantBaseService merchantBaseService;

    /**
     * 用户端:查询某个商家的商品列表(精简字段)
     */
    @GetMapping("/merchant/{merchantBaseId}")
    public TableDataInfo listByMerchant(
            @PathVariable("merchantBaseId") Long merchantBaseId,
            @RequestParam(required = false) String category) {

        // 校验商家
        MerchantBase merchant = merchantBaseService.selectMerchantBaseByMerchantBaseId(merchantBaseId);
        if (merchant == null || merchant.getAuditStatus() != 1L || merchant.getBusinessStatus() != 1L) {
            return getDataTable(List.of());
        }

        startPage();

        MerchantGoods query = new MerchantGoods();
        query.setMerchantBaseId(merchantBaseId);
        query.setStatus(1L);
        if (category != null && !category.isEmpty()) {
            query.setCategory(category);
        }

        List<MerchantGoods> list = merchantGoodsService.selectMerchantGoodsList(query);

        // 转换为VO,并附带主图
        List<GoodsListVO> voList = list.stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return getDataTable(voList);
    }

    /**
     * 用户端:获取商品详细信息(含图片)
     */
    @GetMapping("/{merchantGoodsId}")
    public AjaxResult getInfo(@PathVariable("merchantGoodsId") Long merchantGoodsId) {

        MerchantGoods goods = merchantGoodsService.selectMerchantGoodsByMerchantGoodsId(merchantGoodsId);

        if (goods == null) {
            return AjaxResult.error("商品不存在");
        }

        if (goods.getStatus() != 1L) {
            return AjaxResult.error("该商品已下架");
        }

        // 查询图片
        MerchantGoodsImage imageQuery = new MerchantGoodsImage();
        imageQuery.setMerchantGoodsId(merchantGoodsId);
        List<MerchantGoodsImage> images = merchantGoodsImageService.selectMerchantGoodsImageList(imageQuery);

        // 按排序
        images = images.stream()
                .sorted((a, b) -> {
                    if (a.getIsMain() != null && a.getIsMain() == 1L) return -1;
                    if (b.getIsMain() != null && b.getIsMain() == 1L) return 1;
                    long sortA = a.getSortOrder() != null ? a.getSortOrder() : 999;
                    long sortB = b.getSortOrder() != null ? b.getSortOrder() : 999;
                    return Long.compare(sortA, sortB);
                })
                .collect(Collectors.toList());

        // 组装返回
        Map<String, Object> result = new HashMap<>();
        result.put("merchantGoodsId", goods.getMerchantGoodsId());
        result.put("goodsName", goods.getGoodsName());
        result.put("category", goods.getCategory());
        result.put("price", goods.getPrice());
        result.put("originalPrice", goods.getOriginalPrice());
        result.put("stock", goods.getStock());
        result.put("salesCount", goods.getSalesCount());
        result.put("description", goods.getDescription());
        result.put("avgRating", goods.getAvgRating());
        result.put("ratingCount", goods.getRatingCount());
        result.put("images", images);

        return success(result);
    }

    /**
     * 搜索商家的商品
     */
    @GetMapping("/merchant/{merchantBaseId}/search")
    public TableDataInfo search(
            @PathVariable("merchantBaseId") Long merchantBaseId,
            @RequestParam("keyword") String keyword) {

        startPage();

        MerchantGoods query = new MerchantGoods();
        query.setMerchantBaseId(merchantBaseId);
        query.setGoodsName(keyword);
        query.setStatus(1L);

        List<MerchantGoods> list = merchantGoodsService.selectMerchantGoodsList(query);

        List<GoodsListVO> voList = list.stream()
                .map(this::convertToListVO)
                .collect(Collectors.toList());

        return getDataTable(voList);
    }

    /**
     * 转换为商品列表VO并获取主图
     */
    private GoodsListVO convertToListVO(MerchantGoods goods) {
        GoodsListVO vo = new GoodsListVO();
        BeanUtils.copyProperties(goods, vo);

        // 查询主图
        MerchantGoodsImage imageQuery = new MerchantGoodsImage();
        imageQuery.setMerchantGoodsId(goods.getMerchantGoodsId());
        imageQuery.setIsMain(1L);
        List<MerchantGoodsImage> images = merchantGoodsImageService.selectMerchantGoodsImageList(imageQuery);

        if (!images.isEmpty()) {
            vo.setMainImageUrl(images.get(0).getImageUrl());
        }

        return vo;
    }
}