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
import com.ruoyi.platform.domain.PlatformRole;
import com.ruoyi.platform.service.IPlatformRoleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 角色Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/role")
public class PlatformRoleController extends BaseController
{
    @Autowired
    private IPlatformRoleService platformRoleService;

    /**
     * 查询角色列表
     */
    @PreAuthorize("@ss.hasPermi('platform:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformRole platformRole)
    {
        startPage();
        List<PlatformRole> list = platformRoleService.selectPlatformRoleList(platformRole);
        return getDataTable(list);
    }

    /**
     * 导出角色列表
     */
    @PreAuthorize("@ss.hasPermi('platform:role:export')")
    @Log(title = "角色", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlatformRole platformRole)
    {
        List<PlatformRole> list = platformRoleService.selectPlatformRoleList(platformRole);
        ExcelUtil<PlatformRole> util = new ExcelUtil<PlatformRole>(PlatformRole.class);
        util.exportExcel(response, list, "角色数据");
    }

    /**
     * 获取角色详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:role:query')")
    @GetMapping(value = "/{platformRoleId}")
    public AjaxResult getInfo(@PathVariable("platformRoleId") Long platformRoleId)
    {
        return success(platformRoleService.selectPlatformRoleByPlatformRoleId(platformRoleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('platform:role:add')")
    @Log(title = "角色", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlatformRole platformRole)
    {
        return toAjax(platformRoleService.insertPlatformRole(platformRole));
    }

    /**
     * 修改角色
     */
    @PreAuthorize("@ss.hasPermi('platform:role:edit')")
    @Log(title = "角色", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformRole platformRole)
    {
        return toAjax(platformRoleService.updatePlatformRole(platformRole));
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('platform:role:remove')")
    @Log(title = "角色", businessType = BusinessType.DELETE)
	@DeleteMapping("/{platformRoleIds}")
    public AjaxResult remove(@PathVariable Long[] platformRoleIds)
    {
        return toAjax(platformRoleService.deletePlatformRoleByPlatformRoleIds(platformRoleIds));
    }
}
