package com.ruoyi.platform.user.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.domain.MerchantGoodsImage;
import com.ruoyi.platform.merchant.service.IMerchantGoodsInfoService;
import com.ruoyi.platform.merchant.service.IMerchantGoodsImageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 顾客端商品及商品图片信息控制器
 * 仅支持商品和图片的公开查询，不允许修改
 */
@RestController
@RequestMapping("/customer/goods")
public class CustomerGoodsInfoController {

    @Autowired
    private IMerchantGoodsInfoService merchantGoodsInfoService;

    @Autowired
    private IMerchantGoodsImageInfoService merchantGoodsImageInfoService;

    /**
     * 查询商品信息【顾客端】
     * 可展示已上架商品信息
     * @param merchantGoodsId 商品ID
     * @return 商品信息
     */
    @GetMapping("/base/{merchantGoodsId}")
    public AjaxResult getGoodsInfoForCustomer(@PathVariable Long merchantGoodsId) {
        MerchantGoods goods = merchantGoodsInfoService.selectMerchantGoodsByMerchantGoodsId(merchantGoodsId);
        if (goods == null) {
            return AjaxResult.error("商品不存在");
        }
        // 只允许展示已上架商品
        if (goods.getStatus() == null || goods.getStatus() != 1L) {
            return AjaxResult.error("商品未上架，无法展示");
        }
        // 可以根据业务需要，过滤掉一些商家端才展示的字段
        return AjaxResult.success(goods);
    }

    /**
     * 查询商品图片信息【顾客端】
     * 可展示商品图片
     * @param merchantGoodsImageId 商品图片ID
     * @return 图片信息
     */
    @GetMapping("/image/{merchantGoodsImageId}")
    public AjaxResult getGoodsImageForCustomer(@PathVariable Long merchantGoodsImageId) {
        MerchantGoodsImage image = merchantGoodsImageInfoService.selectMerchantGoodsImageByMerchantGoodsImageId(merchantGoodsImageId);
        if (image == null) {
            return AjaxResult.error("图片不存在");
        }
        // 可见图片简单判断，只要URL不为空
        if (image.getImageUrl() == null || image.getImageUrl().trim().isEmpty()) {
            return AjaxResult.error("图片不可展示");
        }
        return AjaxResult.success(image);
    }

    /**
     * 顾客端分页查询商品列表（可按商家筛选，可支持分类等扩展）
     * @param merchantBaseId 商家ID（可选，不传为全部商家）
     * @return 商品列表（仅展示已上架商品）
     */
    @GetMapping("/list")
    public AjaxResult listGoodsForCustomer(@RequestParam(required = false) Long merchantBaseId) {
        List<MerchantGoods> goodsList = merchantGoodsInfoService.selectMerchantGoodsListForCustomer(merchantBaseId, 1L, null, null);
        return AjaxResult.success(goodsList);
    }
}