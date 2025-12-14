package com.ruoyi.platform.rider.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.service.IOrderFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 骑手订单流转控制器
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@RestController
@RequestMapping("/rider/orderFlow")
public class RiderOrderFlowController extends BaseController {

    @Autowired
    private IOrderFlowService orderFlowService;

    /**
     * 骑手接单
     *
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Log(title = "骑手接单", businessType = BusinessType.UPDATE)
    @PostMapping("/accept/{orderMainId}")
    public AjaxResult acceptOrder(@PathVariable("orderMainId") Long orderMainId) {
        Long riderId = SecurityUtils.getRiderBaseId();

        // 调用服务层接单方法
        int result = orderFlowService.riderAcceptOrder(riderId, orderMainId);

        return toAjax(result);
    }

    /**
     * 骑手取货
     *
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Log(title = "骑手取货", businessType = BusinessType.UPDATE)
    @PostMapping("/pickup/{orderMainId}")
    public AjaxResult pickupOrder(@PathVariable("orderMainId") Long orderMainId) {
        Long riderId = SecurityUtils.getRiderBaseId();

        // 调用服务层取货方法
        int result = orderFlowService.riderPickupOrder(riderId, orderMainId);

        return toAjax(result);
    }

    /**
     * 骑手送达
     *
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Log(title = "骑手送达", businessType = BusinessType.UPDATE)
    @PostMapping("/deliver/{orderMainId}")
    public AjaxResult deliverOrder(@PathVariable("orderMainId") Long orderMainId) {
        Long riderId = SecurityUtils.getRiderBaseId();

        // 调用服务层送达方法
        int result = orderFlowService.riderDeliverOrder(riderId, orderMainId);

        return toAjax(result);
    }
}