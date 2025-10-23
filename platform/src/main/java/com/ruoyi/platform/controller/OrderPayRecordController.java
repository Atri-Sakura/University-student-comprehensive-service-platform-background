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
import com.ruoyi.platform.domain.OrderPayRecord;
import com.ruoyi.platform.service.IOrderPayRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单支付记录Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/order/record")
public class OrderPayRecordController extends BaseController
{
    @Autowired
    private IOrderPayRecordService orderPayRecordService;

    /**
     * 查询订单支付记录列表
     */
    @PreAuthorize("@ss.hasPermi('order:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderPayRecord orderPayRecord)
    {
        startPage();
        List<OrderPayRecord> list = orderPayRecordService.selectOrderPayRecordList(orderPayRecord);
        return getDataTable(list);
    }

    /**
     * 导出订单支付记录列表
     */
    @PreAuthorize("@ss.hasPermi('order:record:export')")
    @Log(title = "订单支付记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderPayRecord orderPayRecord)
    {
        List<OrderPayRecord> list = orderPayRecordService.selectOrderPayRecordList(orderPayRecord);
        ExcelUtil<OrderPayRecord> util = new ExcelUtil<OrderPayRecord>(OrderPayRecord.class);
        util.exportExcel(response, list, "订单支付记录数据");
    }

    /**
     * 获取订单支付记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:record:query')")
    @GetMapping(value = "/{orderPayRecordId}")
    public AjaxResult getInfo(@PathVariable("orderPayRecordId") Long orderPayRecordId)
    {
        return success(orderPayRecordService.selectOrderPayRecordByOrderPayRecordId(orderPayRecordId));
    }

    /**
     * 新增订单支付记录
     */
    @PreAuthorize("@ss.hasPermi('order:record:add')")
    @Log(title = "订单支付记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderPayRecord orderPayRecord)
    {
        return toAjax(orderPayRecordService.insertOrderPayRecord(orderPayRecord));
    }

    /**
     * 修改订单支付记录
     */
    @PreAuthorize("@ss.hasPermi('order:record:edit')")
    @Log(title = "订单支付记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderPayRecord orderPayRecord)
    {
        return toAjax(orderPayRecordService.updateOrderPayRecord(orderPayRecord));
    }

    /**
     * 删除订单支付记录
     */
    @PreAuthorize("@ss.hasPermi('order:record:remove')")
    @Log(title = "订单支付记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderPayRecordIds}")
    public AjaxResult remove(@PathVariable Long[] orderPayRecordIds)
    {
        return toAjax(orderPayRecordService.deleteOrderPayRecordByOrderPayRecordIds(orderPayRecordIds));
    }
}
