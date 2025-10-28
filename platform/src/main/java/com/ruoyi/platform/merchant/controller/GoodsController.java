package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.merchant.dto.MerchantGoodsDTO;
import com.ruoyi.platform.merchant.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController extends BaseController {

    @Autowired
    private IGoodsService goodsService;

    //商品上下架管理
    @PutMapping("/up/{goodsId}")
    public AjaxResult upGoods(@PathVariable Long goodsId){
        Integer result = goodsService.upGoods(goodsId);
        return result > 0 ? AjaxResult.success("上架成功") : AjaxResult.error("上架失败");
    }
    @PutMapping("/down/{goodsId}")
    public AjaxResult downGoods(@PathVariable Long goodsId){
        Integer result = goodsService.downGoods(goodsId);
        return result > 0 ? AjaxResult.success("下架成功") : AjaxResult.error("下架失败");
    }

    //查询商家的全部商品(包含主图url)
    @GetMapping("/list")
    public TableDataInfo getGoodsList(){
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null){
            return null;
        }
//        System.out.println(merchantId);
        startPage();
//        第一种查询方法
//        List<MerchantGoodsDTO> merchantGoodsList = goodsService.getGoodsList(merchantId);
//        第二种查询方法 二选一就行
        List<MerchantGoodsDTO> merchantGoodsList = goodsService.getGoodsListWithMainImage(merchantId);
        System.out.println(merchantGoodsList);
        return getDataTable(merchantGoodsList);
    }

    //某个商家删除某个商品
    @DeleteMapping("/delete/{goodsId}")
    public AjaxResult deleteGoods(@PathVariable Long goodsId){
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null){
            return null;
        }
        Integer result = goodsService.deleteGoods(goodsId,merchantId);
        return result > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    //某个商家修改某个商品
    @PutMapping("/update/{goodsId}")
    public AjaxResult updateGoods(@PathVariable Long goodsId,@RequestBody MerchantGoods goods){
        Long merchantId = SecurityUtils.getMerchantBaseId();
        if (merchantId == null){
            return null;
        }
        Integer result = goodsService.updateGoods(goodsId,merchantId);
        return result > 0 ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
    }
}
