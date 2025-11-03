package com.ruoyi.platform.chat.ssl; // 确保包路径正确

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@Component // 核心：标记为Spring Bean，让Spring管理
@Slf4j
public class SslServerContextFactory {

    // 从配置文件读取证书参数（与application.yml中的配置对应）
//    @Value("${ssl.key-store}")
    private String keyStorePath = "classpath:server.p12";

//    @Value("${ssl.key-store-password}")
    private String keyStorePassword = "123456";

//    @Value("${ssl.key-store-type}")
    private String keyStoreType = "PKCS12";

    // 检查SSL证书是否可用（非静态方法，需实例调用）
    public boolean isSslAvailable() {
        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            // 读取classpath下的证书文件
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(keyStorePath.substring("classpath:".length()))) {
                if (is == null) {
                    log.warn("SSL证书文件不存在，路径：{}", keyStorePath);
                    return false;
                }
                // 用配置的密码加载证书
                keyStore.load(is, keyStorePassword.toCharArray());
            }
            return true;
        } catch (Exception e) {
            log.warn("SSL证书验证失败", e); // 打印详细异常，便于排查
            return false;
        }
    }

    // 创建SSL上下文（非静态方法，需实例调用）
    public SslContext createSslContext() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(keyStorePath.substring("classpath:".length()))) {
            if (is == null) {
                throw new RuntimeException("SSL证书文件不存在，路径：" + keyStorePath);
            }
            keyStore.load(is, keyStorePassword.toCharArray());
        }

        // 初始化密钥管理器
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyStorePassword.toCharArray());

        // 构建服务端SSL上下文
        return SslContextBuilder.forServer(kmf).build();
    }
}