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
import com.ruoyi.platform.domain.vo.CreateErrandOrderDto;
import com.ruoyi.platform.mapper.OrderMainMapper;
import com.ruoyi.platform.service.IUserOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/errandOrder")
@Slf4j
public class UserErrandOrderController extends BaseController {

    @Autowired
    private IUserOrderService userOrderService;

    @Autowired
    private OrderMainMapper orderMainMapper;

    /**
     * 创建跑腿预支付订单
     * @param
     * @return
     */
    @Log(title = "创建预支付跑腿订单", businessType = BusinessType.INSERT)
    @PostMapping("/prepay")
    public AjaxResult createPayOrder(@RequestBody @Validated CreateErrandOrderDto createErrandOrderDTO) {
        Long userId = SecurityUtils.getUserBaseId();
        String userNickname = SecurityUtils.getUsername();
        createErrandOrderDTO.setUserId(userId);
        createErrandOrderDTO.setUserNickname(userNickname);
        return AjaxResult.success("订单信息已提交，请在15分钟内完成支付",
                userOrderService.createPrePayErrandOrder(createErrandOrderDTO));
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

        log.info("{}",userId);
        OrderMain order = userOrderService.payAndCreateErrandOrder(userId, payOrderDTO,payOrderDTO.getUserAddressId());
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
        boolean result = userOrderService.cancelPrePayErrandOrder(userId, preOrderNo);
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
    /**
     * 用户确认收货
     *
     * @param orderMainId 订单ID
     * @param riderId 骑手ID
     * @return 结果
     */
    @Log(title = "用户确认收货", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm/{orderMainId}")
    public AjaxResult confirmReceive(@PathVariable("orderMainId") Long orderMainId,
                                     @RequestParam(value = "riderId", required = false) Long riderId) {
        Long userId = SecurityUtils.getUserBaseId();
        return toAjax(userOrderService.confirmReceiveErrand(userId, orderMainId, riderId));
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




}
