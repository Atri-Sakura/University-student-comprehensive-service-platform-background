package com.ruoyi.platform.rider.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.rider.domain.vo.RiderOrderListVO;
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
        // 修改为 RiderOrderListVO
        List<RiderOrderListVO> list = riderOrderService.selectAvailableOrderList(orderMain);
        return getDataTable(list);
    }

    /**
     * 查询骑手自己的订单列表
     *
     * @param orderMain 查询条件
     * @param timeRange 时间范围: today-今日, yesterday-昨日, week-本周, month-本月
     * @return 订单列表
     */
    @GetMapping("/myOrders")
    public TableDataInfo listMyOrders(OrderMain orderMain,
                                      @RequestParam(required = false) String timeRange) {
        Long riderId = SecurityUtils.getRiderBaseId();

        List<RiderOrderListVO> list = riderOrderService.selectRiderOrderList(riderId, orderMain, timeRange);
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
        Long riderId = SecurityUtils.getRiderBaseId();

        OrderMain order = riderOrderService.selectRiderOrderById(riderId, orderMainId);
        return AjaxResult.success(order);
    }

    /**
     * 按时间范围统计订单数量
     *
     * @return 统计结果
     */
    @GetMapping("/statistics")
    public AjaxResult getOrderStatistics() {
        Long riderId = SecurityUtils.getRiderBaseId();
        return AjaxResult.success(riderOrderService.getOrderStatistics(riderId));
    }
}