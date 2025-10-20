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
import com.ruoyi.platform.domain.PlatformRolePerm;
import com.ruoyi.platform.service.IPlatformRolePermService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 角色权限关联Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/perm")
public class PlatformRolePermController extends BaseController
{
    @Autowired
    private IPlatformRolePermService platformRolePermService;

    /**
     * 查询角色权限关联列表
     */
    @PreAuthorize("@ss.hasPermi('platform:perm:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformRolePerm platformRolePerm)
    {
        startPage();
        List<PlatformRolePerm> list = platformRolePermService.selectPlatformRolePermList(platformRolePerm);
        return getDataTable(list);
    }

    /**
     * 导出角色权限关联列表
     */
    @PreAuthorize("@ss.hasPermi('platform:perm:export')")
    @Log(title = "角色权限关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlatformRolePerm platformRolePerm)
    {
        List<PlatformRolePerm> list = platformRolePermService.selectPlatformRolePermList(platformRolePerm);
        ExcelUtil<PlatformRolePerm> util = new ExcelUtil<PlatformRolePerm>(PlatformRolePerm.class);
        util.exportExcel(response, list, "角色权限关联数据");
    }

    /**
     * 获取角色权限关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:perm:query')")
    @GetMapping(value = "/{platformRolePermId}")
    public AjaxResult getInfo(@PathVariable("platformRolePermId") Long platformRolePermId)
    {
        return success(platformRolePermService.selectPlatformRolePermByPlatformRolePermId(platformRolePermId));
    }

    /**
     * 新增角色权限关联
     */
    @PreAuthorize("@ss.hasPermi('platform:perm:add')")
    @Log(title = "角色权限关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlatformRolePerm platformRolePerm)
    {
        return toAjax(platformRolePermService.insertPlatformRolePerm(platformRolePerm));
    }

    /**
     * 修改角色权限关联
     */
    @PreAuthorize("@ss.hasPermi('platform:perm:edit')")
    @Log(title = "角色权限关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformRolePerm platformRolePerm)
    {
        return toAjax(platformRolePermService.updatePlatformRolePerm(platformRolePerm));
    }

    /**
     * 删除角色权限关联
     */
    @PreAuthorize("@ss.hasPermi('platform:perm:remove')")
    @Log(title = "角色权限关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{platformRolePermIds}")
    public AjaxResult remove(@PathVariable Long[] platformRolePermIds)
    {
        return toAjax(platformRolePermService.deletePlatformRolePermByPlatformRolePermIds(platformRolePermIds));
    }
}
