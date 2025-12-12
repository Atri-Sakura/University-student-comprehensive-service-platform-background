package com.ruoyi.platform.user.aop;

import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.service.IOrderNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单通知AOP：拦截支付创建订单方法，执行后发送消息通知
 * @author ruoyi
 * @date 2025-12-12
 */
@Aspect
@Component
@Slf4j
public class OrderAspect {

    @Autowired
    private IOrderNotifyService orderNotifyService;

    /**
     * 切点1：拦截外卖订单支付创建方法
     * 匹配 UserTakeOutOrderController 的 payAndCreateOrder 方法
     */
    @Pointcut("execution(* com.ruoyi.platform.user.controller.UserTakeOutOrderController.payAndCreateOrder(..))")
    public void takeoutOrderPayPointcut() {}

    /**
     * 切点2：（可选）拦截跑腿订单支付创建方法（如果有单独的Controller）
     * 可根据实际包路径调整
     */
    @Pointcut("execution(* com.ruoyi.platform.user.controller.UserErrandOrderController.payAndCreateOrder(..))")
    public void errandOrderPayPointcut() {}

    /**
     * 外卖订单支付成功后通知
     * AfterReturning：方法正常返回后执行，可获取返回值
     */
    @AfterReturning(
            pointcut = "takeoutOrderPayPointcut()",
            returning = "result" // 接收方法返回的 AjaxResult 对象
    )
    public void afterTakeoutOrderPay(Object result) {
        try {
            // 1. 解析返回结果，获取订单信息
            if (result instanceof com.ruoyi.common.core.domain.AjaxResult ajaxResult) {
                Object data = ajaxResult.getData();
                if (data instanceof OrderMain orderMain) {
                    Long userId = orderMain.getUserId();
                    // 2. 发送用户下单成功通知
                    orderNotifyService.sendUserOrderSuccessNotify(orderMain, userId);
                    log.info("AOP拦截外卖订单支付成功，已发送通知，订单号：{}", orderMain.getOrderNo());
                }
            }
        } catch (Exception e) {
            log.error("外卖订单支付后发送通知失败", e);
            // 通知发送失败不影响主流程，仅记录日志
        }
    }

    /**
     * 跑腿订单支付成功后通知（可选）
     */
    @AfterReturning(
            pointcut = "errandOrderPayPointcut()",
            returning = "result"
    )
    public void afterErrandOrderPay(Object result) {
        try {
            if (result instanceof com.ruoyi.common.core.domain.AjaxResult ajaxResult) {
                Object data = ajaxResult.getData();
                if (data instanceof OrderMain orderMain) {
                    Long userId = orderMain.getUserId();
                    // 发送跑腿订单通知
                    orderNotifyService.sendUserOrderSuccessNotify(orderMain, userId);
                    log.info("AOP拦截跑腿订单支付成功，已发送通知，订单号：{}", orderMain.getOrderNo());
                }
            }
        } catch (Exception e) {
            log.error("跑腿订单支付后发送通知失败", e);
        }
    }
}