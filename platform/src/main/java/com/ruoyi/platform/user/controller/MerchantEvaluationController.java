package com.ruoyi.platform.user.controller;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.vo.MerchantEvaluationAddReq;
import com.ruoyi.platform.domain.vo.MerchantEvaluationUpdateReq;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.platform.domain.MerchantEvaluation;
import com.ruoyi.platform.service.IMerchantEvaluationService;

/**
 * 商家评价Controller
 *
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/evaluation")
public class MerchantEvaluationController extends BaseController
{
    @Autowired
    private IMerchantEvaluationService merchantEvaluationService;

    /**
     * 查询商家评价列表
     */
    @GetMapping("/list")
    public AjaxResult list(MerchantEvaluation merchantEvaluation)
    {
        List<MerchantEvaluation> list = merchantEvaluationService.selectMerchantEvaluationList(merchantEvaluation);
        return success(list);
    }

    /**
     * 获取商家评价详细信息
     */
    @GetMapping(value = "/{merchantEvaluationId}")
    public AjaxResult getInfo(@PathVariable("merchantEvaluationId") Long merchantEvaluationId)
    {
        return success(merchantEvaluationService.selectMerchantEvaluationByMerchantEvaluationId(merchantEvaluationId));
    }

    /**
     * 用户新增商家评价（支持图片上传）
     * 注意：使用 @ModelAttribute 接收 multipart/form-data 请求
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @ModelAttribute MerchantEvaluationAddReq req)
    {
        // 1. 获取当前登录用户的 userBaseId
        Long userBaseId = SecurityUtils.getUserBaseId();

        // 2. 调用业务逻辑（带权限校验 + 图片上传）
        return toAjax(merchantEvaluationService. insertUserEvaluation(req, userBaseId));
    }

    /**
     * 用户修改商家评价（支持图片处理）
     * 注意：使用 @ModelAttribute 接收 multipart/form-data 请求
     */
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @ModelAttribute MerchantEvaluationUpdateReq req)
    {
        // 1. 获取当前登录用户的 userBaseId
        Long userBaseId = SecurityUtils.getUserBaseId();

        // 2. 调用业务逻辑（带权限校验 + 图片处理）
        return toAjax(merchantEvaluationService.updateUserEvaluation(req, userBaseId));
    }

    /**
     * 用户删除单个商家评价（自动删除关联图片）
     */
    @DeleteMapping("/{merchantEvaluationId}")
    public AjaxResult remove(@PathVariable Long merchantEvaluationId)
    {
        // 1. 获取当前登录用户的 userBaseId
        Long userBaseId = SecurityUtils.getUserBaseId();

        // 2. 调用业务逻辑（带权限校验 + 图片删除）
        return toAjax(merchantEvaluationService.deleteUserEvaluation(merchantEvaluationId, userBaseId));
    }

    /**
     * 用户批量删除商家评价（自动删除关联图片）
     */
    @DeleteMapping("/batch/{merchantEvaluationIds}")
    public AjaxResult removeBatch(@PathVariable Long[] merchantEvaluationIds)
    {
        // 1. 获取当前登录用户的 userBaseId
        Long userBaseId = SecurityUtils.getUserBaseId();

        // 2. 调用业务逻辑（带权限校验 + 图片删除）
        return toAjax(merchantEvaluationService.deleteUserEvaluationBatch(merchantEvaluationIds, userBaseId));
    }
}