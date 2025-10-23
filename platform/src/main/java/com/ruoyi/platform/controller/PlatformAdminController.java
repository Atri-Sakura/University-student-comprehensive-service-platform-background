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
import com.ruoyi.platform.domain.PlatformAdmin;
import com.ruoyi.platform.service.IPlatformAdminService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 平台管理员Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/admin")
public class PlatformAdminController extends BaseController
{
    @Autowired
    private IPlatformAdminService platformAdminService;

    /**
     * 查询平台管理员列表
     */
    @PreAuthorize("@ss.hasPermi('platform:admin:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformAdmin platformAdmin)
    {
        startPage();
        List<PlatformAdmin> list = platformAdminService.selectPlatformAdminList(platformAdmin);
        return getDataTable(list);
    }

    /**
     * 导出平台管理员列表
     */
    @PreAuthorize("@ss.hasPermi('platform:admin:export')")
    @Log(title = "平台管理员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlatformAdmin platformAdmin)
    {
        List<PlatformAdmin> list = platformAdminService.selectPlatformAdminList(platformAdmin);
        ExcelUtil<PlatformAdmin> util = new ExcelUtil<PlatformAdmin>(PlatformAdmin.class);
        util.exportExcel(response, list, "平台管理员数据");
    }

    /**
     * 获取平台管理员详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:admin:query')")
    @GetMapping(value = "/{platformAdminId}")
    public AjaxResult getInfo(@PathVariable("platformAdminId") Long platformAdminId)
    {
        return success(platformAdminService.selectPlatformAdminByPlatformAdminId(platformAdminId));
    }

    /**
     * 新增平台管理员
     */
    @PreAuthorize("@ss.hasPermi('platform:admin:add')")
    @Log(title = "平台管理员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlatformAdmin platformAdmin)
    {
        return toAjax(platformAdminService.insertPlatformAdmin(platformAdmin));
    }

    /**
     * 修改平台管理员
     */
    @PreAuthorize("@ss.hasPermi('platform:admin:edit')")
    @Log(title = "平台管理员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformAdmin platformAdmin)
    {
        return toAjax(platformAdminService.updatePlatformAdmin(platformAdmin));
    }

    /**
     * 删除平台管理员
     */
    @PreAuthorize("@ss.hasPermi('platform:admin:remove')")
    @Log(title = "平台管理员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{platformAdminIds}")
    public AjaxResult remove(@PathVariable Long[] platformAdminIds)
    {
        return toAjax(platformAdminService.deletePlatformAdminByPlatformAdminIds(platformAdminIds));
    }
}
