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
import com.ruoyi.platform.domain.RiderOrderRel;
import com.ruoyi.platform.service.IRiderOrderRelService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 骑手接单关联Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/rider/rel")
public class RiderOrderRelController extends BaseController
{
    @Autowired
    private IRiderOrderRelService riderOrderRelService;

    /**
     * 查询骑手接单关联列表
     */
    @PreAuthorize("@ss.hasPermi('rider:rel:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiderOrderRel riderOrderRel)
    {
        startPage();
        List<RiderOrderRel> list = riderOrderRelService.selectRiderOrderRelList(riderOrderRel);
        return getDataTable(list);
    }

    /**
     * 导出骑手接单关联列表
     */
    @PreAuthorize("@ss.hasPermi('rider:rel:export')")
    @Log(title = "骑手接单关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiderOrderRel riderOrderRel)
    {
        List<RiderOrderRel> list = riderOrderRelService.selectRiderOrderRelList(riderOrderRel);
        ExcelUtil<RiderOrderRel> util = new ExcelUtil<RiderOrderRel>(RiderOrderRel.class);
        util.exportExcel(response, list, "骑手接单关联数据");
    }

    /**
     * 获取骑手接单关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('rider:rel:query')")
    @GetMapping(value = "/{riderOrderRelId}")
    public AjaxResult getInfo(@PathVariable("riderOrderRelId") Long riderOrderRelId)
    {
        return success(riderOrderRelService.selectRiderOrderRelByRiderOrderRelId(riderOrderRelId));
    }

    /**
     * 新增骑手接单关联
     */
    @PreAuthorize("@ss.hasPermi('rider:rel:add')")
    @Log(title = "骑手接单关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiderOrderRel riderOrderRel)
    {
        return toAjax(riderOrderRelService.insertRiderOrderRel(riderOrderRel));
    }

    /**
     * 修改骑手接单关联
     */
    @PreAuthorize("@ss.hasPermi('rider:rel:edit')")
    @Log(title = "骑手接单关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiderOrderRel riderOrderRel)
    {
        return toAjax(riderOrderRelService.updateRiderOrderRel(riderOrderRel));
    }

    /**
     * 删除骑手接单关联
     */
    @PreAuthorize("@ss.hasPermi('rider:rel:remove')")
    @Log(title = "骑手接单关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{riderOrderRelIds}")
    public AjaxResult remove(@PathVariable Long[] riderOrderRelIds)
    {
        return toAjax(riderOrderRelService.deleteRiderOrderRelByRiderOrderRelIds(riderOrderRelIds));
    }
}
