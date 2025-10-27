package com.ruoyi.platform.auth.aop;

import com.ruoyi.common.utils.netty.NettyClientUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.auth.controller.AuthController;
import com.ruoyi.platform.service.IUserBaseService;
import com.ruoyi.platform.service.impl.UserBaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 登录后自动连接Netty服务器的切面
 */
@Aspect
@Component
@RequiredArgsConstructor
public class NettyAspect {

    @Autowired
    private final NettyClientUtil nettyClientUtil;

    @Autowired
    private IUserBaseService userBaseService;

    /**
     * 定义切点：拦截AuthController中所有登录方法（用户、骑手、商家登录）
     */
    @Pointcut("execution(* com.ruoyi.platform.auth.controller.AuthController.login*(..))")
    public void loginPointcut() {}

    /**
     * 登录成功后，触发Netty连接
     */
    @AfterReturning(pointcut = "loginPointcut()", returning = "result")
    public void afterLoginSuccess(JoinPoint joinPoint, Object result) {
        // 仅处理登录成功的响应
        if (result instanceof AjaxResult ajaxResult && ajaxResult.get("token") != null) {
            // 获取方法名，判断登录类型（用户、骑手、商家）
            String methodName = joinPoint.getSignature().getName();
            Long userType = switch (methodName) {
                case "loginUser" -> 1L;   // 用户类型：1-用户
                case "loginRider" -> 2L;  // 用户类型：2-骑手
                case "loginMerchant" -> 3L; // 用户类型：3-商家
                default -> 0L;
            };


            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof com.ruoyi.common.core.domain.model.LoginBody loginBody) {
                Long userBaseId = userBaseService.selectUserBaseIdByPhone(loginBody.getPhonenumber());
                if (userType > 0 && userBaseId != null) {
                    nettyClientUtil.connectAndRegister(userType, userBaseId);
                }
            }
        }
    }
}