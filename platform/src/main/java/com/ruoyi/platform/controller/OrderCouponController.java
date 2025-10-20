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
import com.ruoyi.platform.domain.OrderCoupon;
import com.ruoyi.platform.service.IOrderCouponService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单优惠券Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/order/coupon")
public class OrderCouponController extends BaseController
{
    @Autowired
    private IOrderCouponService orderCouponService;

    /**
     * 查询订单优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('order:coupon:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderCoupon orderCoupon)
    {
        startPage();
        List<OrderCoupon> list = orderCouponService.selectOrderCouponList(orderCoupon);
        return getDataTable(list);
    }

    /**
     * 导出订单优惠券列表
     */
    @PreAuthorize("@ss.hasPermi('order:coupon:export')")
    @Log(title = "订单优惠券", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderCoupon orderCoupon)
    {
        List<OrderCoupon> list = orderCouponService.selectOrderCouponList(orderCoupon);
        ExcelUtil<OrderCoupon> util = new ExcelUtil<OrderCoupon>(OrderCoupon.class);
        util.exportExcel(response, list, "订单优惠券数据");
    }

    /**
     * 获取订单优惠券详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:coupon:query')")
    @GetMapping(value = "/{orderCouponId}")
    public AjaxResult getInfo(@PathVariable("orderCouponId") Long orderCouponId)
    {
        return success(orderCouponService.selectOrderCouponByOrderCouponId(orderCouponId));
    }

    /**
     * 新增订单优惠券
     */
    @PreAuthorize("@ss.hasPermi('order:coupon:add')")
    @Log(title = "订单优惠券", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderCoupon orderCoupon)
    {
        return toAjax(orderCouponService.insertOrderCoupon(orderCoupon));
    }

    /**
     * 修改订单优惠券
     */
    @PreAuthorize("@ss.hasPermi('order:coupon:edit')")
    @Log(title = "订单优惠券", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderCoupon orderCoupon)
    {
        return toAjax(orderCouponService.updateOrderCoupon(orderCoupon));
    }

    /**
     * 删除订单优惠券
     */
    @PreAuthorize("@ss.hasPermi('order:coupon:remove')")
    @Log(title = "订单优惠券", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderCouponIds}")
    public AjaxResult remove(@PathVariable Long[] orderCouponIds)
    {
        return toAjax(orderCouponService.deleteOrderCouponByOrderCouponIds(orderCouponIds));
    }
}
