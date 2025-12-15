package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.domain.MerchantGoodsImage;
import com.ruoyi.platform.merchant.service.IMerchantGoodsInfoService;
import com.ruoyi.platform.merchant.service.IMerchantGoodsImageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品及商品图片信息控制器
 * 支持 merchant_goods 和 merchant_goods_image 基础信息的查询与修改
 */
@RestController
@RequestMapping("/merchant/goods")
public class MerchantGoodsInfoController {

    @Autowired
    private IMerchantGoodsInfoService merchantGoodsInfoService;

    @Autowired
    private IMerchantGoodsImageInfoService merchantGoodsImageInfoService;

    /**
     * 查询商品基础信息（仅限当前登录商家）
     * @param merchantGoodsId 商品ID
     * @return 商品信息
     */
    @GetMapping("/base/{merchantGoodsId}")
    public AjaxResult getMerchantGoodsInfo(@PathVariable Long merchantGoodsId) {
        MerchantGoods goods = merchantGoodsInfoService.selectMerchantGoodsByMerchantGoodsId(merchantGoodsId);
        if (goods == null || !goods.getMerchantBaseId().equals(SecurityUtils.getMerchantBaseId())) {
            return AjaxResult.error("无权访问该商品信息");
        }
        return AjaxResult.success(goods);
    }

    /**
     * 统计商品销量
     * @param merchantGoodsId
     * @return
     */
    @GetMapping("/base/monthly/sales/{merchantGoodsId}")
    public AjaxResult countMerchantGoodsSales(@PathVariable Long merchantGoodsId) {
        MerchantGoods merchantGoods = merchantGoodsInfoService.selectMerchantGoodsByMerchantGoodsId(merchantGoodsId);
        int goods = merchantGoodsInfoService.getMonthlySaleCounts(merchantGoodsId);
        return AjaxResult.success("查询成功",goods);
    }



    /**
     * 修改商品基础信息（仅限当前登录商家）
     * @param merchantGoods 商品信息对象
     * @return 操作结果
     */
    @PutMapping("/base")
    public AjaxResult updateMerchantGoods(@RequestBody MerchantGoods merchantGoods) {
        if (merchantGoods == null || merchantGoods.getMerchantGoodsId() == null) {
            return AjaxResult.error("参数错误，缺少商品ID");
        }
        MerchantGoods dbGoods = merchantGoodsInfoService.selectMerchantGoodsByMerchantGoodsId(merchantGoods.getMerchantGoodsId());
        if (dbGoods == null || !dbGoods.getMerchantBaseId().equals(SecurityUtils.getMerchantBaseId())) {
            return AjaxResult.error("无权修改该商品信息");
        }
        merchantGoods.setMerchantBaseId(SecurityUtils.getMerchantBaseId());
        int result = merchantGoodsInfoService.updateMerchantGoods(merchantGoods);
        if (result > 0) {
            return AjaxResult.success("商品信息修改成功");
        }
        return AjaxResult.error("商品信息修改失败");
    }

    /**
     * 查询商品图片信息（仅限当前登录商家）
     * @param merchantGoodsImageId 商品图片ID
     * @return 商品图片信息
     */
    @GetMapping("/image/{merchantGoodsImageId}")
    public AjaxResult getMerchantGoodsImage(@PathVariable Long merchantGoodsImageId) {
        MerchantGoodsImage image = merchantGoodsImageInfoService.selectMerchantGoodsImageByMerchantGoodsImageId(merchantGoodsImageId);
        if (image == null || !image.getMerchantBaseId().equals(SecurityUtils.getMerchantBaseId())) {
            return AjaxResult.error("无权访问该商品图片信息");
        }
        return AjaxResult.success(image);
    }

    /**
     * 修改商品图片信息（仅限当前登录商家）
     * @param merchantGoodsImage 商品图片对象
     * @return 操作结果
     */
    @PutMapping("/image")
    public AjaxResult updateMerchantGoodsImage(@RequestBody MerchantGoodsImage merchantGoodsImage) {
        if (merchantGoodsImage == null || merchantGoodsImage.getMerchantGoodsImageId() == null) {
            return AjaxResult.error("参数错误，缺少图片ID");
        }
        MerchantGoodsImage dbImage = merchantGoodsImageInfoService.selectMerchantGoodsImageByMerchantGoodsImageId(merchantGoodsImage.getMerchantGoodsImageId());
        if (dbImage == null || !dbImage.getMerchantBaseId().equals(SecurityUtils.getMerchantBaseId())) {
            return AjaxResult.error("无权修改该商品图片信息");
        }
        merchantGoodsImage.setMerchantBaseId(SecurityUtils.getMerchantBaseId());
        int result = merchantGoodsImageInfoService.updateMerchantGoodsImage(merchantGoodsImage);
        if (result > 0) {
            return AjaxResult.success("商品图片修改成功");
        }
        return AjaxResult.error("商品图片修改失败");
    }
}