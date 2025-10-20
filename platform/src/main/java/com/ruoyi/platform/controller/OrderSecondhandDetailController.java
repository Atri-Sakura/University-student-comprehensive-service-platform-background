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
import com.ruoyi.platform.domain.OrderSecondhandDetail;
import com.ruoyi.platform.service.IOrderSecondhandDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 二手交易订单明细（不含地址信息）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/order/secondhandDetail")
public class OrderSecondhandDetailController extends BaseController
{
    @Autowired
    private IOrderSecondhandDetailService orderSecondhandDetailService;

    /**
     * 查询二手交易订单明细（不含地址信息）列表
     */
    @PreAuthorize("@ss.hasPermi('order:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderSecondhandDetail orderSecondhandDetail)
    {
        startPage();
        List<OrderSecondhandDetail> list = orderSecondhandDetailService.selectOrderSecondhandDetailList(orderSecondhandDetail);
        return getDataTable(list);
    }

    /**
     * 导出二手交易订单明细（不含地址信息）列表
     */
    @PreAuthorize("@ss.hasPermi('order:detail:export')")
    @Log(title = "二手交易订单明细（不含地址信息）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderSecondhandDetail orderSecondhandDetail)
    {
        List<OrderSecondhandDetail> list = orderSecondhandDetailService.selectOrderSecondhandDetailList(orderSecondhandDetail);
        ExcelUtil<OrderSecondhandDetail> util = new ExcelUtil<OrderSecondhandDetail>(OrderSecondhandDetail.class);
        util.exportExcel(response, list, "二手交易订单明细（不含地址信息）数据");
    }

    /**
     * 获取二手交易订单明细（不含地址信息）详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:detail:query')")
    @GetMapping(value = "/{orderSecondhandDetailId}")
    public AjaxResult getInfo(@PathVariable("orderSecondhandDetailId") Long orderSecondhandDetailId)
    {
        return success(orderSecondhandDetailService.selectOrderSecondhandDetailByOrderSecondhandDetailId(orderSecondhandDetailId));
    }

    /**
     * 新增二手交易订单明细（不含地址信息）
     */
    @PreAuthorize("@ss.hasPermi('order:detail:add')")
    @Log(title = "二手交易订单明细（不含地址信息）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderSecondhandDetail orderSecondhandDetail)
    {
        return toAjax(orderSecondhandDetailService.insertOrderSecondhandDetail(orderSecondhandDetail));
    }

    /**
     * 修改二手交易订单明细（不含地址信息）
     */
    @PreAuthorize("@ss.hasPermi('order:detail:edit')")
    @Log(title = "二手交易订单明细（不含地址信息）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderSecondhandDetail orderSecondhandDetail)
    {
        return toAjax(orderSecondhandDetailService.updateOrderSecondhandDetail(orderSecondhandDetail));
    }

    /**
     * 删除二手交易订单明细（不含地址信息）
     */
    @PreAuthorize("@ss.hasPermi('order:detail:remove')")
    @Log(title = "二手交易订单明细（不含地址信息）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderSecondhandDetailIds}")
    public AjaxResult remove(@PathVariable Long[] orderSecondhandDetailIds)
    {
        return toAjax(orderSecondhandDetailService.deleteOrderSecondhandDetailByOrderSecondhandDetailIds(orderSecondhandDetailIds));
    }
}
