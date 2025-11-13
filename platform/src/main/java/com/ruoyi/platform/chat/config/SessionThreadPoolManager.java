package com.ruoyi.platform.chat.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Component
public class SessionThreadPoolManager {
    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final Map<Integer, ExecutorService> sessionPools = new ConcurrentHashMap<>();
    private final List<ExecutorService> pools = new ArrayList<>(POOL_SIZE);

    public SessionThreadPoolManager() {
        for (int i = 0; i < POOL_SIZE; i++) {
            // 关键：将i赋值给final变量index，满足Lambda要求
            final int index = i;
            ExecutorService pool = Executors.newSingleThreadExecutor(runnable -> {
                Thread thread = new Thread(runnable, "session-thread-" + index);
//                thread.setDaemon(true);
                log.info("{}", thread.getName());
                return thread;
            });
            pools.add(pool);
            sessionPools.put(index, pool);
        }
    }

    public ExecutorService getExecutor(Long sessionId) {
        if (sessionId == null) {
            throw new IllegalArgumentException("sessionId不能为空");
        }
        int index = Math.abs(sessionId.hashCode() % POOL_SIZE);
        return sessionPools.get(index);
    }

    @PreDestroy
    public void shutdown() {
        for(ExecutorService pool : pools) {
            try{
                pool.shutdown();
                if(!pool.awaitTermination(30, TimeUnit.SECONDS)){
                    log.warn("---线程池未正确关闭---");
                }
            }catch(InterruptedException e){
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}