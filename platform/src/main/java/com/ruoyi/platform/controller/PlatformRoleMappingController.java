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
import com.ruoyi.platform.domain.PlatformRoleMapping;
import com.ruoyi.platform.service.IPlatformRoleMappingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 角色-账号映射（多角色登录路由核心）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/mapping")
public class PlatformRoleMappingController extends BaseController
{
    @Autowired
    private IPlatformRoleMappingService platformRoleMappingService;

    /**
     * 查询角色-账号映射（多角色登录路由核心）列表
     */
    @PreAuthorize("@ss.hasPermi('platform:mapping:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformRoleMapping platformRoleMapping)
    {
        startPage();
        List<PlatformRoleMapping> list = platformRoleMappingService.selectPlatformRoleMappingList(platformRoleMapping);
        return getDataTable(list);
    }

    /**
     * 导出角色-账号映射（多角色登录路由核心）列表
     */
    @PreAuthorize("@ss.hasPermi('platform:mapping:export')")
    @Log(title = "角色-账号映射（多角色登录路由核心）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlatformRoleMapping platformRoleMapping)
    {
        List<PlatformRoleMapping> list = platformRoleMappingService.selectPlatformRoleMappingList(platformRoleMapping);
        ExcelUtil<PlatformRoleMapping> util = new ExcelUtil<PlatformRoleMapping>(PlatformRoleMapping.class);
        util.exportExcel(response, list, "角色-账号映射（多角色登录路由核心）数据");
    }

    /**
     * 获取角色-账号映射（多角色登录路由核心）详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:mapping:query')")
    @GetMapping(value = "/{platformRoleMappingId}")
    public AjaxResult getInfo(@PathVariable("platformRoleMappingId") Long platformRoleMappingId)
    {
        return success(platformRoleMappingService.selectPlatformRoleMappingByPlatformRoleMappingId(platformRoleMappingId));
    }

    /**
     * 新增角色-账号映射（多角色登录路由核心）
     */
    @PreAuthorize("@ss.hasPermi('platform:mapping:add')")
    @Log(title = "角色-账号映射（多角色登录路由核心）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlatformRoleMapping platformRoleMapping)
    {
        return toAjax(platformRoleMappingService.insertPlatformRoleMapping(platformRoleMapping));
    }

    /**
     * 修改角色-账号映射（多角色登录路由核心）
     */
    @PreAuthorize("@ss.hasPermi('platform:mapping:edit')")
    @Log(title = "角色-账号映射（多角色登录路由核心）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformRoleMapping platformRoleMapping)
    {
        return toAjax(platformRoleMappingService.updatePlatformRoleMapping(platformRoleMapping));
    }

    /**
     * 删除角色-账号映射（多角色登录路由核心）
     */
    @PreAuthorize("@ss.hasPermi('platform:mapping:remove')")
    @Log(title = "角色-账号映射（多角色登录路由核心）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{platformRoleMappingIds}")
    public AjaxResult remove(@PathVariable Long[] platformRoleMappingIds)
    {
        return toAjax(platformRoleMappingService.deletePlatformRoleMappingByPlatformRoleMappingIds(platformRoleMappingIds));
    }
}
