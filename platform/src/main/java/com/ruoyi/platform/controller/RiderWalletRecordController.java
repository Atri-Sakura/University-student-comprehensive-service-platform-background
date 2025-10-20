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
import com.ruoyi.platform.domain.RiderWalletRecord;
import com.ruoyi.platform.service.IRiderWalletRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 骑手钱包流水Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/rider/record")
public class RiderWalletRecordController extends BaseController
{
    @Autowired
    private IRiderWalletRecordService riderWalletRecordService;

    /**
     * 查询骑手钱包流水列表
     */
    @PreAuthorize("@ss.hasPermi('rider:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiderWalletRecord riderWalletRecord)
    {
        startPage();
        List<RiderWalletRecord> list = riderWalletRecordService.selectRiderWalletRecordList(riderWalletRecord);
        return getDataTable(list);
    }

    /**
     * 导出骑手钱包流水列表
     */
    @PreAuthorize("@ss.hasPermi('rider:record:export')")
    @Log(title = "骑手钱包流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiderWalletRecord riderWalletRecord)
    {
        List<RiderWalletRecord> list = riderWalletRecordService.selectRiderWalletRecordList(riderWalletRecord);
        ExcelUtil<RiderWalletRecord> util = new ExcelUtil<RiderWalletRecord>(RiderWalletRecord.class);
        util.exportExcel(response, list, "骑手钱包流水数据");
    }

    /**
     * 获取骑手钱包流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('rider:record:query')")
    @GetMapping(value = "/{riderWalletRecordId}")
    public AjaxResult getInfo(@PathVariable("riderWalletRecordId") Long riderWalletRecordId)
    {
        return success(riderWalletRecordService.selectRiderWalletRecordByRiderWalletRecordId(riderWalletRecordId));
    }

    /**
     * 新增骑手钱包流水
     */
    @PreAuthorize("@ss.hasPermi('rider:record:add')")
    @Log(title = "骑手钱包流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiderWalletRecord riderWalletRecord)
    {
        return toAjax(riderWalletRecordService.insertRiderWalletRecord(riderWalletRecord));
    }

    /**
     * 修改骑手钱包流水
     */
    @PreAuthorize("@ss.hasPermi('rider:record:edit')")
    @Log(title = "骑手钱包流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiderWalletRecord riderWalletRecord)
    {
        return toAjax(riderWalletRecordService.updateRiderWalletRecord(riderWalletRecord));
    }

    /**
     * 删除骑手钱包流水
     */
    @PreAuthorize("@ss.hasPermi('rider:record:remove')")
    @Log(title = "骑手钱包流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{riderWalletRecordIds}")
    public AjaxResult remove(@PathVariable Long[] riderWalletRecordIds)
    {
        return toAjax(riderWalletRecordService.deleteRiderWalletRecordByRiderWalletRecordIds(riderWalletRecordIds));
    }
}
