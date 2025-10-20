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
import com.ruoyi.platform.domain.PlatformPermission;
import com.ruoyi.platform.service.IPlatformPermissionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 权限Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/permission")
public class PlatformPermissionController extends BaseController
{
    @Autowired
    private IPlatformPermissionService platformPermissionService;

    /**
     * 查询权限列表
     */
    @PreAuthorize("@ss.hasPermi('platform:permission:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformPermission platformPermission)
    {
        startPage();
        List<PlatformPermission> list = platformPermissionService.selectPlatformPermissionList(platformPermission);
        return getDataTable(list);
    }

    /**
     * 导出权限列表
     */
    @PreAuthorize("@ss.hasPermi('platform:permission:export')")
    @Log(title = "权限", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlatformPermission platformPermission)
    {
        List<PlatformPermission> list = platformPermissionService.selectPlatformPermissionList(platformPermission);
        ExcelUtil<PlatformPermission> util = new ExcelUtil<PlatformPermission>(PlatformPermission.class);
        util.exportExcel(response, list, "权限数据");
    }

    /**
     * 获取权限详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:permission:query')")
    @GetMapping(value = "/{platformPermissionId}")
    public AjaxResult getInfo(@PathVariable("platformPermissionId") Long platformPermissionId)
    {
        return success(platformPermissionService.selectPlatformPermissionByPlatformPermissionId(platformPermissionId));
    }

    /**
     * 新增权限
     */
    @PreAuthorize("@ss.hasPermi('platform:permission:add')")
    @Log(title = "权限", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlatformPermission platformPermission)
    {
        return toAjax(platformPermissionService.insertPlatformPermission(platformPermission));
    }

    /**
     * 修改权限
     */
    @PreAuthorize("@ss.hasPermi('platform:permission:edit')")
    @Log(title = "权限", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformPermission platformPermission)
    {
        return toAjax(platformPermissionService.updatePlatformPermission(platformPermission));
    }

    /**
     * 删除权限
     */
    @PreAuthorize("@ss.hasPermi('platform:permission:remove')")
    @Log(title = "权限", businessType = BusinessType.DELETE)
	@DeleteMapping("/{platformPermissionIds}")
    public AjaxResult remove(@PathVariable Long[] platformPermissionIds)
    {
        return toAjax(platformPermissionService.deletePlatformPermissionByPlatformPermissionIds(platformPermissionIds));
    }
}
