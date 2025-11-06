package com.ruoyi.platform.chat.utils;


import com.ruoyi.platform.domain.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ChatCacheUtils {
    // 缓存常量统一管理
    public static final String CACHE_KEY_PREFIX = "chat:sessionId:";
    public static final String CACHE_KEY_SUFFIX = ":messages";
    // 新增：消息ID与会话ID的映射Key（Hash结构，key=msgId, value=sessionId）
    public static final String MSG_ID_TO_SESSION_ID = "chat:msgId:sessionId:map";

    public static final int MAX_CACHE_MESSAGE_COUNT = 100;
    public static final long CACHE_EXPIRE_DAYS = 7;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void cacheChatMessage(ChatMessage message) {
        if (message.getSessionId() == null || message.getMessageId() == null) {
            log.warn("消息会话ID或消息ID为空，无法缓存，消息ID: {}", message.getMessageId());
            return;
        }
        String cacheKey = buildCacheKey(message.getSessionId());
        try {
            // 1. 缓存消息到会话列表
            redisTemplate.opsForList().rightPush(cacheKey, message);
            redisTemplate.opsForList().trim(cacheKey, -MAX_CACHE_MESSAGE_COUNT, -1);
            redisTemplate.expire(cacheKey, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);

            // 2. 记录消息ID→会话ID的映射（field 用 String 类型）
            String messageIdStr = String.valueOf(message.getMessageId()); // 转为 String
            redisTemplate.opsForHash().put(MSG_ID_TO_SESSION_ID, messageIdStr, message.getSessionId());
            redisTemplate.expire(MSG_ID_TO_SESSION_ID, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);

        } catch (Exception e) {
            log.error("缓存消息失败，消息ID: {}", message.getMessageId(), e);
            throw new RuntimeException("消息缓存失败", e);
        }
    }

    public void deleteMessageFromCache(Long messageId) {
        try {
            String messageIdStr = String.valueOf(messageId);

            // 1. 从 Hash 中获取 sessionId（注意：存入的是 Long 类型，此处应转为 Long）
            // 错误：String sessionId = (String) ...
            // 正确：直接获取 Long 类型（Redis 会自动反序列化）
            Long sessionId = (Long) redisTemplate.opsForHash().get(MSG_ID_TO_SESSION_ID, messageIdStr);
            if (sessionId == null) {
                log.warn("消息{}不在缓存中，无需删除", messageId);
                return;
            }

            // 2. 从会话缓存中删除该消息（sessionId 已为 Long 类型，直接使用）
            String cacheKey = buildCacheKey(sessionId); // 无需再转换，直接传入 Long
            List<Object> messages = redisTemplate.opsForList().range(cacheKey, 0, -1);
            if (messages == null || messages.isEmpty()) {
                log.warn("会话{}的缓存为空，无需删除消息{}", sessionId, messageId);
                return;
            }

            // 3. 过滤被撤回的消息（逻辑不变）
            List<Object> remainingMessages = messages.stream()
                    .filter(msg -> {
                        ChatMessage chatMsg = (ChatMessage) msg;
                        return !chatMsg.getMessageId().equals(messageId);
                    })
                    .toList();

            // 4. 覆盖原缓存（逻辑不变）
            redisTemplate.delete(cacheKey);
            if (!remainingMessages.isEmpty()) {
                redisTemplate.opsForList().rightPushAll(cacheKey, remainingMessages);
                redisTemplate.expire(cacheKey, CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
            }

            // 5. 从映射表中删除记录（逻辑不变）
            redisTemplate.opsForHash().delete(MSG_ID_TO_SESSION_ID, messageIdStr);
            log.info("已从缓存中删除消息{}", messageId);

        } catch (Exception e) {
            log.error("删除缓存消息{}失败", messageId, e);
            // 降级逻辑：同样修正 sessionId 类型
            String messageIdStr = String.valueOf(messageId);
            Long sessionId = (Long) redisTemplate.opsForHash().get(MSG_ID_TO_SESSION_ID, messageIdStr);
            if (sessionId != null) {
                redisTemplate.delete(buildCacheKey(sessionId));
                log.warn("删除单条消息失败，已降级删除会话{}的缓存", sessionId);
            }
        }
    }

    /**
     * 构建缓存Key（原逻辑不变）
     */
    public String buildCacheKey(Long sessionId) {
        return CACHE_KEY_PREFIX + sessionId + CACHE_KEY_SUFFIX;
    }

    // 其他已有方法...
}