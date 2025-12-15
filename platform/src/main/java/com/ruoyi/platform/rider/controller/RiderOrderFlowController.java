package com.ruoyi.platform.rider.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.service.IOrderFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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

        return AjaxResult.success("骑手接单成功", orderMainId);
    }

    /**
     * 骑手取货
     *
     * @param orderMainId 订单ID
     * @param actualPickLongitude 实际取货经度
     * @param actualPickLatitude 实际取货纬度
     */
    @Log(title = "骑手取货", businessType = BusinessType.UPDATE)
    @PostMapping("/pickup/{orderMainId}")
    public AjaxResult pickupOrder(
            @PathVariable("orderMainId") Long orderMainId,
            @RequestParam("actualPickLongitude") BigDecimal actualPickLongitude,
            @RequestParam("actualPickLatitude") BigDecimal actualPickLatitude) {

        Long riderId = SecurityUtils. getRiderBaseId();

        // 参数校验
        if (actualPickLongitude == null || actualPickLatitude == null) {
            return AjaxResult.error("请开启定位权限");
        }

        // 调用服务层
        int result = orderFlowService.riderPickupOrder(
                riderId,
                orderMainId,
                actualPickLongitude,
                actualPickLatitude
        );

        return result > 0 ? AjaxResult.success("取货成功") : AjaxResult.error("取货失败");
    }

    /**
     * 骑手送达
     *
     * @param orderMainId 订单ID
     * @param actualDeliverLongitude 实际送达经度
     * @param actualDeliverLatitude 实际送达纬度
     */
    @Log(title = "骑手送达", businessType = BusinessType.UPDATE)
    @PostMapping("/deliver/{orderMainId}")
    public AjaxResult deliverOrder(
            @PathVariable("orderMainId") Long orderMainId,
            @RequestParam("actualDeliverLongitude") BigDecimal actualDeliverLongitude,
            @RequestParam("actualDeliverLatitude") BigDecimal actualDeliverLatitude) {

        Long riderId = SecurityUtils.getRiderBaseId();

        // 参数校验
        if (actualDeliverLongitude == null || actualDeliverLatitude == null) {
            return AjaxResult.error("请开启定位权限");
        }

        // 调用服务层
        int result = orderFlowService.riderDeliverOrder(
                riderId,
                orderMainId,
                actualDeliverLongitude,
                actualDeliverLatitude
        );

        return result > 0 ? AjaxResult. success("送达成功") : AjaxResult.error("送达失败");
    }
}