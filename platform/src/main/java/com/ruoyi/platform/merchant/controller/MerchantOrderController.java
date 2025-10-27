package com.ruoyi.platform.merchant.controller;

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
import com.ruoyi.platform.merchant.service.IMerchantOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 商家订单管理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/merchant/order")
public class MerchantOrderController extends BaseController
{
    @Autowired
    private IMerchantOrderService merchantOrderService;

    /**
     * 查询商家订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OrderMain orderMain)
    {
        startPage();
        // 从SecurityUtils获取当前登录的商家ID
        Long merchantId = SecurityUtils.getMerchantBaseId();
        List<OrderMain> list = merchantOrderService.selectMerchantOrderList(merchantId, orderMain);
        return getDataTable(list);
    }

    /**
     * 获取订单详细信息
     */
    @GetMapping(value = "/{orderMainId}")
    public AjaxResult getInfo(@PathVariable("orderMainId") Long orderMainId)
    {
        // 从SecurityUtils获取当前登录的商家ID
        Long merchantId = SecurityUtils.getMerchantBaseId();
        OrderMain order = merchantOrderService.selectMerchantOrderById(merchantId, orderMainId);
        return AjaxResult.success(order);
    }

    /**
     * 商家接单
     */
    @Log(title = "商家接单", businessType = BusinessType.UPDATE)
    @PutMapping("/accept/{orderMainId}")
    public AjaxResult accept(@PathVariable Long orderMainId)
    {
        // 从SecurityUtils获取当前登录的商家ID
        Long merchantId = SecurityUtils.getMerchantBaseId();
        return toAjax(merchantOrderService.acceptOrder(merchantId, orderMainId));
    }
}