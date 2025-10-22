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
     * 查询门店订单评价列表（按分类）
     * @param merchantBaseId 商家ID
     * @param category 分类(全部:null, 五星:5, 四星:4, 三星及以下:1)
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam Long merchantBaseId,
                           @RequestParam(required = false) Integer category) {
        List<GoodsEvaluation> list = merchantGoodsEvaluationService.getGoodsEvaluationList(merchantBaseId, category);
        return AjaxResult.success(list); // 返回统一对象格式
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