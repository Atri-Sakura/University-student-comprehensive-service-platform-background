package com.gzu.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "test2")
@Data
public class OrderProperties {

    String Timeout;

    String autoConfirm;
}
