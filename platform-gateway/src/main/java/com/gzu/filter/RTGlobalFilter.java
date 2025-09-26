package com.gzu.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RTGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String uri = request.getURI().toString();
        long startTime = System.currentTimeMillis();
        log.info("请求【{}】开始，时间:{}", uri, startTime);
        //======================前置逻辑======================

        Mono<Void> filter = chain.filter(exchange).
                doFinally((result)->{
            long endTime = System.currentTimeMillis();
            log.info("请求【{}】 结束：:{} 耗时:{}", uri, endTime,endTime-startTime);
        }); //放行


        //=====================以下是后置逻辑=====================
        return filter;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
