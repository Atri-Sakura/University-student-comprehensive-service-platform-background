package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.merchant.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;


    //接单
    @PutMapping("/accept/{orderNoId}")
    public AjaxResult acceptOrder(@PathVariable Long orderNoId) {
        orderService.acceptOrder(orderNoId);
        return AjaxResult.success();
    }

    //批量接单
    @PutMapping("/acceptBatch")
    public AjaxResult acceptOrderBatch(@RequestBody Long[] orderNoIds) {
        if (orderNoIds == null || orderNoIds.length == 0) {
            return AjaxResult.error("订单号数组不能为空");
        }
        orderService.acceptOrderBatch(orderNoIds);
        return AjaxResult.success();
    }
}
