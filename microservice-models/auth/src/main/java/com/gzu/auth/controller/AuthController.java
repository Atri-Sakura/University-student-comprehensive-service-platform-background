package com.gzu.auth.controller;

import com.gzu.common.result.Result;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RefreshScope
public class AuthController {
    @GetMapping("/login")
    public Result login(){
        return Result.success("登录成功");
    }
}
