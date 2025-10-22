package com.ruoyi.platform.merchant.controller;

import com.ruoyi.platform.domain.GoodsEvaluation;
import com.ruoyi.platform.merchant.service.IMerchantGoodsEvaluationService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchant/evaluation")
public class MerchantGoodsEvaluationController {

    @Autowired
    private IMerchantGoodsEvaluationService merchantGoodsEvaluationService;

    /**
     * 查询门店订单评价列表（按分类和是否有图）
     * @param merchantBaseId 商家ID
     * @param category 分类(全部:null, 五星:5, 四星:4, 三星及以下:1)
     * @param hasImage 是否有图（可为空，true-有图，false-无图，不传则全部）
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam Long merchantBaseId,
                           @RequestParam(required = false) Integer category,
                           @RequestParam(required = false) Boolean hasImage) {
        List<GoodsEvaluation> list = merchantGoodsEvaluationService.getGoodsEvaluationList(merchantBaseId, category, hasImage);
        return AjaxResult.success(list);
    }

    /**
     * 商家回复订单评价
     */
    @PostMapping("/reply")
    public AjaxResult reply(@RequestParam Long goodsEvaluationId,
                            @RequestParam String merchantReply) {
        int result = merchantGoodsEvaluationService.replyGoodsEvaluation(goodsEvaluationId, merchantReply);
        if (result > 0) {
            return AjaxResult.success("回复成功");
        } else {
            return AjaxResult.error("回复失败");
        }
    }
}