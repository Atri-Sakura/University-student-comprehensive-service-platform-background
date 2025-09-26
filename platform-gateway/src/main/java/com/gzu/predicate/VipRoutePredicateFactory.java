package com.gzu.predicate;


import com.alibaba.nacos.common.utils.StringUtils;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class VipRoutePredicateFactory extends AbstractRoutePredicateFactory<VipRoutePredicateFactory.Config> {

    public VipRoutePredicateFactory() {
        super(Config.class);
    }


    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return exchange -> {
            ServerHttpRequest request = exchange.getRequest();
            // 1. 获取请求中 "param" 对应的参数值（如 user=leifengyang → first = "leifengyang"）
            String paramValue = request.getQueryParams().getFirst(config.param);
            // 2. 校验：参数值存在，且等于配置的 "value"
            return StringUtils.hasText(paramValue) && paramValue.equals(config.value);
        };
    }

    @Validated
    public static class Config {
        private @NotEmpty String param; // 参数名（如 user）
        private String value;           // 参数预期值（如 leifengyang）

        public @NotEmpty String getParam() {
            return param;
        }

        public String getValue() {
            return value;
        }

        public void setParam(@NotEmpty String param) {
            this.param = param;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}