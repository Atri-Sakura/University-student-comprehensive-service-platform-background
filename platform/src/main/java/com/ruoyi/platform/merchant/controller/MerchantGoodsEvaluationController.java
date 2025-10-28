package com.ruoyi.platform.merchant.controller;

import com.ruoyi.platform.domain.GoodsEvaluation;
import com.ruoyi.platform.domain.vo.GoodsEvaluationDetailVO;
import com.ruoyi.platform.domain.GoodsEvaluationImage;
import com.ruoyi.platform.mapper.GoodsEvaluationImageMapper;
import com.ruoyi.platform.mapper.MerchantGoodsImageMapper;
import com.ruoyi.platform.merchant.service.IMerchantGoodsEvaluationService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchant/evaluation")
public class MerchantGoodsEvaluationController {

    @Autowired
    private IMerchantGoodsEvaluationService merchantGoodsEvaluationService;

    @Autowired
    private MerchantGoodsImageMapper merchantGoodsImageMapper;

    /**
     * 查询门店订单评价列表（按分类和是否有图）
     * 仅限当前登录商家
     * @param category 分类(全部:null, 五星:5, 四星:4, 三星及以下:1)
     * @param hasImage 是否有图（可为空，true-有图，false-无图，不传则全部）
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(required = false) Integer category,
                           @RequestParam(required = false) Boolean hasImage) {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        List<GoodsEvaluation> list = merchantGoodsEvaluationService.getGoodsEvaluationList(merchantBaseId, category, hasImage);
        return AjaxResult.success(list);
    }

    @GetMapping("/detail/{goodsEvaluationId}")
    public AjaxResult detail(@PathVariable Long goodsEvaluationId) {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        GoodsEvaluation evaluation = merchantGoodsEvaluationService.getGoodsEvaluationById(goodsEvaluationId);
        if (evaluation == null || !evaluation.getMerchantBaseId().equals(merchantBaseId)) {
            return AjaxResult.error("无权查看该评价");
        }
        // 查询图片列表
        List<GoodsEvaluationImage> images = merchantGoodsImageMapper.selectImagesByGoodsEvaluationId(goodsEvaluationId);
        // 组合返回结果
        return AjaxResult.success(new GoodsEvaluationDetailVO(evaluation, images));
    }

    /**
     * 商家回复订单评价
     * 仅限当前登录商家
     */
    @PostMapping("/reply")
    public AjaxResult reply(@RequestParam Long goodsEvaluationId,
                            @RequestParam String merchantReply) {
        // 查询评价并校验归属
        GoodsEvaluation evaluation = merchantGoodsEvaluationService.getGoodsEvaluationById(goodsEvaluationId);
        if (evaluation == null || !evaluation.getMerchantBaseId().equals(SecurityUtils.getMerchantBaseId())) {
            return AjaxResult.error("无权操作该评价");
        }
        int result = merchantGoodsEvaluationService.replyGoodsEvaluation(goodsEvaluationId, merchantReply);
        if (result > 0) {
            return AjaxResult.success("回复成功");
        } else {
            return AjaxResult.error("回复失败");
        }
    }
}