package com.ruoyi.platform.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.platform.auth.service.IAuthService;

import jakarta.validation.Valid;

/**
 * 三端认证控制器（用户、骑手、商家）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/platform/auth")
public class AuthController extends BaseController
{
    @Autowired
    private IAuthService authService;

    // ==================== 用户端 ====================

    /**
     * 用户端注册
     */
    @PostMapping("/user/register")
    public AjaxResult registerUser(@Valid @RequestBody RegisterBody registerBody)
    {
        registerBody.setUserType("1"); // 用户类型：1-用户
        String msg = authService.registerUser(registerBody);
        return success(msg);
    }

    /**
     * 用户端登录
     */
    @PostMapping("/user/login")
    public AjaxResult loginUser(@RequestBody LoginBody loginBody)
    {
        // 使用手机号登录
        String token = authService.loginUser(
                loginBody.getPhonenumber(),
                loginBody.getPassword(),
                loginBody.getCode(),
                loginBody.getUuid()
        );
        AjaxResult ajax = AjaxResult.success();
        ajax.put("token", token);
        return ajax;
    }

    // ==================== 骑手端 ====================

    /**
     * 骑手端注册
     */
    @PostMapping("/rider/register")
    public AjaxResult registerRider(@Valid @RequestBody RegisterBody registerBody)
    {
        registerBody.setUserType("2"); // 用户类型：2-骑手
        String msg = authService.registerRider(registerBody);
        return success(msg);
    }

    /**
     * 骑手端登录
     */
    @PostMapping("/rider/login")
    public AjaxResult loginRider(@RequestBody LoginBody loginBody)
    {
        // 使用手机号登录
        String token = authService.loginRider(
                loginBody.getPhonenumber(),
                loginBody.getPassword(),
                loginBody.getCode(),
                loginBody.getUuid()
        );
        AjaxResult ajax = AjaxResult.success();
        ajax.put("token", token);
        return ajax;
    }

    // ==================== 商家端 ====================

    /**
     * 商家端注册
     */
    @PostMapping("/merchant/register")
    public AjaxResult registerMerchant(@Valid @RequestBody RegisterBody registerBody)
    {
        registerBody.setUserType("3"); // 用户类型：3-商家
        String msg = authService.registerMerchant(registerBody);
        return success(msg);
    }

    /**
     * 商家端登录
     */
    @PostMapping("/merchant/login")
    public AjaxResult loginMerchant(@RequestBody LoginBody loginBody)
    {
        // 使用手机号登录
        String token = authService.loginMerchant(
                loginBody.getPhonenumber(),
                loginBody.getPassword(),
                loginBody.getCode(),
                loginBody.getUuid()
        );
        AjaxResult ajax = AjaxResult.success();
        ajax.put("token", token);
        return ajax;
    }
}