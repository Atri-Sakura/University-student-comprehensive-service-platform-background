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
import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.service.IMerchantBaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商家基础信息Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/merchant/base")
public class MerchantBaseController extends BaseController
{
    @Autowired
    private IMerchantBaseService merchantBaseService;

    /**
     * 查询商家基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:base:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantBase merchantBase)
    {
        startPage();
        List<MerchantBase> list = merchantBaseService.selectMerchantBaseList(merchantBase);
        return getDataTable(list);
    }

    /**
     * 导出商家基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:base:export')")
    @Log(title = "商家基础信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MerchantBase merchantBase)
    {
        List<MerchantBase> list = merchantBaseService.selectMerchantBaseList(merchantBase);
        ExcelUtil<MerchantBase> util = new ExcelUtil<MerchantBase>(MerchantBase.class);
        util.exportExcel(response, list, "商家基础信息数据");
    }

    /**
     * 获取商家基础信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('merchant:base:query')")
    @GetMapping(value = "/{merchantBaseId}")
    public AjaxResult getInfo(@PathVariable("merchantBaseId") Long merchantBaseId)
    {
        return success(merchantBaseService.selectMerchantBaseByMerchantBaseId(merchantBaseId));
    }

    /**
     * 新增商家基础信息
     */
    @PreAuthorize("@ss.hasPermi('merchant:base:add')")
    @Log(title = "商家基础信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantBase merchantBase)
    {
        return toAjax(merchantBaseService.insertMerchantBase(merchantBase));
    }

    /**
     * 修改商家基础信息
     */
    @PreAuthorize("@ss.hasPermi('merchant:base:edit')")
    @Log(title = "商家基础信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantBase merchantBase)
    {
        return toAjax(merchantBaseService.updateMerchantBase(merchantBase));
    }

    /**
     * 删除商家基础信息
     */
    @PreAuthorize("@ss.hasPermi('merchant:base:remove')")
    @Log(title = "商家基础信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{merchantBaseIds}")
    public AjaxResult remove(@PathVariable Long[] merchantBaseIds)
    {
        return toAjax(merchantBaseService.deleteMerchantBaseByMerchantBaseIds(merchantBaseIds));
    }
}
