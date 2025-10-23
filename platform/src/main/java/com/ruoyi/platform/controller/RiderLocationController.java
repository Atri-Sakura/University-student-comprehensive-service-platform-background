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
import com.ruoyi.platform.domain.RiderLocation;
import com.ruoyi.platform.service.IRiderLocationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 骑手位置Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/rider/location")
public class RiderLocationController extends BaseController
{
    @Autowired
    private IRiderLocationService riderLocationService;

    /**
     * 查询骑手位置列表
     */
    @PreAuthorize("@ss.hasPermi('rider:location:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiderLocation riderLocation)
    {
        startPage();
        List<RiderLocation> list = riderLocationService.selectRiderLocationList(riderLocation);
        return getDataTable(list);
    }

    /**
     * 导出骑手位置列表
     */
    @PreAuthorize("@ss.hasPermi('rider:location:export')")
    @Log(title = "骑手位置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiderLocation riderLocation)
    {
        List<RiderLocation> list = riderLocationService.selectRiderLocationList(riderLocation);
        ExcelUtil<RiderLocation> util = new ExcelUtil<RiderLocation>(RiderLocation.class);
        util.exportExcel(response, list, "骑手位置数据");
    }

    /**
     * 获取骑手位置详细信息
     */
    @PreAuthorize("@ss.hasPermi('rider:location:query')")
    @GetMapping(value = "/{riderLocationId}")
    public AjaxResult getInfo(@PathVariable("riderLocationId") Long riderLocationId)
    {
        return success(riderLocationService.selectRiderLocationByRiderLocationId(riderLocationId));
    }

    /**
     * 新增骑手位置
     */
    @PreAuthorize("@ss.hasPermi('rider:location:add')")
    @Log(title = "骑手位置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiderLocation riderLocation)
    {
        return toAjax(riderLocationService.insertRiderLocation(riderLocation));
    }

    /**
     * 修改骑手位置
     */
    @PreAuthorize("@ss.hasPermi('rider:location:edit')")
    @Log(title = "骑手位置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiderLocation riderLocation)
    {
        return toAjax(riderLocationService.updateRiderLocation(riderLocation));
    }

    /**
     * 删除骑手位置
     */
    @PreAuthorize("@ss.hasPermi('rider:location:remove')")
    @Log(title = "骑手位置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{riderLocationIds}")
    public AjaxResult remove(@PathVariable Long[] riderLocationIds)
    {
        return toAjax(riderLocationService.deleteRiderLocationByRiderLocationIds(riderLocationIds));
    }
}
