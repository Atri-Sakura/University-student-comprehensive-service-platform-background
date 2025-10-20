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
import com.ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.service.IRiderBaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 骑手基础信息Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/rider/base")
public class RiderBaseController extends BaseController
{
    @Autowired
    private IRiderBaseService riderBaseService;

    /**
     * 查询骑手基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('rider:base:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiderBase riderBase)
    {
        startPage();
        List<RiderBase> list = riderBaseService.selectRiderBaseList(riderBase);
        return getDataTable(list);
    }

    /**
     * 导出骑手基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('rider:base:export')")
    @Log(title = "骑手基础信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiderBase riderBase)
    {
        List<RiderBase> list = riderBaseService.selectRiderBaseList(riderBase);
        ExcelUtil<RiderBase> util = new ExcelUtil<RiderBase>(RiderBase.class);
        util.exportExcel(response, list, "骑手基础信息数据");
    }

    /**
     * 获取骑手基础信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('rider:base:query')")
    @GetMapping(value = "/{riderBaseId}")
    public AjaxResult getInfo(@PathVariable("riderBaseId") Long riderBaseId)
    {
        return success(riderBaseService.selectRiderBaseByRiderBaseId(riderBaseId));
    }

    /**
     * 新增骑手基础信息
     */
    @PreAuthorize("@ss.hasPermi('rider:base:add')")
    @Log(title = "骑手基础信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiderBase riderBase)
    {
        return toAjax(riderBaseService.insertRiderBase(riderBase));
    }

    /**
     * 修改骑手基础信息
     */
    @PreAuthorize("@ss.hasPermi('rider:base:edit')")
    @Log(title = "骑手基础信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiderBase riderBase)
    {
        return toAjax(riderBaseService.updateRiderBase(riderBase));
    }

    /**
     * 删除骑手基础信息
     */
    @PreAuthorize("@ss.hasPermi('rider:base:remove')")
    @Log(title = "骑手基础信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{riderBaseIds}")
    public AjaxResult remove(@PathVariable Long[] riderBaseIds)
    {
        return toAjax(riderBaseService.deleteRiderBaseByRiderBaseIds(riderBaseIds));
    }
}
