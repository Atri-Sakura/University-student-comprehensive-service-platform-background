package com.gzu.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gzu.common.result.Result;

@RestController
@RequestMapping("/auth")
public class Controller {
    @RequestMapping("/hello")
    public Result<Integer> hello(){
        return Result.success(1);
    }

}
