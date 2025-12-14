package com.ruoyi.platform.user.aop;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.service.IOrderMainService;
import com.ruoyi.platform.service.IOrderNotifyService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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

    private final IOrderNotifyService orderNotifyService;
    private final IOrderMainService orderMainService;
    private final ThreadPoolTaskExecutor asyncExecutor;

    // 构造器注入（解决字段注入警告+线程池Bean冲突）
    @Autowired
    public OrderAspect(IOrderNotifyService orderNotifyService,
                       IOrderMainService orderMainService,
                       @Qualifier("threadPoolTaskExecutor") ThreadPoolTaskExecutor asyncExecutor) {
        this.orderNotifyService = orderNotifyService;
        this.orderMainService = orderMainService;
        this.asyncExecutor = asyncExecutor;
    }

    /**
     * 切点1：拦截外卖订单支付创建方法
     */
    @Pointcut("execution(* com.ruoyi.platform.user.controller.UserTakeOutOrderController.payAndCreateOrder(..))")
    public void takeoutOrderPayPointcut() {}

    /**
     * 切点2：拦截跑腿订单支付创建方法
     */
    @Pointcut("execution(* com.ruoyi.platform.user.controller.UserErrandOrderController.payAndCreateOrder(..))")
    public void errandOrderPayPointcut() {}

    /**
     * 切点3：拦截二手交易订单创建方法
     */
    @Pointcut("execution(* com.ruoyi.platform.controller1.user.UserSecondhandGoodsController1.createSecondhandOrder(..))")
    public void secondhandOrderPayPointcut() {}

    /**
     * 切点4：拦截商家接单接口
     */
    @Pointcut("execution(* com.ruoyi.platform.merchant.controller.MerchantOrderController.acceptOrderV2(..))")
    public void merchantAcceptOrderPointcut() {}

    /**
     * 切点5：拦截骑手接单接口
     */
    @Pointcut("execution(* com.ruoyi.platform.rider.controller.RiderOrderFlowController.acceptOrder(..))")
    public void riderAcceptOrderPointcut() {}

    // ------------------------------ 外卖订单支付通知 ------------------------------
    @AfterReturning(pointcut = "takeoutOrderPayPointcut()", returning = "result")
    public void afterTakeoutOrderPay(Object result) {
        asyncExecutor.execute(() -> {
            try {
                if (result instanceof AjaxResult ajaxResult && ajaxResult.getData() instanceof OrderMain orderMain) {
                    // 给买家发下单成功通知
                    orderNotifyService.sendUserOrderSuccessNotify(orderMain, orderMain.getUserId());
                    // 给商家发新订单提醒（复用已实现的商家通知方法）
                    if (orderMain.getMerchantId() != null && orderMain.getPayStatus() == 1) {
                        orderNotifyService.sendTakeoutOrderNotify(orderMain, null);
                    }
                    log.info("AOP异步发送外卖订单通知完成，订单号：{}，买家ID：{}，商家ID：{}",
                            orderMain.getOrderNo(), orderMain.getUserId(), orderMain.getMerchantId());
                }
            } catch (Exception e) {
                log.error("外卖订单支付通知发送失败", e);
            }
        });
    }

    // ------------------------------ 跑腿订单支付通知 ------------------------------
    @AfterReturning(pointcut = "errandOrderPayPointcut()", returning = "result")
    public void afterErrandOrderPay(Object result) {
        asyncExecutor.execute(() -> {
            try {
                if (result instanceof AjaxResult ajaxResult && ajaxResult.getData() instanceof OrderMain orderMain) {
                    // 给下单用户发通知
                    if(orderMain.getMerchantId() != null && orderMain.getPayStatus() == 1) {
                        orderNotifyService.sendUserOrderSuccessNotify(orderMain, orderMain.getUserId());
                        log.info("AOP异步发送跑腿订单通知完成，订单号：{}，用户ID：{}",
                                orderMain.getOrderNo(), orderMain.getUserId());
                    }
                }
            } catch (Exception e) {
                log.error("跑腿订单支付通知发送失败", e);
            }
        });
    }

    // ------------------------------ 二手订单创建通知 ------------------------------
    @AfterReturning(pointcut = "secondhandOrderPayPointcut()", returning = "result")
    public void afterSecondhandOrderPay(Object result) {
        asyncExecutor.execute(() -> {
            try {
                if (result instanceof AjaxResult ajaxResult) {
                    Object data = ajaxResult.getData();
                    OrderMain orderMain = null;
                    if (data instanceof String orderNo) {
                        orderMain = orderMainService.selectByOrderNo(orderNo);
                    } else if (data instanceof OrderMain) {
                        orderMain = (OrderMain) data;
                    }
                    if (orderMain != null) {
                        orderNotifyService.sendUserOrderSuccessNotify(orderMain, orderMain.getUserId());
                        log.info("AOP异步发送二手订单通知完成，订单号：{}，用户ID：{}",
                                orderMain.getOrderNo(), orderMain.getUserId());
                    }
                }
            } catch (Exception e) {
                log.error("二手订单创建通知发送失败", e);
            }
        });
    }

    // ------------------------------ 商家接单通知 ------------------------------
    @AfterReturning(pointcut = "merchantAcceptOrderPointcut()", returning = "result")
    public void afterMerchantAcceptOrder(Object result) {
        asyncExecutor.execute(() -> {
            try {
                if (result instanceof AjaxResult ajaxResult && ajaxResult.isSuccess()) {
                    OrderMain orderMain = (OrderMain)ajaxResult.getData();
                    Long orderMainId = orderMain.getOrderMainId();
                    Long merchantId = orderMainService.selectOrderMainByOrderMainId(orderMainId).getMerchantId();
                    // 调用商家接单通知方法
                    orderNotifyService.sendMerchantAcceptOrderToUserNotify(orderMainId, merchantId);
                    log.info("AOP异步发送商家接单通知完成，订单ID：{}，商家ID：{}", orderMainId, merchantId);
                }
            } catch (Exception e) {
                log.error("商家接单通知发送失败", e);
            }
        });
    }

    // ------------------------------ 骑手接单通知 ------------------------------
    @AfterReturning(pointcut = "riderAcceptOrderPointcut()", returning = "result")
    public void afterRiderAcceptOrder(Object result) {
        asyncExecutor.execute(() -> {
            try {
                if (result instanceof AjaxResult ajaxResult && ajaxResult.isSuccess()) {
                    // 解析订单ID（生产建议用JoinPoint获取PathVariable）
                    OrderMain orderMain = (OrderMain)ajaxResult.getData();
                    Long orderMainId = orderMain.getOrderMainId();
                    Long riderId = SecurityUtils.getRiderBaseId(); // 从上下文获取骑手ID
                    // 调用骑手接单通知方法
                    orderNotifyService.sendPickOrderToUserNotify(riderId, orderMainId);
                    log.info("AOP异步发送骑手接单通知完成，订单ID：{}，骑手ID：{}", orderMainId, riderId);
                }
            } catch (Exception e) {
                log.error("骑手接单通知发送失败", e);
            }
        });
    }

    // 预留方法（保持原有结构）
    public void sendMerchantAcceptOrderToUserNotify(Object result) {}
    public void sendRiderAcceptOrderToUserNotify(Object result) {}
}