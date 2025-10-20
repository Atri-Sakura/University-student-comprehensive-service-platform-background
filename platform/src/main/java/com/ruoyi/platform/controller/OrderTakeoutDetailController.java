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
import com.ruoyi.platform.domain.OrderTakeoutDetail;
import com.ruoyi.platform.service.IOrderTakeoutDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 外卖订单明细（不含地址信息）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/order/detail")
public class OrderTakeoutDetailController extends BaseController
{
    @Autowired
    private IOrderTakeoutDetailService orderTakeoutDetailService;

    /**
     * 查询外卖订单明细（不含地址信息）列表
     */
    @PreAuthorize("@ss.hasPermi('order:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderTakeoutDetail orderTakeoutDetail)
    {
        startPage();
        List<OrderTakeoutDetail> list = orderTakeoutDetailService.selectOrderTakeoutDetailList(orderTakeoutDetail);
        return getDataTable(list);
    }

    /**
     * 导出外卖订单明细（不含地址信息）列表
     */
    @PreAuthorize("@ss.hasPermi('order:detail:export')")
    @Log(title = "外卖订单明细（不含地址信息）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderTakeoutDetail orderTakeoutDetail)
    {
        List<OrderTakeoutDetail> list = orderTakeoutDetailService.selectOrderTakeoutDetailList(orderTakeoutDetail);
        ExcelUtil<OrderTakeoutDetail> util = new ExcelUtil<OrderTakeoutDetail>(OrderTakeoutDetail.class);
        util.exportExcel(response, list, "外卖订单明细（不含地址信息）数据");
    }

    /**
     * 获取外卖订单明细（不含地址信息）详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:detail:query')")
    @GetMapping(value = "/{orderTakeoutDetailId}")
    public AjaxResult getInfo(@PathVariable("orderTakeoutDetailId") Long orderTakeoutDetailId)
    {
        return success(orderTakeoutDetailService.selectOrderTakeoutDetailByOrderTakeoutDetailId(orderTakeoutDetailId));
    }

    /**
     * 新增外卖订单明细（不含地址信息）
     */
    @PreAuthorize("@ss.hasPermi('order:detail:add')")
    @Log(title = "外卖订单明细（不含地址信息）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderTakeoutDetail orderTakeoutDetail)
    {
        return toAjax(orderTakeoutDetailService.insertOrderTakeoutDetail(orderTakeoutDetail));
    }

    /**
     * 修改外卖订单明细（不含地址信息）
     */
    @PreAuthorize("@ss.hasPermi('order:detail:edit')")
    @Log(title = "外卖订单明细（不含地址信息）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderTakeoutDetail orderTakeoutDetail)
    {
        return toAjax(orderTakeoutDetailService.updateOrderTakeoutDetail(orderTakeoutDetail));
    }

    /**
     * 删除外卖订单明细（不含地址信息）
     */
    @PreAuthorize("@ss.hasPermi('order:detail:remove')")
    @Log(title = "外卖订单明细（不含地址信息）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderTakeoutDetailIds}")
    public AjaxResult remove(@PathVariable Long[] orderTakeoutDetailIds)
    {
        return toAjax(orderTakeoutDetailService.deleteOrderTakeoutDetailByOrderTakeoutDetailIds(orderTakeoutDetailIds));
    }
}
