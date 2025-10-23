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
import com.ruoyi.platform.domain.OrderDelivery;
import com.ruoyi.platform.service.IOrderDeliveryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单配送（含实际配送定位）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/order/delivery")
public class OrderDeliveryController extends BaseController
{
    @Autowired
    private IOrderDeliveryService orderDeliveryService;

    /**
     * 查询订单配送（含实际配送定位）列表
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderDelivery orderDelivery)
    {
        startPage();
        List<OrderDelivery> list = orderDeliveryService.selectOrderDeliveryList(orderDelivery);
        return getDataTable(list);
    }

    /**
     * 导出订单配送（含实际配送定位）列表
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:export')")
    @Log(title = "订单配送（含实际配送定位）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderDelivery orderDelivery)
    {
        List<OrderDelivery> list = orderDeliveryService.selectOrderDeliveryList(orderDelivery);
        ExcelUtil<OrderDelivery> util = new ExcelUtil<OrderDelivery>(OrderDelivery.class);
        util.exportExcel(response, list, "订单配送（含实际配送定位）数据");
    }

    /**
     * 获取订单配送（含实际配送定位）详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:query')")
    @GetMapping(value = "/{orderDeliveryId}")
    public AjaxResult getInfo(@PathVariable("orderDeliveryId") Long orderDeliveryId)
    {
        return success(orderDeliveryService.selectOrderDeliveryByOrderDeliveryId(orderDeliveryId));
    }

    /**
     * 新增订单配送（含实际配送定位）
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:add')")
    @Log(title = "订单配送（含实际配送定位）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderDelivery orderDelivery)
    {
        return toAjax(orderDeliveryService.insertOrderDelivery(orderDelivery));
    }

    /**
     * 修改订单配送（含实际配送定位）
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:edit')")
    @Log(title = "订单配送（含实际配送定位）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderDelivery orderDelivery)
    {
        return toAjax(orderDeliveryService.updateOrderDelivery(orderDelivery));
    }

    /**
     * 删除订单配送（含实际配送定位）
     */
    @PreAuthorize("@ss.hasPermi('order:delivery:remove')")
    @Log(title = "订单配送（含实际配送定位）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderDeliveryIds}")
    public AjaxResult remove(@PathVariable Long[] orderDeliveryIds)
    {
        return toAjax(orderDeliveryService.deleteOrderDeliveryByOrderDeliveryIds(orderDeliveryIds));
    }
}
