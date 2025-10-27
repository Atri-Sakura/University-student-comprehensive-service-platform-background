package com.ruoyi.platform.merchant.controller;

import java.util.List;

import com.ruoyi.platform.merchant.service.IMerchantReportService;
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
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.platform.domain.MerchantReport;


/**
 * 商家报表Controller
 *
 * @author ruoyi
 * @date 2025-10-24
 */
@RestController
@RequestMapping("/api/report")
public class MerchantReportController extends BaseController
{
    @Autowired
    private IMerchantReportService merchantReportService;

    /**
     * 查询商家报表列表
     */
    @PreAuthorize("@ss.hasPermi('platform:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantReport merchantReport)
    {
        startPage(); // 启动分页（若依框架分页工具）
        List<MerchantReport> list = merchantReportService.selectMerchantReportList(merchantReport);
        return getDataTable(list); // 封装分页结果
    }

    /**
     * 导出商家报表列表
     */
    @PreAuthorize("@ss.hasPermi('platform:report:export')")
    @Log(title = "商家报表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MerchantReport merchantReport)
    {
        List<MerchantReport> list = merchantReportService.selectMerchantReportList(merchantReport);
        ExcelUtil<MerchantReport> util = new ExcelUtil<MerchantReport>(MerchantReport.class);
        util.exportExcel(response, list, "商家报表数据");
    }

    /**
     * 获取商家报表详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:report:query')")
    @GetMapping(value = "/{reportId}")
    public AjaxResult getInfo(@PathVariable("reportId") Long reportId)
    {
        return success(merchantReportService.selectMerchantReportByReportId(reportId));
    }

    /**
     * 新增商家报表
     */
//    @PreAuthorize("@ss.hasPermi('platform:report:add')")
    @Log(title = "商家报表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantReport merchantReport)
    {
        // 可在此处添加业务校验（如：商家ID不能为空、报表内容长度限制等）
        if (merchantReport.getMerchantBaseId() == null) {
            return error("商家基础ID不能为空");
        }
        return toAjax(merchantReportService.insertMerchantReport(merchantReport));
    }

    /**
     * 修改商家报表
     */
    @PreAuthorize("@ss.hasPermi('platform:report:edit')")
    @Log(title = "商家报表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantReport merchantReport)
    {
        // 校验报表ID是否存在
        if (merchantReport.getReportId() == null) {
            return error("报表ID不能为空");
        }
        return toAjax(merchantReportService.updateMerchantReport(merchantReport));
    }

    /**
     * 删除商家报表
     */
    @PreAuthorize("@ss.hasPermi('platform:report:remove')")
    @Log(title = "商家报表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{reportIds}")
    public AjaxResult remove(@PathVariable Long[] reportIds)
    {
        return toAjax(merchantReportService.deleteMerchantReportByReportIds(reportIds));
    }
}