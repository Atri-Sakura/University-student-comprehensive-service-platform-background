package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.merchant.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IGoodsService goodsService;

    //商品上下架管理
    @PutMapping("/up/{goodsId}")
    public AjaxResult upGoods(@PathVariable Long goodsId){
        goodsService.upGoods(goodsId);
        return AjaxResult.success();
    }
    @PutMapping("/down/{goodsId}")
    public AjaxResult downGoods(@PathVariable Long goodsId){
        goodsService.downGoods(goodsId);
        return AjaxResult.success();
    }

    //查询商家的全部商品
    @GetMapping("/list")
    public AjaxResult getGoods(){
        Long merchantId = SecurityUtils.getMerchantBaseId();
        List<MerchantGoods> merchantGoodsList = goodsService.getGoodsList(merchantId);
        return AjaxResult.success();
    }

    //某个商家删除某个商品
    @DeleteMapping("/delete/{goodsId}")
    public AjaxResult deleteGoods(@PathVariable Long goodsId){
        return AjaxResult.success();
    }

    //某个商家修改某个商品
    @PutMapping("/update/{goodsId}")
    public AjaxResult updateGoods(@PathVariable Long goodsId){
        return AjaxResult.success();
    }
}
