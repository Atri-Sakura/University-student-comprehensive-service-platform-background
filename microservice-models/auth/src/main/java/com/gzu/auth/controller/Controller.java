package com.gzu.auth.controller;

import com.gzu.auth.properties.JwtProperties;
import com.gzu.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping("/test")
public class Controller {
    @Autowired
    private JwtProperties jwtProperties;
    @RequestMapping("/hello")
    public Result<Integer> hello(){
        System.out.println(jwtProperties.getAdminTtl());
        System.out.println(jwtProperties.getAdminSecretKey());
        System.out.println(jwtProperties.getAdminTokenName());
        return Result.success(1);
    }

}
