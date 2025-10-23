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
import com.ruoyi.platform.domain.PlatformWorkorder;
import com.ruoyi.platform.service.IPlatformWorkorderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 客服工单Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/workorder")
public class PlatformWorkorderController extends BaseController
{
    @Autowired
    private IPlatformWorkorderService platformWorkorderService;

    /**
     * 查询客服工单列表
     */
    @PreAuthorize("@ss.hasPermi('platform:workorder:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformWorkorder platformWorkorder)
    {
        startPage();
        List<PlatformWorkorder> list = platformWorkorderService.selectPlatformWorkorderList(platformWorkorder);
        return getDataTable(list);
    }

    /**
     * 导出客服工单列表
     */
    @PreAuthorize("@ss.hasPermi('platform:workorder:export')")
    @Log(title = "客服工单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlatformWorkorder platformWorkorder)
    {
        List<PlatformWorkorder> list = platformWorkorderService.selectPlatformWorkorderList(platformWorkorder);
        ExcelUtil<PlatformWorkorder> util = new ExcelUtil<PlatformWorkorder>(PlatformWorkorder.class);
        util.exportExcel(response, list, "客服工单数据");
    }

    /**
     * 获取客服工单详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:workorder:query')")
    @GetMapping(value = "/{platformWorkorderId}")
    public AjaxResult getInfo(@PathVariable("platformWorkorderId") Long platformWorkorderId)
    {
        return success(platformWorkorderService.selectPlatformWorkorderByPlatformWorkorderId(platformWorkorderId));
    }

    /**
     * 新增客服工单
     */
    @PreAuthorize("@ss.hasPermi('platform:workorder:add')")
    @Log(title = "客服工单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlatformWorkorder platformWorkorder)
    {
        return toAjax(platformWorkorderService.insertPlatformWorkorder(platformWorkorder));
    }

    /**
     * 修改客服工单
     */
    @PreAuthorize("@ss.hasPermi('platform:workorder:edit')")
    @Log(title = "客服工单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformWorkorder platformWorkorder)
    {
        return toAjax(platformWorkorderService.updatePlatformWorkorder(platformWorkorder));
    }

    /**
     * 删除客服工单
     */
    @PreAuthorize("@ss.hasPermi('platform:workorder:remove')")
    @Log(title = "客服工单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{platformWorkorderIds}")
    public AjaxResult remove(@PathVariable Long[] platformWorkorderIds)
    {
        return toAjax(platformWorkorderService.deletePlatformWorkorderByPlatformWorkorderIds(platformWorkorderIds));
    }
}
