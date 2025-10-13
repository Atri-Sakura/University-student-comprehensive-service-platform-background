package com.gzu.auth.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Data
public class JwtProperties {

    /**
     * 平台端生成jwt令牌相关配置  已经添加nacos云更新配置
     */
    @Value("${JwtProperties.adminSecretKey}")
    private String adminSecretKey;
    @Value("${JwtProperties.adminTtl}")
    private long adminTtl;
    @Value("${JwtProperties.adminTokenName}")
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey="student";
    private long userTtl = 7200000;
    private String userTokenName = "token";

}
