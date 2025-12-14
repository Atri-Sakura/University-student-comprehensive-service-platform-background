package com.ruoyi.platform.user.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.dto.RiderEvaluationDTO;
import com.ruoyi.platform.domain.vo.RiderEvaluationStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common. core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.platform.domain.RiderEvaluation;
import com.ruoyi.platform.service.IRiderEvaluationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户-骑手评价Controller
 *
 * @author ruoyi
 * @date 2025-11-26
 */
@RestController
@RequestMapping("/user/riderEvaluation")
public class UserRiderEvaluationController extends BaseController
{
    @Autowired
    private IRiderEvaluationService riderEvaluationService;

    /**
     * 查询我的骑手评价列表
     */
    @GetMapping("/myList")
    public TableDataInfo myList(RiderEvaluation riderEvaluation)
    {
        // 只能查询当前用户的评价
//        Long userId = SecurityUtils.getUserBaseId();
//        riderEvaluation.setUserId(userId);
        List<RiderEvaluation> list = riderEvaluationService.selectRiderEvaluationList(riderEvaluation);
        return getDataTable(list);
    }

    /**
     * 获取骑手评价详细信息
     */
    @GetMapping(value = "/{riderEvaluationId}")
    public AjaxResult getInfo(@PathVariable("riderEvaluationId") Long riderEvaluationId)
    {
        RiderEvaluation evaluation = riderEvaluationService.selectRiderEvaluationByRiderEvaluationId(riderEvaluationId);
        // 验证是否是当前用户的评价
        Long userId = SecurityUtils.getUserBaseId();
        if (evaluation != null && ! evaluation.getUserId().equals(userId)) {
            return error("无权查看该评价");
        }
        return success(evaluation);
    }

    /**
     * 新增骑手评价
     */
    @Log(title = "骑手评价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody RiderEvaluationDTO evaluationDTO)
    {
        // 使用当前登录用户ID
        Long userId = SecurityUtils.getUserBaseId();
        return toAjax(riderEvaluationService.insertRiderEvaluation(evaluationDTO, userId));
    }

    /**
     * 修改骑手评价
     */
    @Log(title = "骑手评价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiderEvaluation riderEvaluation)
    {
        // 验证是否是当前用户的评价
        Long userId = SecurityUtils.getUserBaseId();
        RiderEvaluation existEvaluation = riderEvaluationService.selectRiderEvaluationByRiderEvaluationId(riderEvaluation.getRiderEvaluationId());
        if (existEvaluation == null) {
            return error("评价不存在");
        }
        if (!existEvaluation.getUserId().equals(userId)) {
            return error("无权修改该评价");
        }
        return toAjax(riderEvaluationService.updateRiderEvaluation(riderEvaluation));
    }

    /**
     * 删除骑手评价
     */
    @Log(title = "骑手评价", businessType = BusinessType.DELETE)
    @DeleteMapping("/{riderEvaluationIds}")
    public AjaxResult remove(@PathVariable Long[] riderEvaluationIds)
    {
        // 验证是否是当前用户的评价
        Long userId = SecurityUtils.getUserBaseId();
        for (Long evaluationId : riderEvaluationIds) {
            RiderEvaluation evaluation = riderEvaluationService.selectRiderEvaluationByRiderEvaluationId(evaluationId);
            if (evaluation == null) {
                return error("评价ID:" + evaluationId + " 不存在");
            }
            if (!evaluation.getUserId().equals(userId)) {
                return error("无权删除评价ID:" + evaluationId);
            }
        }
        return toAjax(riderEvaluationService.deleteRiderEvaluationByRiderEvaluationIds(riderEvaluationIds));
    }

    /**
     * 根据订单ID查询我的评价
     */
    @GetMapping("/order/{orderId}")
    public AjaxResult getByOrderId(@PathVariable("orderId") Long orderId)
    {
        RiderEvaluation evaluation = riderEvaluationService.selectRiderEvaluationByOrderId(orderId);
        // 验证是否是当前用户的评价
        if (evaluation != null) {
            Long userId = SecurityUtils.getUserBaseId();
            if (!evaluation.getUserId().equals(userId)) {
                return error("无权查看该评价");
            }
        }
        return success(evaluation);
    }

    /**
     * 查询骑手评价统计信息（公开信息，用户可查看任意骑手的评价统计）
     */
    @GetMapping("/statistics/{riderBaseId}")
    public AjaxResult getStatistics(@PathVariable("riderBaseId") Long riderBaseId)
    {
        RiderEvaluationStatisticsVO statistics = riderEvaluationService. selectRiderEvaluationStatistics(riderBaseId);
        return success(statistics);
    }

    /**
     * 根据评分筛选查询骑手评价列表（公开信息，用户可查看任意骑手的评价列表）
     * @param riderBaseId 骑手ID
     * @param filterType 筛选类型(null-全部, 1-好评4-5星, 2-中评2-3星, 3-差评1星)
     */
    @GetMapping("/listByRating/{riderBaseId}")
    public TableDataInfo listByRating(@PathVariable("riderBaseId") Long riderBaseId, Integer filterType)
    {
        List<RiderEvaluation> list = riderEvaluationService.selectRiderEvaluationListByRating(riderBaseId, filterType);
        return getDataTable(list);
    }
}