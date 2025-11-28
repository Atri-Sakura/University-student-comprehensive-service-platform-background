package com.ruoyi.platform.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import org.springframework.stereotype.Component;

@Component
public class AlipayEasySdkInitializer {
    public AlipayEasySdkInitializer(AlipayProperties props){
        System.out.println(">>> appId = " + props.getAppId());
        System.out.println(">>> merchantPrivateKey length = " +
                (props.getMerchantPrivateKey() == null ? "null" : props.getMerchantPrivateKey().length()));
        Config config = new Config();

        config.protocol = props.getProtocol();
        config.gatewayHost = props.getGatewayHost();
        config.signType =props.getSignType();//default "RSA2“

        config.appId = props.getAppId();
        config.merchantPrivateKey = props.getMerchantPrivateKey();
        config.alipayPublicKey = props.getAlipayPublicKey();

        config.notifyUrl = props.getNotifyUrl();

        Factory.setOptions(config);

    }
}
