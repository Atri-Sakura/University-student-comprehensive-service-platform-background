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
import com.ruoyi.platform.domain.PlatformCoupon;
import com.ruoyi.platform.service.IPlatformCouponService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 平台优惠券Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/coupon")
public class PlatformCouponController extends BaseController
{
    @Autowired
    private IPlatformCouponService platformCouponService;

    /**
     * 查询平台优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('platform:coupon:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformCoupon platformCoupon)
    {
        startPage();
        List<PlatformCoupon> list = platformCouponService.selectPlatformCouponList(platformCoupon);
        return getDataTable(list);
    }

    /**
     * 导出平台优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('platform:coupon:export')")
    @Log(title = "平台优惠券", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlatformCoupon platformCoupon)
    {
        List<PlatformCoupon> list = platformCouponService.selectPlatformCouponList(platformCoupon);
        ExcelUtil<PlatformCoupon> util = new ExcelUtil<PlatformCoupon>(PlatformCoupon.class);
        util.exportExcel(response, list, "平台优惠券数据");
    }

    /**
     * 获取平台优惠券详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:coupon:query')")
    @GetMapping(value = "/{platformCouponId}")
    public AjaxResult getInfo(@PathVariable("platformCouponId") Long platformCouponId)
    {
        return success(platformCouponService.selectPlatformCouponByPlatformCouponId(platformCouponId));
    }

    /**
     * 新增平台优惠券
     */
    @PreAuthorize("@ss.hasPermi('platform:coupon:add')")
    @Log(title = "平台优惠券", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlatformCoupon platformCoupon)
    {
        return toAjax(platformCouponService.insertPlatformCoupon(platformCoupon));
    }

    /**
     * 修改平台优惠券
     */
    @PreAuthorize("@ss.hasPermi('platform:coupon:edit')")
    @Log(title = "平台优惠券", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformCoupon platformCoupon)
    {
        return toAjax(platformCouponService.updatePlatformCoupon(platformCoupon));
    }

    /**
     * 删除平台优惠券
     */
    @PreAuthorize("@ss.hasPermi('platform:coupon:remove')")
    @Log(title = "平台优惠券", businessType = BusinessType.DELETE)
	@DeleteMapping("/{platformCouponIds}")
    public AjaxResult remove(@PathVariable Long[] platformCouponIds)
    {
        return toAjax(platformCouponService.deletePlatformCouponByPlatformCouponIds(platformCouponIds));
    }
}
