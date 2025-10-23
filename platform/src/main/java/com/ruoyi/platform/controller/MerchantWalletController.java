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
import com.ruoyi.platform.domain.MerchantWallet;
import com.ruoyi.platform.service.IMerchantWalletService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商家钱包Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/merchant/wallet")
public class MerchantWalletController extends BaseController
{
    @Autowired
    private IMerchantWalletService merchantWalletService;

    /**
     * 查询商家钱包列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:wallet:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantWallet merchantWallet)
    {
        startPage();
        List<MerchantWallet> list = merchantWalletService.selectMerchantWalletList(merchantWallet);
        return getDataTable(list);
    }

    /**
     * 导出商家钱包列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:wallet:export')")
    @Log(title = "商家钱包", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MerchantWallet merchantWallet)
    {
        List<MerchantWallet> list = merchantWalletService.selectMerchantWalletList(merchantWallet);
        ExcelUtil<MerchantWallet> util = new ExcelUtil<MerchantWallet>(MerchantWallet.class);
        util.exportExcel(response, list, "商家钱包数据");
    }

    /**
     * 获取商家钱包详细信息
     */
    @PreAuthorize("@ss.hasPermi('merchant:wallet:query')")
    @GetMapping(value = "/{merchantWalletId}")
    public AjaxResult getInfo(@PathVariable("merchantWalletId") Long merchantWalletId)
    {
        return success(merchantWalletService.selectMerchantWalletByMerchantWalletId(merchantWalletId));
    }

    /**
     * 新增商家钱包
     */
    @PreAuthorize("@ss.hasPermi('merchant:wallet:add')")
    @Log(title = "商家钱包", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantWallet merchantWallet)
    {
        return toAjax(merchantWalletService.insertMerchantWallet(merchantWallet));
    }

    /**
     * 修改商家钱包
     */
    @PreAuthorize("@ss.hasPermi('merchant:wallet:edit')")
    @Log(title = "商家钱包", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantWallet merchantWallet)
    {
        return toAjax(merchantWalletService.updateMerchantWallet(merchantWallet));
    }

    /**
     * 删除商家钱包
     */
    @PreAuthorize("@ss.hasPermi('merchant:wallet:remove')")
    @Log(title = "商家钱包", businessType = BusinessType.DELETE)
	@DeleteMapping("/{merchantWalletIds}")
    public AjaxResult remove(@PathVariable Long[] merchantWalletIds)
    {
        return toAjax(merchantWalletService.deleteMerchantWalletByMerchantWalletIds(merchantWalletIds));
    }
}
