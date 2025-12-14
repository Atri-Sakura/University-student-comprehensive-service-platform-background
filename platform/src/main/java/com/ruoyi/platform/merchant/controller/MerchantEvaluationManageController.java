package com.ruoyi.platform.merchant.controller;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.service.IMerchantEvaluationService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商家评价管理Controller（商家端）
 *
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/merchant/evaluation/manage")
public class MerchantEvaluationManageController extends BaseController {

    @Autowired
    private IMerchantEvaluationService merchantEvaluationService;

    /**
     * 查询评价列表（带分页和高级筛选）
     *
     * @param req 查询条件
     * @return 评价列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MerchantEvaluationQueryReq req) {
        // 1. 获取当前登录商家ID
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();

        // 3. 查询评价列表
        List<MerchantEvaluationDetailVO> list = merchantEvaluationService
                .getMerchantEvaluationList(req, merchantBaseId);

        // 4. 返回分页结果
        return getDataTable(list);
    }

    /**
     * 查询评价统计信息
     *
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public AjaxResult getStatistics() {
        // 1. 获取当前登录商家ID
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();

        // 2. 查询统计信息
        MerchantEvaluationStatisticsVO statistics = merchantEvaluationService
                .getEvaluationStatistics(merchantBaseId);

        // 3. 返回结果
        return success(statistics);
    }

    /**
     * 回复评价
     *
     * @param req 回复请求
     * @return 结果
     */
    @PostMapping("/reply")
    public AjaxResult reply(@Validated @RequestBody MerchantEvaluationReplyReq req) {
        // 1. 获取当前登录商家ID
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();

        // 2. 执行回复
        return toAjax(merchantEvaluationService.replyEvaluation(req, merchantBaseId));
    }

    /**
     * 获取评价详情
     *
     * @param merchantEvaluationId 评价ID
     * @return 评价详情
     */
    @GetMapping("/{merchantEvaluationId}")
    public AjaxResult getDetail(@PathVariable("merchantEvaluationId") Long merchantEvaluationId) {
        // 1. 获取当前登录商家ID
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();

        // 2. 查询评价详情
        MerchantEvaluationDetailVO detail = merchantEvaluationService
                .getEvaluationDetail(merchantEvaluationId, merchantBaseId);

        // 3. 返回结果
        return success(detail);
    }
}