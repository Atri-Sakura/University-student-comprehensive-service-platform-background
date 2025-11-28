package com.ruoyi.platform.controller1.rider;

import com.alipay.easysdk.factory.Factory;
import com.ruoyi.platform.service.IPayNotifyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay/alipay")
public class AlipayNotifyController {
    @Autowired
    private IPayNotifyService payNotifyService;

    @PostMapping("/notify")
    public String notify(HttpServletRequest request) throws Exception {
        System.out.println(">>> 收到测试 notify");
//         处理支付宝异步通知逻辑
        Map<String,String[]> requestParams = request.getParameterMap();
        Map<String,String> params = new HashMap<>();
        for(String name : requestParams.keySet()) {
            String value = requestParams.get(name)[0];
            params.put(name, value);
        }
//        // 验证签名、更新订单状态等
//        boolean signVerified = Factory.Payment.Common().verifyNotify(params);
//        if (!signVerified) {
//            System.out.println("【支付宝沙箱回调】验签失败：" + params);
//            return "fail";
//        }
        // 返回给支付宝的响应
        try {
            // 2. 交给 service 统一处理（包括：验签、查 pay_order、分流、记钱包流水等）
            String result = payNotifyService.handleAlipayNotify(params);
            // result 只允许是 "success" 或 "fail"
            return result;
        } catch (Exception e) {
            // 打一下日志就行，别把异常往外抛
            e.printStackTrace();
            return "fail";
        }

    }
}
