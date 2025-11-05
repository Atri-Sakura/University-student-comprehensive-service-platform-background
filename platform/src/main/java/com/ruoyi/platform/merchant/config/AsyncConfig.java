package com.ruoyi.platform.merchant.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class AsyncConfig {
    /**
     * 异步线程池配置，保证异步线程较可靠但依旧可能丢失
     *  */
    @Bean("withdrawExecutor")
    public Executor withdrawExecutor(){
        return new ThreadPoolExecutor(
                5,
                10, // 最大线程数
                60L, // 空闲线程存活时间
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100), // 队列容量
                new ThreadFactoryBuilder().setNameFormat("withdraw-pool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy() // 队列满时拒绝策略
        );
    }
}
