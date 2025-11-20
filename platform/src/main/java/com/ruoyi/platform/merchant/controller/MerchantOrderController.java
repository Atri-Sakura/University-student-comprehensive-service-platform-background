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

    @Autowired
    private IOrderFlowService orderFlowService;

    /**
     * 查询商家订单列表
     *
     * @param orderMain 查询条件
     * @return 订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OrderMain orderMain) {
        startPage();
        // 从SecurityUtils获取当前登录的商家ID
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
        // 从SecurityUtils获取当前登录的商家ID
        Long merchantId = SecurityUtils.getMerchantBaseId();
        OrderMain order = merchantOrderService.selectMerchantOrderById(merchantId, orderMainId);
        return AjaxResult.success(order);
    }

    /**
     * 商家接单（推荐使用新版本 /accept-v2）
     *
     * @param orderMainId 订单ID
     * @return 结果
     * @deprecated 建议使用 acceptOrderV2 (POST /accept-v2/{orderMainId})
     */
    @Deprecated
    @Log(title = "商家接单", businessType = BusinessType.UPDATE)
    @PutMapping("/accept/{orderMainId}")
    public AjaxResult accept(@PathVariable Long orderMainId) {
        // 从SecurityUtils获取当前登录的商家ID
        Long merchantId = SecurityUtils.getMerchantBaseId();
        return toAjax(merchantOrderService.acceptOrder(merchantId, orderMainId));
    }

    /**
     * 商家接单（新版本，使用订单流转服务）
     *
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Log(title = "商家接单", businessType = BusinessType.UPDATE)
    @PostMapping("/accept-v2/{orderMainId}")
    public AjaxResult acceptOrderV2(@PathVariable("orderMainId") Long orderMainId) {
        // 从SecurityUtils获取当前登录的商家ID
        Long merchantId = SecurityUtils.getMerchantBaseId();

        // 调用订单流转服务的接单方法
        int result = orderFlowService.merchantAcceptOrder(merchantId, orderMainId);

        return toAjax(result);
    }

    /**
     * 商家拒单（旧版本）
     *
     * @param orderMainId 订单ID
     * @return 结果
     * @deprecated 建议使用 rejectOrderV2 (POST /reject-v2/{orderMainId})
     */
    @Deprecated
    @Log(title = "商家拒单", businessType = BusinessType.UPDATE)
    @PutMapping("/reject/{orderMainId}")
    public AjaxResult reject(@PathVariable Long orderMainId) {
        Long merchantId = SecurityUtils.getMerchantBaseId();
        String operator = "merchant"; // 或获取当前商家名称
        return toAjax(merchantOrderService.rejectOrder(merchantId, orderMainId, operator));
    }

    /**
     * 商家拒单（新版本，使用订单流转服务）
     *
     * @param orderMainId 订单ID
     * @param refuseReason 拒单原因
     * @return 结果
     */
    @Log(title = "商家拒单", businessType = BusinessType.UPDATE)
    @PostMapping("/reject-v2/{orderMainId}")
    public AjaxResult rejectOrderV2(@PathVariable("orderMainId") Long orderMainId,
                                    @RequestParam("refuseReason") String refuseReason) {
        // 从SecurityUtils获取当前登录的商家ID
        Long merchantId = SecurityUtils.getMerchantBaseId();

        // 调用订单流转服务的拒单方法
        int result = orderFlowService.merchantRejectOrder(merchantId, orderMainId, refuseReason);

        return toAjax(result);
    }
}