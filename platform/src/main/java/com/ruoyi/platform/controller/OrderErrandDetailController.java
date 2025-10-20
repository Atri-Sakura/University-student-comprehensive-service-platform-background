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
import com.ruoyi.platform.domain.OrderErrandDetail;
import com.ruoyi.platform.service.IOrderErrandDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 跑腿订单明细（不含地址信息）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/order/errandDetail")
public class OrderErrandDetailController extends BaseController
{
    @Autowired
    private IOrderErrandDetailService orderErrandDetailService;

    /**
     * 查询跑腿订单明细（不含地址信息）列表
     */
    @PreAuthorize("@ss.hasPermi('order:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderErrandDetail orderErrandDetail)
    {
        startPage();
        List<OrderErrandDetail> list = orderErrandDetailService.selectOrderErrandDetailList(orderErrandDetail);
        return getDataTable(list);
    }

    /**
     * 导出跑腿订单明细（不含地址信息）列表
     */
    @PreAuthorize("@ss.hasPermi('order:detail:export')")
    @Log(title = "跑腿订单明细（不含地址信息）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderErrandDetail orderErrandDetail)
    {
        List<OrderErrandDetail> list = orderErrandDetailService.selectOrderErrandDetailList(orderErrandDetail);
        ExcelUtil<OrderErrandDetail> util = new ExcelUtil<OrderErrandDetail>(OrderErrandDetail.class);
        util.exportExcel(response, list, "跑腿订单明细（不含地址信息）数据");
    }

    /**
     * 获取跑腿订单明细（不含地址信息）详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:detail:query')")
    @GetMapping(value = "/{orderErrandDetailId}")
    public AjaxResult getInfo(@PathVariable("orderErrandDetailId") Long orderErrandDetailId)
    {
        return success(orderErrandDetailService.selectOrderErrandDetailByOrderErrandDetailId(orderErrandDetailId));
    }

    /**
     * 新增跑腿订单明细（不含地址信息）
     */
    @PreAuthorize("@ss.hasPermi('order:detail:add')")
    @Log(title = "跑腿订单明细（不含地址信息）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderErrandDetail orderErrandDetail)
    {
        return toAjax(orderErrandDetailService.insertOrderErrandDetail(orderErrandDetail));
    }

    /**
     * 修改跑腿订单明细（不含地址信息）
     */
    @PreAuthorize("@ss.hasPermi('order:detail:edit')")
    @Log(title = "跑腿订单明细（不含地址信息）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderErrandDetail orderErrandDetail)
    {
        return toAjax(orderErrandDetailService.updateOrderErrandDetail(orderErrandDetail));
    }

    /**
     * 删除跑腿订单明细（不含地址信息）
     */
    @PreAuthorize("@ss.hasPermi('order:detail:remove')")
    @Log(title = "跑腿订单明细（不含地址信息）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderErrandDetailIds}")
    public AjaxResult remove(@PathVariable Long[] orderErrandDetailIds)
    {
        return toAjax(orderErrandDetailService.deleteOrderErrandDetailByOrderErrandDetailIds(orderErrandDetailIds));
    }
}
