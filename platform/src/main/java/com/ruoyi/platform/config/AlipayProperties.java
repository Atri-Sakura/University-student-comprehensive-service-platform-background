package com.ruoyi.platform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayProperties {
    private String protocol;
    private String gatewayHost;
    private String signType;

    private String appId;
    private String merchantPrivateKey;
    private String alipayPublicKey;

    private String notifyUrl;
    private String returnUrl;

    private String payeeType;
    private String payeeAccount;
    private String payeeName;

}
