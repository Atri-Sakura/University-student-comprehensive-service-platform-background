package com.ruoyi.platform.chat.utils;

import com.ruoyi.common.core.domain.entity.ChatMessage;
import com.ruoyi.platform.service.IChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MessageCacheSyncTask {
    @Autowired
    private IChatMessageService chatMessageService;
    @Autowired
    private ChatCacheUtils chatCacheUtils;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 每小时执行一次
    @Scheduled(cron = "0 0 * * * ?")
    public void syncCacheAndDb() {
        log.info("开始同步消息缓存与数据库状态");
        // 1. 扫描最近1天内有状态更新的消息
        List<ChatMessage> updatedMessages = chatMessageService.selectRecentlyUpdatedMessages(1);
        for (ChatMessage msg : updatedMessages) {
            String cacheKey = chatCacheUtils.buildCacheKey(msg.getSessionId());
            // 2. 检查缓存中该消息的状态是否与DB一致
            boolean isConsistent = checkCacheConsistency(cacheKey, msg);
            if (!isConsistent) {
                log.warn("消息状态不一致，消息ID: {}，DB状态: {}，缓存状态: ...", msg.getMessageId(), msg.getMsgStatus());
                // 3. 不一致则删除缓存，下次读取时从DB加载
                redisTemplate.delete(cacheKey);
            }
        }
        log.info("消息缓存与数据库状态同步完成");
    }

    // 校验单条消息的缓存与DB状态是否一致
    private boolean checkCacheConsistency(String cacheKey, ChatMessage dbMsg) {
        List<Object> cacheList = redisTemplate.opsForList().range(cacheKey, 0, -1);
        if (cacheList == null) {
            return true; // 缓存不存在，无需校验
        }
        for (Object obj : cacheList) {
            ChatMessage cacheMsg = (ChatMessage) obj;
            if (cacheMsg.getMessageId().equals(dbMsg.getMessageId())) {
                // 状态不一致
                return cacheMsg.getMsgStatus().equals(dbMsg.getMsgStatus());
            }
        }
        return true; // 缓存中不存在该消息，无需校验
    }
}