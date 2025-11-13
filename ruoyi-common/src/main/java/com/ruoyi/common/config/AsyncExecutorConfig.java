package com.ruoyi.common.config;

import jakarta.annotation.PreDestroy;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;


//@Configuration
public class AsyncExecutorConfig {

//    @Bean(name = "riderUploadExecutor")
    public ThreadPoolExecutor riderUploadExecutor(){
        ThreadFactory threadFactory = new ThreadFactory() {
            int count = 1;
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "rider-upload-thread-" + count++);
                t.setDaemon(false);
                return t;
            }
        };
        return  new ThreadPoolExecutor(
                4,          // 核心线程数
                8,                      // 最大线程数
                60L, TimeUnit.SECONDS,  // 空闲线程存活时间
                new LinkedBlockingQueue<>(100),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：主线程执行
        );
    }


}
