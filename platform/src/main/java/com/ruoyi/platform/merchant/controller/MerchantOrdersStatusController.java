package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.merchant.vo.MerchantOrderStatusVO;
import com.ruoyi.platform.merchant.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant/orders")
public class MerchantOrdersStatusController {

    @Autowired
    private IOrderService orderService;

    //商家首页订单状态
    @GetMapping("/status")
    public AjaxResult status() {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        MerchantOrderStatusVO merchantOrderStatusVO = orderService.getMerchantOrderStatus(merchantBaseId);
        return AjaxResult.success(merchantOrderStatusVO);
    }
}
