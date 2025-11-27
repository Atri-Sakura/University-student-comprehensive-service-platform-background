package com.ruoyi. platform.rider.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform. domain.vo.RiderEvaluationStatisticsVO;
import org. springframework.beans.factory.annotation. Autowired;
import org. springframework.web.bind.annotation.*;
import com.ruoyi. common.annotation.Log;
import com.ruoyi.common. core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.platform.domain.RiderEvaluation;
import com. ruoyi.platform.service. IRiderEvaluationService;
import com.ruoyi. common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page. TableDataInfo;

/**
 * 骑手-评价管理Controller
 *
 * @author ruoyi
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/rider/evaluation")
public class RiderEvaluationInfoController extends BaseController
{
    @Autowired
    private IRiderEvaluationService riderEvaluationService;

    /**
     * 查询我收到的评价列表
     */
    @GetMapping("/myList")
    public TableDataInfo myList(RiderEvaluation riderEvaluation)
    {
        startPage();
        // 只能查询当前骑手收到的评价
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        riderEvaluation.setRiderBaseId(riderBaseId);
        List<RiderEvaluation> list = riderEvaluationService.selectRiderEvaluationList(riderEvaluation);
        return getDataTable(list);
    }

    /**
     * 导出我收到的评价列表
     */
    @Log(title = "骑手评价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response)
    {
        // 只能导出当前骑手收到的评价
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        RiderEvaluation riderEvaluation = new RiderEvaluation();
        riderEvaluation.setRiderBaseId(riderBaseId);
        List<RiderEvaluation> list = riderEvaluationService.selectRiderEvaluationList(riderEvaluation);
        ExcelUtil<RiderEvaluation> util = new ExcelUtil<>(RiderEvaluation.class);
        util.exportExcel(response, list, "我的评价数据");
    }

    /**
     * 获取评价详细信息
     */
    @GetMapping(value = "/{riderEvaluationId}")
    public AjaxResult getInfo(@PathVariable("riderEvaluationId") Long riderEvaluationId)
    {
        RiderEvaluation evaluation = riderEvaluationService.selectRiderEvaluationByRiderEvaluationId(riderEvaluationId);
        // 验证是否是当前骑手的评价
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        if (evaluation != null && !evaluation.getRiderBaseId().equals(riderBaseId)) {
            return error("无权查看该评价");
        }
        return success(evaluation);
    }

    /**
     * 查询我的评价统计信息
     */
    @GetMapping("/myStatistics")
    public AjaxResult getMyStatistics()
    {
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        RiderEvaluationStatisticsVO statistics = riderEvaluationService. selectRiderEvaluationStatistics(riderBaseId);
        return success(statistics);
    }

    /**
     * 根据评分筛选查询我的评价列表
     * @param filterType 筛选类型(null-全部, 1-好评4-5星, 2-中评2-3星, 3-差评1星)
     */
    @GetMapping("/myListByRating")
    public TableDataInfo myListByRating(Integer filterType)
    {
        startPage();
        // 只能查询当前骑手的评价
        Long riderBaseId = SecurityUtils. getRiderBaseId();
        List<RiderEvaluation> list = riderEvaluationService.selectRiderEvaluationListByRating(riderBaseId, filterType);
        return getDataTable(list);
    }

    /**
     * 根据订单ID查询评价
     */
    @GetMapping("/order/{orderId}")
    public AjaxResult getByOrderId(@PathVariable("orderId") Long orderId)
    {
        RiderEvaluation evaluation = riderEvaluationService.selectRiderEvaluationByOrderId(orderId);
        // 验证是否是当前骑手的评价
        if (evaluation != null) {
            Long riderBaseId = SecurityUtils.getRiderBaseId();
            if (!evaluation.getRiderBaseId().equals(riderBaseId)) {
                return error("无权查看该评价");
            }
        }
        return success(evaluation);
    }

    /**
     * 查询指定骑手的评价统计（可查看其他骑手，用于对比）
     */
    @GetMapping("/statistics/{riderBaseId}")
    public AjaxResult getStatistics(@PathVariable("riderBaseId") Long riderBaseId)
    {
        RiderEvaluationStatisticsVO statistics = riderEvaluationService.selectRiderEvaluationStatistics(riderBaseId);
        return success(statistics);
    }

    /**
     * 查询指定骑手的评价列表（可查看其他骑手，用于对比）
     * @param riderBaseId 骑手ID
     * @param filterType 筛选类型(null-全部, 1-好评4-5星, 2-中评2-3星, 3-差评1星)
     */
    @GetMapping("/listByRating/{riderBaseId}")
    public TableDataInfo listByRating(@PathVariable("riderBaseId") Long riderBaseId, Integer filterType)
    {
        startPage();
        List<RiderEvaluation> list = riderEvaluationService.selectRiderEvaluationListByRating(riderBaseId, filterType);
        return getDataTable(list);
    }
}