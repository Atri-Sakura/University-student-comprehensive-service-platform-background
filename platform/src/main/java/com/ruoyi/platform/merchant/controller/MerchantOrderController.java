package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.merchant.service.IMerchantOrderService;
import com.ruoyi.platform.service.IOrderFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商家订单管理控制器（合并订单流转功能）
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@RestController
@RequestMapping("/merchant/order")
public class MerchantOrderController extends BaseController {

    @Autowired
    private IMerchantOrderService merchantOrderService;

    /**
     * 查询商家订单列表
     *
     * @param orderMain 查询条件
     * @return 订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OrderMain orderMain) {
        startPage();
        Long merchantId = SecurityUtils.getMerchantBaseId();
        List<OrderMain> list = merchantOrderService.selectMerchantOrderList(merchantId, orderMain);
        return getDataTable(list);
    }

    /**
     * 获取订单详细信息
     *
     * @param orderMainId 订单ID
     * @return 订单详情
     */
    @GetMapping("/{orderMainId}")
    public AjaxResult getInfo(@PathVariable("orderMainId") Long orderMainId) {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        OrderMain order = merchantOrderService.selectMerchantOrderById(merchantId, orderMainId);
        return AjaxResult.success(order);
    }

    /**
     * 商家接单
     *
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Deprecated
    @Log(title = "商家接单", businessType = BusinessType.UPDATE)
    @PutMapping("/accept/{orderMainId}")
    public AjaxResult accept(@PathVariable Long orderMainId) {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        return toAjax(merchantOrderService.acceptOrder(merchantId, orderMainId));
    }


    /**
     * 商家拒单
     *
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Deprecated
    @Log(title = "商家拒单", businessType = BusinessType.UPDATE)
    @PutMapping("/reject/{orderMainId}")
    public AjaxResult reject(@PathVariable Long orderMainId) {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        String operator = "merchant";
        return toAjax(merchantOrderService.rejectOrder(merchantId, orderMainId, operator));
    }
}