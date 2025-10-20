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
import com.ruoyi.platform.domain.RiderWallet;
import com.ruoyi.platform.service.IRiderWalletService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 骑手钱包Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/rider/wallet")
public class RiderWalletController extends BaseController
{
    @Autowired
    private IRiderWalletService riderWalletService;

    /**
     * 查询骑手钱包列表
     */
    @PreAuthorize("@ss.hasPermi('rider:wallet:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiderWallet riderWallet)
    {
        startPage();
        List<RiderWallet> list = riderWalletService.selectRiderWalletList(riderWallet);
        return getDataTable(list);
    }

    /**
     * 导出骑手钱包列表
     */
    @PreAuthorize("@ss.hasPermi('rider:wallet:export')")
    @Log(title = "骑手钱包", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiderWallet riderWallet)
    {
        List<RiderWallet> list = riderWalletService.selectRiderWalletList(riderWallet);
        ExcelUtil<RiderWallet> util = new ExcelUtil<RiderWallet>(RiderWallet.class);
        util.exportExcel(response, list, "骑手钱包数据");
    }

    /**
     * 获取骑手钱包详细信息
     */
    @PreAuthorize("@ss.hasPermi('rider:wallet:query')")
    @GetMapping(value = "/{riderWalletId}")
    public AjaxResult getInfo(@PathVariable("riderWalletId") Long riderWalletId)
    {
        return success(riderWalletService.selectRiderWalletByRiderWalletId(riderWalletId));
    }

    /**
     * 新增骑手钱包
     */
    @PreAuthorize("@ss.hasPermi('rider:wallet:add')")
    @Log(title = "骑手钱包", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiderWallet riderWallet)
    {
        return toAjax(riderWalletService.insertRiderWallet(riderWallet));
    }

    /**
     * 修改骑手钱包
     */
    @PreAuthorize("@ss.hasPermi('rider:wallet:edit')")
    @Log(title = "骑手钱包", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiderWallet riderWallet)
    {
        return toAjax(riderWalletService.updateRiderWallet(riderWallet));
    }

    /**
     * 删除骑手钱包
     */
    @PreAuthorize("@ss.hasPermi('rider:wallet:remove')")
    @Log(title = "骑手钱包", businessType = BusinessType.DELETE)
	@DeleteMapping("/{riderWalletIds}")
    public AjaxResult remove(@PathVariable Long[] riderWalletIds)
    {
        return toAjax(riderWalletService.deleteRiderWalletByRiderWalletIds(riderWalletIds));
    }
}
