package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.merchant.service.IMerchantAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant/analytics")
public class MerchantAnalyticsController {

    @Autowired
    private IMerchantAnalyticsService merchantAnalyticsService;

    // 商家订单量 营业额等数据接口
    @GetMapping("/sales")
    public AjaxResult getSalesData() {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        return AjaxResult.success(merchantAnalyticsService.getSalesData(merchantBaseId));
    }

    // 商家评价数据接口
    @GetMapping("/ratings")
    public AjaxResult getRatingsData() {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        return AjaxResult.success(merchantAnalyticsService.getRatingsData(merchantBaseId));
    }

    // 商家热销商品排行榜
    @GetMapping("/topGoods")
    public AjaxResult getTopGoods() {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        return AjaxResult.success(merchantAnalyticsService.getTopGoods(merchantBaseId));
    }
}
