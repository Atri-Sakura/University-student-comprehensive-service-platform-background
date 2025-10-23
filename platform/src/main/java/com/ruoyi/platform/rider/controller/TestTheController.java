package com.ruoyi.platform.rider.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestTheController {

    @RequestMapping("/print")
    public AjaxResult print(){
        System.out.println("hello world");
        return AjaxResult.success("FakeAuthFilter 测试成功","fucking word");
    }
}
