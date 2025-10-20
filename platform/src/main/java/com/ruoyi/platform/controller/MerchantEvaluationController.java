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
import com.ruoyi.platform.domain.MerchantEvaluation;
import com.ruoyi.platform.service.IMerchantEvaluationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商家评价Controller
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/system/evaluation")
public class MerchantEvaluationController extends BaseController
{
    @Autowired
    private IMerchantEvaluationService merchantEvaluationService;

    /**
     * 查询商家评价列表
     */
    @PreAuthorize("@ss.hasPermi('system:evaluation:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantEvaluation merchantEvaluation)
    {
        startPage();
        List<MerchantEvaluation> list = merchantEvaluationService.selectMerchantEvaluationList(merchantEvaluation);
        return getDataTable(list);
    }

    /**
     * 导出商家评价列表
     */
    @PreAuthorize("@ss.hasPermi('system:evaluation:export')")
    @Log(title = "商家评价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MerchantEvaluation merchantEvaluation)
    {
        List<MerchantEvaluation> list = merchantEvaluationService.selectMerchantEvaluationList(merchantEvaluation);
        ExcelUtil<MerchantEvaluation> util = new ExcelUtil<MerchantEvaluation>(MerchantEvaluation.class);
        util.exportExcel(response, list, "商家评价数据");
    }

    /**
     * 获取商家评价详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:evaluation:query')")
    @GetMapping(value = "/{merchantEvaluationId}")
    public AjaxResult getInfo(@PathVariable("merchantEvaluationId") Long merchantEvaluationId)
    {
        return success(merchantEvaluationService.selectMerchantEvaluationByMerchantEvaluationId(merchantEvaluationId));
    }

    /**
     * 新增商家评价
     */
    @PreAuthorize("@ss.hasPermi('system:evaluation:add')")
    @Log(title = "商家评价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantEvaluation merchantEvaluation)
    {
        return toAjax(merchantEvaluationService.insertMerchantEvaluation(merchantEvaluation));
    }

    /**
     * 修改商家评价
     */
    @PreAuthorize("@ss.hasPermi('system:evaluation:edit')")
    @Log(title = "商家评价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantEvaluation merchantEvaluation)
    {
        return toAjax(merchantEvaluationService.updateMerchantEvaluation(merchantEvaluation));
    }

    /**
     * 删除商家评价
     */
    @PreAuthorize("@ss.hasPermi('system:evaluation:remove')")
    @Log(title = "商家评价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{merchantEvaluationIds}")
    public AjaxResult remove(@PathVariable Long[] merchantEvaluationIds)
    {
        return toAjax(merchantEvaluationService.deleteMerchantEvaluationByMerchantEvaluationIds(merchantEvaluationIds));
    }
}
