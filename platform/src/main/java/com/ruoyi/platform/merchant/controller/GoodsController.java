package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.merchant.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
