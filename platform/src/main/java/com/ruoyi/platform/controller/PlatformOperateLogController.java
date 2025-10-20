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
import com.ruoyi.platform.domain.PlatformOperateLog;
import com.ruoyi.platform.service.IPlatformOperateLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 系统操作日志Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/log")
public class PlatformOperateLogController extends BaseController
{
    @Autowired
    private IPlatformOperateLogService platformOperateLogService;

    /**
     * 查询系统操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('platform:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformOperateLog platformOperateLog)
    {
        startPage();
        List<PlatformOperateLog> list = platformOperateLogService.selectPlatformOperateLogList(platformOperateLog);
        return getDataTable(list);
    }

    /**
     * 导出系统操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('platform:log:export')")
    @Log(title = "系统操作日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlatformOperateLog platformOperateLog)
    {
        List<PlatformOperateLog> list = platformOperateLogService.selectPlatformOperateLogList(platformOperateLog);
        ExcelUtil<PlatformOperateLog> util = new ExcelUtil<PlatformOperateLog>(PlatformOperateLog.class);
        util.exportExcel(response, list, "系统操作日志数据");
    }

    /**
     * 获取系统操作日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:log:query')")
    @GetMapping(value = "/{platformOperateLogId}")
    public AjaxResult getInfo(@PathVariable("platformOperateLogId") Long platformOperateLogId)
    {
        return success(platformOperateLogService.selectPlatformOperateLogByPlatformOperateLogId(platformOperateLogId));
    }

    /**
     * 新增系统操作日志
     */
    @PreAuthorize("@ss.hasPermi('platform:log:add')")
    @Log(title = "系统操作日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlatformOperateLog platformOperateLog)
    {
        return toAjax(platformOperateLogService.insertPlatformOperateLog(platformOperateLog));
    }

    /**
     * 修改系统操作日志
     */
    @PreAuthorize("@ss.hasPermi('platform:log:edit')")
    @Log(title = "系统操作日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformOperateLog platformOperateLog)
    {
        return toAjax(platformOperateLogService.updatePlatformOperateLog(platformOperateLog));
    }

    /**
     * 删除系统操作日志
     */
    @PreAuthorize("@ss.hasPermi('platform:log:remove')")
    @Log(title = "系统操作日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{platformOperateLogIds}")
    public AjaxResult remove(@PathVariable Long[] platformOperateLogIds)
    {
        return toAjax(platformOperateLogService.deletePlatformOperateLogByPlatformOperateLogIds(platformOperateLogIds));
    }
}
