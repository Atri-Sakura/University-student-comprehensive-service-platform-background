package com.ruoyi.platform.rider.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.rider.service.IRiderOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 骑手订单查询控制器
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@RestController
@RequestMapping("/rider/order")
public class RiderOrderController extends BaseController {

    @Autowired
    private IRiderOrderService riderOrderService;

    /**
     * 查询可接单的订单列表（待取货状态且未被接单）
     *
     * @param orderMain 查询条件
     * @return 订单列表
     */
    @GetMapping("/available")
    public TableDataInfo listAvailableOrders(OrderMain orderMain) {
        startPage();
        List<OrderMain> list = riderOrderService.selectAvailableOrderList(orderMain);
        return getDataTable(list);
    }

    /**
     * 查询骑手自己的订单列表
     *
     * @param orderMain 查询条件
     * @return 订单列表
     */
    @GetMapping("/myOrders")
    public TableDataInfo listMyOrders(OrderMain orderMain) {
        startPage();
        // 从SecurityUtils获取当前登录的骑手ID
        Long riderId = SecurityUtils.getRiderBaseId();

        List<OrderMain> list = riderOrderService.selectRiderOrderList(riderId, orderMain);
        return getDataTable(list);
    }

    /**
     * 查询订单详情
     *
     * @param orderMainId 订单ID
     * @return 订单详情
     */
    @GetMapping("/{orderMainId}")
    public AjaxResult getOrderDetail(@PathVariable("orderMainId") Long orderMainId) {
        // 从SecurityUtils获取当前登录的骑手ID
        Long riderId = SecurityUtils.getRiderBaseId();

        OrderMain order = riderOrderService.selectRiderOrderById(riderId, orderMainId);
        return AjaxResult.success(order);
    }
}