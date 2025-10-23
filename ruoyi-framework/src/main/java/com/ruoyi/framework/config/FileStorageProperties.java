package com.ruoyi.framework.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String storage = "local";

    private Cloud cloud = new Cloud();

    @Data
    public static class Cloud {
        private boolean enabled;
        private String type;
        private String endpoint;
        private String bucket;
        private String accessKey;
        private String secretKey;
    }
}
