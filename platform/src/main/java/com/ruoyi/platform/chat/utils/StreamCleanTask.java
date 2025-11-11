package com.ruoyi.platform.chat.utils;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.platform.domain.ChatMessage;
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



    @Scheduled(cron = "* * 2 * * ?")
    public void cleanExpiredListMessage() {
        long fiveDaysAgo = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000;
        log.info("开始清理5天前的聊天消息，过期时间戳: {}", fiveDaysAgo);

        Set<String> listKeys = redisTemplate.keys("chat:sessionId:*:messages");
        if (listKeys == null || listKeys.isEmpty()) {
            log.info("无待清理的Redis List键，任务结束");
            return;
        }

        int batchSize = 100; // 批次大小，可根据实际情况调整
        for (String listKey : listKeys) {
            try {
                long start = 0;
                long end = batchSize - 1;
                List<ChatMessage> batch;
                List<Long> expiredIndexes = new ArrayList<>();

                // 分段读取并筛选过期消息索引
                do {
                    batch = redisTemplate.opsForList().range(listKey, start, end);
                    if (batch == null || batch.isEmpty()) {
                        break;
                    }

                    for (int i = 0; i < batch.size(); i++) {
                        ChatMessage msg = JSON.parseObject(JSON.toJSONString(batch.get(i)),ChatMessage.class);
                        if (msg.getCreateTime().getTime() < fiveDaysAgo) {
                            expiredIndexes.add(start + i);
                        }
                    }

                    start = end + 1;
                    end += batchSize;
                } while (batch.size() == batchSize);

                // 用LTRIM原子删除过期消息
                if (!expiredIndexes.isEmpty()) {
                    long lastExpiredIndex = expiredIndexes.get(expiredIndexes.size() - 1);
                    redisTemplate.opsForList().trim(listKey, lastExpiredIndex + 1, -1);
                    log.info("列表 {} 清理完成，删除过期消息 {} 条", listKey, expiredIndexes.size());
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