package com.ruoyi.platform.chat.utils;

import com.ruoyi.common.core.domain.entity.ChatMessage;
import com.ruoyi.common.core.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class StreamCleanTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisCache redisCache;

    // 每天凌晨2点执行清理
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanExpiredListMessage() {
        // 计算5天前的时间戳（毫秒）
        long fiveDaysAgo = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000;
        log.info("开始清理5天前的聊天消息，过期时间戳: {}", fiveDaysAgo);

        // 1. 获取所有聊天消息的List键（匹配chat:sessionId:*:messages）
        Set<String> listKeys = redisTemplate.keys("chat:sessionId:*:messages");
        if (listKeys == null || listKeys.isEmpty()) {
            log.info("无待清理的Redis List键，任务结束");
            return;
        }

        // 2. 遍历每个List键，清理过期消息
        for (String listKey : listKeys) {
            try {
                // 获取列表中所有消息
                List<ChatMessage> messages = redisCache.getCacheList(listKey);
                if (messages == null || messages.isEmpty()) {
                    log.debug("列表 {} 中无消息，跳过清理", listKey);
                    continue;
                }

                // 筛选出需要保留的消息（未过期）
                List<ChatMessage> remainingMessages = new ArrayList<>();
                int expiredCount = 0;

                for (ChatMessage message : messages) {
                    // 假设消息的时间戳字段为createTime（毫秒）
                    if (message.getCreateTime().getTime() >= fiveDaysAgo) {
                        remainingMessages.add(message);
                    } else {
                        expiredCount++;
                    }
                }

                // 如果有过期消息，更新列表（先清空再添加保留的消息）
                if (expiredCount > 0) {
                    // 删除原有列表
                    redisCache.deleteObject(listKey);
                    // 添加保留的消息
                    if (!remainingMessages.isEmpty()) {
                        redisCache.setCacheList(listKey, remainingMessages);
                    }
                    log.info("列表 {} 清理完成，删除过期消息 {} 条，保留消息 {} 条",
                            listKey, expiredCount, remainingMessages.size());
                } else {
                    log.debug("列表 {} 中无过期消息，无需清理", listKey);
                }
            } catch (Exception e) {
                log.error("清理列表 {} 时发生异常", listKey, e);
            }
        }

        log.info("聊天消息清理任务执行完毕");
    }
}