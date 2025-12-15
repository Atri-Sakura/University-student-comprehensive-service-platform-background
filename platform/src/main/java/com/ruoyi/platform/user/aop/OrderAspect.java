package com.ruoyi.platform.user.aop;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.domain.OrderDelivery;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.mapper.OrderDeliveryMapper;
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
    private final OrderDeliveryMapper orderDeliveryMapper;

    // 构造器注入（解决字段注入警告+线程池Bean冲突）
    @Autowired
    public OrderAspect(IOrderNotifyService orderNotifyService,
                       IOrderMainService orderMainService,
                       @Qualifier("threadPoolTaskExecutor") ThreadPoolTaskExecutor asyncExecutor,
                       OrderDeliveryMapper orderDeliveryMapper) {
        this.orderNotifyService = orderNotifyService;
        this.orderMainService = orderMainService;
        this.asyncExecutor = asyncExecutor;
        this.orderDeliveryMapper = orderDeliveryMapper;
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

    // ========== 新增：骑手取货切点 ==========
    @Pointcut("execution(* com.ruoyi.platform.rider.controller.RiderOrderFlowController.pickupOrder(..))")
    public void riderPickupOrderPointcut() {}

    // ========== 新增：用户确认收货（订单完成）切点 ==========
    @Pointcut("execution(* com.ruoyi.platform.user.controller.UserTakeOutOrderController.confirmReceive(..))")
    public void userConfirmReceivePointcut() {}

    // ------------------------------ 原有逻辑保持不变 ------------------------------
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
                    } else {
                        log.warn("二手订单创建通知：未获取到有效订单信息，返回数据：{}", data);
                    }
                }
            } catch (Exception e) {
                log.error("二手订单创建通知发送失败", e);
            }
        });
    }

    @AfterReturning(pointcut = "merchantAcceptOrderPointcut()", returning = "result")
    public void afterMerchantAcceptOrder(Object result) {
        asyncExecutor.execute(() -> {
            try {
                if (result instanceof AjaxResult ajaxResult && ajaxResult.isSuccess()) {
                    // 增加orderMainId非空校验
                    Object data = ajaxResult.getData();
                    if (!(data instanceof Long orderMainId) || orderMainId == null) {
                        log.warn("商家接单通知：订单ID为空，返回数据：{}", data);
                        return;
                    }

                    OrderMain orderMain = orderMainService.selectOrderMainByOrderMainId(orderMainId);
                    if (orderMain == null) {
                        log.warn("商家接单通知：未找到订单信息，订单ID：{}", orderMainId);
                        return;
                    }

                    Long merchantId = orderMain.getMerchantId();
                    if (merchantId == null) {
                        log.warn("商家接单通知：订单{}的商家ID为空", orderMainId);
                        return;
                    }

                    // 调用商家接单通知方法
                    orderNotifyService.sendMerchantAcceptOrderToUserNotify(orderMainId, merchantId);
                    log.info("AOP异步发送商家接单通知完成，订单ID：{}，商家ID：{}", orderMainId, merchantId);
                }
            } catch (Exception e) {
                log.error("商家接单通知发送失败", e);
            }
        });
    }

    @AfterReturning(pointcut = "riderAcceptOrderPointcut()", returning = "result")
    public void afterRiderAcceptOrder(Object result) {
        asyncExecutor.execute(() -> {
            try {
                if (result instanceof AjaxResult ajaxResult && ajaxResult.isSuccess()) {
                    // 1. 校验并获取订单ID（核心修复点）
                    Object data = ajaxResult.getData();
                    if (!(data instanceof Long orderMainId) || orderMainId == null) {
                        log.warn("骑手接单通知：订单ID为空或格式错误，返回数据：{}", data);
                        return;
                    }

                    // 2. 查询配送信息并校验非空
                    OrderDelivery orderDelivery = orderDeliveryMapper.selectOrderDeliveryByOrderMainId(orderMainId);
                    if (orderDelivery == null) {
                        log.warn("骑手接单通知：未找到订单{}的配送信息", orderMainId);
                        return;
                    }

                    // 3. 校验骑手ID非空
                    Long riderId = orderDelivery.getRiderId();
                    if (riderId == null) {
                        log.warn("骑手接单通知：订单{}的配送信息中骑手ID为空", orderMainId);
                        return;
                    }

                    // 4. 发送通知
                    orderNotifyService.sendPickOrderToUserNotify(riderId, orderMainId);
                    log.info("AOP异步发送骑手接单通知完成，订单ID：{}，骑手ID：{}", orderMainId, riderId);
                }
            } catch (Exception e) {
                log.error("骑手接单通知发送失败", e);
            }
        });
    }

    // ========== 新增：骑手取货通知逻辑 ==========
//    @AfterReturning(pointcut = "riderPickupOrderPointcut()", returning = "result")
//    public void afterRiderPickupOrder(Object result) {
//        asyncExecutor.execute(() -> {
//            try {
//                if (result instanceof AjaxResult ajaxResult && ajaxResult.isSuccess()) {
//                    // 提取订单ID
//                    Long orderMainId = null;
//                    Object data = ajaxResult.getData();
//                    if (data instanceof Long) {
//                        orderMainId = (Long) data;
//                    }
//
//                    if (orderMainId == null) {
//                        log.warn("骑手取货通知：订单ID为空，返回数据：{}", data);
//                        return;
//                    }
//
//                    // 查询配送信息
//                    OrderDelivery orderDelivery = orderDeliveryMapper.selectOrderDeliveryByOrderMainId(orderMainId);
//                    if (orderDelivery == null || orderDelivery.getRiderId() == null) {
//                        log.warn("骑手取货通知：配送信息异常，订单ID：{}", orderMainId);
//                        return;
//                    }
//
//                    // 发送骑手取货通知
//                    orderNotifyService.sendRiderGetOrderToUserNotify(orderDelivery.getRiderId(), orderMainId);
//                    log.info("AOP异步发送骑手取货通知完成，订单ID：{}，骑手ID：{}", orderMainId, orderDelivery.getRiderId());
//                }
//            } catch (Exception e) {
//                log.error("骑手取货通知发送失败", e);
//            }
//        });
//    }

    // ========== 新增：用户确认收货（订单完成）通知逻辑 ==========
//    @AfterReturning(pointcut = "userConfirmReceivePointcut()", returning = "result")
//    public void afterUserConfirmReceive(Object result) {
//        asyncExecutor.execute(() -> {
//            try {
//                if (result instanceof AjaxResult ajaxResult && ajaxResult.isSuccess()) {
//                    // 提取订单ID
//                    Long orderMainId = null;
//                    Object data = ajaxResult.getData();
//                    if (data instanceof Long) {
//                        orderMainId = (Long) data;
//                    }
//
//                    if (orderMainId == null) {
//                        log.warn("用户确认收货通知：订单ID为空，返回数据：{}", data);
//                        return;
//                    }
//
//                    // 发送订单完成通知
//                    orderNotifyService.sendOrderFinishNotify(orderMainId);
//                    log.info("AOP异步发送用户确认收货通知完成，订单ID：{}", orderMainId);
//                }
//            } catch (Exception e) {
//                log.error("用户确认收货通知发送失败", e);
//            }
//        });
//    }

    // 预留方法（保持原有结构）
    public void sendMerchantAcceptOrderToUserNotify(Object result) {}
    public void sendRiderAcceptOrderToUserNotify(Object result) {}
}