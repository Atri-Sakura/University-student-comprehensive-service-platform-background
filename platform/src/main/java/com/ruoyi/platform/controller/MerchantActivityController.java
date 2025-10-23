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
import com.ruoyi.platform.domain.MerchantActivity;
import com.ruoyi.platform.service.IMerchantActivityService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商家活动Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/merchant/activity")
public class MerchantActivityController extends BaseController
{
    @Autowired
    private IMerchantActivityService merchantActivityService;

    /**
     * 查询商家活动列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:activity:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantActivity merchantActivity)
    {
        startPage();
        List<MerchantActivity> list = merchantActivityService.selectMerchantActivityList(merchantActivity);
        return getDataTable(list);
    }

    /**
     * 导出商家活动列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:activity:export')")
    @Log(title = "商家活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MerchantActivity merchantActivity)
    {
        List<MerchantActivity> list = merchantActivityService.selectMerchantActivityList(merchantActivity);
        ExcelUtil<MerchantActivity> util = new ExcelUtil<MerchantActivity>(MerchantActivity.class);
        util.exportExcel(response, list, "商家活动数据");
    }

    /**
     * 获取商家活动详细信息
     */
    @PreAuthorize("@ss.hasPermi('merchant:activity:query')")
    @GetMapping(value = "/{merchantActivityId}")
    public AjaxResult getInfo(@PathVariable("merchantActivityId") Long merchantActivityId)
    {
        return success(merchantActivityService.selectMerchantActivityByMerchantActivityId(merchantActivityId));
    }

    /**
     * 新增商家活动
     */
    @PreAuthorize("@ss.hasPermi('merchant:activity:add')")
    @Log(title = "商家活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantActivity merchantActivity)
    {
        return toAjax(merchantActivityService.insertMerchantActivity(merchantActivity));
    }

    /**
     * 修改商家活动
     */
    @PreAuthorize("@ss.hasPermi('merchant:activity:edit')")
    @Log(title = "商家活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantActivity merchantActivity)
    {
        return toAjax(merchantActivityService.updateMerchantActivity(merchantActivity));
    }

    /**
     * 删除商家活动
     */
    @PreAuthorize("@ss.hasPermi('merchant:activity:remove')")
    @Log(title = "商家活动", businessType = BusinessType.DELETE)
	@DeleteMapping("/{merchantActivityIds}")
    public AjaxResult remove(@PathVariable Long[] merchantActivityIds)
    {
        return toAjax(merchantActivityService.deleteMerchantActivityByMerchantActivityIds(merchantActivityIds));
    }
}
