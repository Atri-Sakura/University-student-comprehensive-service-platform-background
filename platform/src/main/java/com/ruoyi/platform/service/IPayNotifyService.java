package com.ruoyi.platform.service;

import java.util.Map;

public interface IPayNotifyService {

    /**
     * 支付宝异步通知统一处理入口
     */
    String handleAlipayNotify(Map<String, String> params) throws Exception;
}
