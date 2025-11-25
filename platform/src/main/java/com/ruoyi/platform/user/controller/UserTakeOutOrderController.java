package com.ruoyi.platform.user.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.dto.CreateOrderDTO;
import com.ruoyi.platform.domain.dto.PayOrderDTO;
import com.ruoyi.platform.mapper.OrderMainMapper;
import com.ruoyi.platform.service.IUserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户订单控制器
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@RestController
@RequestMapping("/user/order")
public class UserTakeOutOrderController extends BaseController {

    @Autowired
    private IUserOrderService userOrderService;

    @Autowired
    private OrderMainMapper orderMainMapper;

    /**
     * 创建预支付订单
     *
     * @param createOrderDTO 订单创建DTO
     * @return 预支付订单信息
     */
    @Log(title = "创建预支付订单", businessType = BusinessType.INSERT)
    @PostMapping("/prepay")
    public AjaxResult createPrePayOrder(@RequestBody @Validated CreateOrderDTO createOrderDTO) {
        Long userId = SecurityUtils.getUserBaseId();
        String userNickname = SecurityUtils.getUsername();

        createOrderDTO.setUserId(userId);
        createOrderDTO.setUserNickname(userNickname);

        return AjaxResult.success("订单信息已提交，请在15分钟内完成支付",
                userOrderService.createPrePayOrder(createOrderDTO));
    }

    /**
     * 支付并创建订单
     *
     * @param payOrderDTO 支付订单DTO
     * @return 订单信息
     */
    @Log(title = "支付并创建订单", businessType = BusinessType.INSERT)
    @PostMapping("/pay-and-create")
    public AjaxResult payAndCreateOrder(@RequestBody @Validated PayOrderDTO payOrderDTO) {
        Long userId = SecurityUtils.getUserBaseId();
        OrderMain order = userOrderService.payAndCreateOrder(userId, payOrderDTO);
        return AjaxResult.success("支付成功，订单已创建", order);
    }

    /**
     * 取消预支付订单
     *
     * @param preOrderNo 预订单号
     * @return 结果
     */
    @Log(title = "取消预支付订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/prepay/{preOrderNo}")
    public AjaxResult cancelPrePayOrder(@PathVariable("preOrderNo") String preOrderNo) {
        Long userId = SecurityUtils.getUserBaseId();
        boolean result = userOrderService.cancelPrePayOrder(userId, preOrderNo);
        return result ? AjaxResult.success("已取消") : AjaxResult.error("取消失败");
    }

    /**
     * 用户取消订单
     *
     * @param orderMainId 订单ID
     * @param cancelReason 取消原因
     * @return 结果
     */
    @Log(title = "用户取消订单", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel/{orderMainId}")
    public AjaxResult cancelOrder(@PathVariable("orderMainId") Long orderMainId,
                                  @RequestParam("cancelReason") String cancelReason) {
        Long userId = SecurityUtils.getUserBaseId();
        return toAjax(userOrderService.cancelOrder(userId, orderMainId, cancelReason));
    }

    /**
     * 用户确认收货
     *
     * @param orderMainId 订单ID
     * @return 结果
     */
    @Log(title = "用户确认收货", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm/{orderMainId}")
    public AjaxResult confirmReceive(@PathVariable("orderMainId") Long orderMainId) {
        Long userId = SecurityUtils.getUserBaseId();
        return toAjax(userOrderService.confirmReceive(userId, orderMainId));
    }

    /**
     * 查询用户自己的订单列表
     *
     * @param orderMain 查询条件
     * @return 订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OrderMain orderMain) {
        startPage();
        Long userId = SecurityUtils.getUserBaseId();
        orderMain.setUserId(userId);
        List<OrderMain> list = orderMainMapper.selectOrderMainList(orderMain);
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
        Long userId = SecurityUtils.getUserBaseId();
        OrderMain order = orderMainMapper.selectOrderMainWithDetailsByOrderMainId(orderMainId);

        // 权限校验
        if (order == null || !order.getUserId().equals(userId)) {
            return AjaxResult.error("订单不存在或无权查看");
        }

        return AjaxResult.success(order);
    }
    // TODO 查看三种订单详情接口
}