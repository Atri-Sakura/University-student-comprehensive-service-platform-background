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
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.service.IOrderMainService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单主（整合地址与定位信息）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/order/main")
public class OrderMainController extends BaseController
{
    @Autowired
    private IOrderMainService orderMainService;

    /**
     * 查询订单主（整合地址与定位信息）列表
     */
    @PreAuthorize("@ss.hasPermi('order:main:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderMain orderMain)
    {
        startPage();
        List<OrderMain> list = orderMainService.selectOrderMainList(orderMain);
        return getDataTable(list);
    }

    /**
     * 导出订单主（整合地址与定位信息）列表
     */
    @PreAuthorize("@ss.hasPermi('order:main:export')")
    @Log(title = "订单主（整合地址与定位信息）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderMain orderMain)
    {
        List<OrderMain> list = orderMainService.selectOrderMainList(orderMain);
        ExcelUtil<OrderMain> util = new ExcelUtil<OrderMain>(OrderMain.class);
        util.exportExcel(response, list, "订单主（整合地址与定位信息）数据");
    }

    /**
     * 获取订单主（整合地址与定位信息）详细信息
     */
    @PreAuthorize("@ss.hasPermi('order:main:query')")
    @GetMapping(value = "/{orderMainId}")
    public AjaxResult getInfo(@PathVariable("orderMainId") Long orderMainId)
    {
        return success(orderMainService.selectOrderMainByOrderMainId(orderMainId));
    }

    /**
     * 新增订单主（整合地址与定位信息）
     */
    @PreAuthorize("@ss.hasPermi('order:main:add')")
    @Log(title = "订单主（整合地址与定位信息）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OrderMain orderMain)
    {
        return toAjax(orderMainService.insertOrderMain(orderMain));
    }

    /**
     * 修改订单主（整合地址与定位信息）
     */
    @PreAuthorize("@ss.hasPermi('order:main:edit')")
    @Log(title = "订单主（整合地址与定位信息）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OrderMain orderMain)
    {
        return toAjax(orderMainService.updateOrderMain(orderMain));
    }

    /**
     * 删除订单主（整合地址与定位信息）
     */
    @PreAuthorize("@ss.hasPermi('order:main:remove')")
    @Log(title = "订单主（整合地址与定位信息）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderMainIds}")
    public AjaxResult remove(@PathVariable Long[] orderMainIds)
    {
        return toAjax(orderMainService.deleteOrderMainByOrderMainIds(orderMainIds));
    }
}
