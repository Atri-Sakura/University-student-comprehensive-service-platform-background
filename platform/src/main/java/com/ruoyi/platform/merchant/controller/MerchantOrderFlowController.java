package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.service.IOrderFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商家订单流转控制器
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@RestController
@RequestMapping("/merchant/orderFlow")
public class MerchantOrderFlowController extends BaseController {

    @Autowired
    private IOrderFlowService orderFlowService;

    /**
     * 商家接单
     *
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Log(title = "商家接单", businessType = BusinessType.UPDATE)
    @PostMapping("/accept/{orderMainId}")
    public AjaxResult acceptOrder(@PathVariable("orderMainId") Long orderMainId) {
        // 从SecurityUtils获取当前登录的商家ID
        Long merchantId = SecurityUtils.getMerchantBaseId();

        // 调用服务层接单方法
        int result = orderFlowService.merchantAcceptOrder(merchantId, orderMainId);

        return toAjax(result);
    }

    /**
     * 商家拒单
     *
     * @param orderMainId 订单ID
     * @param refuseReason 拒单原因
     * @return 结果
     */
    @Log(title = "商家拒单", businessType = BusinessType.UPDATE)
    @PostMapping("/reject/{orderMainId}")
    public AjaxResult rejectOrder(@PathVariable("orderMainId") Long orderMainId,
                                  @RequestParam("refuseReason") String refuseReason) {
        // 从SecurityUtils获取当前登录的商家ID
        Long merchantId = SecurityUtils.getMerchantBaseId();

        // 调用服务层拒单方法
        int result = orderFlowService.merchantRejectOrder(merchantId, orderMainId, refuseReason);

        return toAjax(result);
    }
}