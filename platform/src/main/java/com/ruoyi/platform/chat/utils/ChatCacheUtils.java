package com.ruoyi.platform.chat.utils;

import com.ruoyi.common.core.domain.entity.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ChatCacheUtils {
    // 缓存常量统一管理
    public static final String CACHE_KEY_PREFIX = "chat:sessionId:";
    public static final String CACHE_KEY_SUFFIX = ":messages";
    public static final int MAX_CACHE_MESSAGE_COUNT = 100;
    public static final long CACHE_EXPIRE_DAYS = 7;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 缓存聊天消息
     */
    public void cacheChatMessage(ChatMessage message) {
        if (message.getSessionId() == null) {
            log.warn("消息会话ID为空，无法缓存，消息ID: {}", message.getMessageId());
            return;
        }
        String cacheKey = buildCacheKey(message.getSessionId());
        try {
            redisTemplate.opsForList().rightPush(cacheKey, message);
            // 正确trim：保留最后MAX_CACHE_MESSAGE_COUNT条
            redisTemplate.opsForList().trim(cacheKey, -MAX_CACHE_MESSAGE_COUNT, -1);
            redisTemplate.expire(cacheKey, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error("缓存消息失败，消息ID: {}", message.getMessageId(), e);
            throw new RuntimeException("消息缓存失败", e); // 可选：抛异常让上层处理，避免静默失败
        }
    }

    /**
     * 构建缓存Key
     */
    public String buildCacheKey(Long sessionId) {
        return CACHE_KEY_PREFIX + sessionId + CACHE_KEY_SUFFIX;
    }

    // 可新增其他缓存方法：如获取会话缓存消息、删除过期缓存等
}