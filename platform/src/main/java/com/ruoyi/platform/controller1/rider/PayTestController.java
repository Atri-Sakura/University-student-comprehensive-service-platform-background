package com.ruoyi.platform.controller1.rider;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay/test")
public class PayTestController {

    @GetMapping("/alipayPage")
    public String testAlipayPage() throws Exception {

        String subject = "test sandbox";
        String outTradeNo = "test" + System.currentTimeMillis();
        String totalAmount = "100.00";


        // returnUrl 用配置的，或者你写死也行
        String returnUrl = "http://localhost:8080";
        AlipayTradePagePayResponse response = Factory.Payment.Page()
                .pay(subject, outTradeNo, totalAmount,returnUrl);
        return response.getBody();
    }
}
