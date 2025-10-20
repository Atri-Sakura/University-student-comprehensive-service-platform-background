package com.ruoyi.platform.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.platform.domain.RiderEvaluation;
import com.ruoyi.platform.service.IRiderEvaluationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 骑手评价Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/rider/evaluation")
public class RiderEvaluationController extends BaseController
{
    @Autowired
    private IRiderEvaluationService riderEvaluationService;

    /**
     * 查询骑手评价列表
     */
    @PreAuthorize("@ss.hasPermi('rider:evaluation:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiderEvaluation riderEvaluation)
    {
        startPage();
        List<RiderEvaluation> list = riderEvaluationService.selectRiderEvaluationList(riderEvaluation);
        return getDataTable(list);
    }

    /**
     * 导出骑手评价列表
     */
    @PreAuthorize("@ss.hasPermi('rider:evaluation:export')")
    @Log(title = "骑手评价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiderEvaluation riderEvaluation)
    {
        List<RiderEvaluation> list = riderEvaluationService.selectRiderEvaluationList(riderEvaluation);
        ExcelUtil<RiderEvaluation> util = new ExcelUtil<RiderEvaluation>(RiderEvaluation.class);
        util.exportExcel(response, list, "骑手评价数据");
    }

    /**
     * 获取骑手评价详细信息
     */
    @PreAuthorize("@ss.hasPermi('rider:evaluation:query')")
    @GetMapping(value = "/{riderEvaluationId}")
    public AjaxResult getInfo(@PathVariable("riderEvaluationId") Long riderEvaluationId)
    {
        return success(riderEvaluationService.selectRiderEvaluationByRiderEvaluationId(riderEvaluationId));
    }

    /**
     * 新增骑手评价
     */
    @PreAuthorize("@ss.hasPermi('rider:evaluation:add')")
    @Log(title = "骑手评价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiderEvaluation riderEvaluation)
    {
        return toAjax(riderEvaluationService.insertRiderEvaluation(riderEvaluation));
    }

    /**
     * 修改骑手评价
     */
    @PreAuthorize("@ss.hasPermi('rider:evaluation:edit')")
    @Log(title = "骑手评价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiderEvaluation riderEvaluation)
    {
        return toAjax(riderEvaluationService.updateRiderEvaluation(riderEvaluation));
    }

    /**
     * 删除骑手评价
     */
    @PreAuthorize("@ss.hasPermi('rider:evaluation:remove')")
    @Log(title = "骑手评价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{riderEvaluationIds}")
    public AjaxResult remove(@PathVariable Long[] riderEvaluationIds)
    {
        return toAjax(riderEvaluationService.deleteRiderEvaluationByRiderEvaluationIds(riderEvaluationIds));
    }
}
