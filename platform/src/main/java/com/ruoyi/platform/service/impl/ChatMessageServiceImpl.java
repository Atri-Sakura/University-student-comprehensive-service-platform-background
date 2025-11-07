package com.ruoyi.platform.service.impl;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.config.FastJson2JsonRedisSerializer;
import com.ruoyi.platform.chat.utils.ChatCacheUtils;
import com.ruoyi.platform.domain.ChatMessage;
import com.ruoyi.platform.service.IChatSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.ChatMessageMapper;

import com.ruoyi.platform.service.IChatMessageService;

/**
 * 聊天消息（存储单条消息的核心信息）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
@Slf4j
public class ChatMessageServiceImpl implements IChatMessageService {
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private ChatCacheUtils chatCacheUtils;
    @Autowired
    private IChatSessionService chatSessionService;
    // 使用正确的RedisTemplate
    @Autowired
    private RedisTemplate<String, Object> stringKeyRedisTemplate;

    /**
     * 查询聊天消息（存储单条消息的核心信息）
     *
     * @param messageId 聊天消息（存储单条消息的核心信息）主键
     * @return 聊天消息（存储单条消息的核心信息）
     */
    @Override
    public ChatMessage selectChatMessageByMessageId(Long messageId) {
        return chatMessageMapper.selectChatMessageByMessageId(messageId);
    }

    /**
     * 查询聊天消息（存储单条消息的核心信息）列表
     *
     * @param chatMessage 聊天消息（存储单条消息的核心信息）
     * @return 聊天消息（存储单条消息的核心信息）
     */
    @Override
    public List<ChatMessage> selectChatMessageList(ChatMessage chatMessage) {
        return chatMessageMapper.selectChatMessageList(chatMessage);
    }

    /**
     * 新增聊天消息（存储单条消息的核心信息）
     *
     * @param chatMessage 聊天消息（存储单条消息的核心信息）
     * @return 结果
     */
    @Override
    public int insertChatMessage(ChatMessage chatMessage) {
        chatMessage.setCreateTime(DateUtils.getNowDate());
        return chatMessageMapper.insertChatMessage(chatMessage);
    }

    /**
     * 修改聊天消息（存储单条消息的核心信息）
     *
     * @param chatMessage 聊天消息（存储单条消息的核心信息）
     * @return 结果
     */
    @Override
    public int updateChatMessage(ChatMessage chatMessage) {
        chatMessage.setUpdateTime(DateUtils.getNowDate());
        return chatMessageMapper.updateChatMessage(chatMessage);
    }

    /**
     * 批量删除聊天消息（存储单条消息的核心信息）
     *
     * @param messageIds 需要删除的聊天消息（存储单条消息的核心信息）主键
     * @return 结果
     */
    @Override
    public int deleteChatMessageByMessageIds(Long[] messageIds) {
        return chatMessageMapper.deleteChatMessageByMessageIds(messageIds);
    }

    /**
     * 删除聊天消息（存储单条消息的核心信息）信息
     *
     * @param messageId 聊天消息（存储单条消息的核心信息）主键
     * @return 结果
     */
    @Override
    public int deleteChatMessageByMessageId(Long messageId) {
        return chatMessageMapper.deleteChatMessageByMessageId(messageId);
    }

    /**
     * 更新消息状态
     *
     * @param chatMessage
     * @param statusId
     */
    public void updateChatMessageStatus(ChatMessage chatMessage, Long statusId) {
        chatMessage.setMsgStatus(statusId);
        chatMessageMapper.updateChatMessage(chatMessage);
    }

    /**
     * 查询离线消息
     *
     * @param chatMessage
     * @return
     */
    public List<ChatMessage> selectOfflineChatMessageList(ChatMessage chatMessage) {
        chatMessage.setMsgStatus(3L);
        return chatMessageMapper.selectChatMessageList(chatMessage);
    }

    /**
     * 查询最近消息
     *
     * @param days
     * @return
     */
    public List<ChatMessage> selectRecentlyUpdatedMessages(Integer days) {
        long timeMillis = System.currentTimeMillis() - (long) days * 24 * 60 * 60 * 1000;
        Date startTime = new Date(timeMillis);
        return chatMessageMapper.selectRecentlyUpdatedMessages(startTime);
    }

    /**
     * 查询关联消息
     *
     * @param chatMessage
     * @return
     */
    @Override
    public List<ChatMessage> selectChatMessageWithAttachmentsJoin(ChatMessage chatMessage) {

        return chatMessageMapper.selectChatMessageWithAttachmentsJoin(chatMessage);
    }


    /**
     * 查询多个会话消息并拼接（直接查询数据库，不使用Redis缓存）
     *
     * @param sessionIds 会话ID列表
     * @return 合并并排序后的消息列表
     */
    public List<ChatMessage> selectMultiSessionMessages(List<Long> sessionIds) {
        List<ChatMessage> allMessages = new ArrayList<>();

        for(Long sessionId : sessionIds) {
            String key = chatCacheUtils.buildCacheKey(sessionId);
            List<Object> cacheLists = null;

            try {
                // 使用stringKeyRedisTemplate而不是redisTemplate
                cacheLists = stringKeyRedisTemplate.opsForList().range(key, 0, -1);
                log.info("查询到redis键{}对应的消息数量: {}", key, cacheLists != null ? cacheLists.size() : 0);
            } catch (Exception e) {
                log.error("redis获取失败, sessionId: {}, 错误: {}", sessionId, e.getMessage());
                continue;
            }

            if(cacheLists != null && !cacheLists.isEmpty()) {
                for(Object cache : cacheLists) {
                    try {
                        if(cache instanceof ChatMessage) {
                            allMessages.add((ChatMessage) cache);
                        } else if (cache instanceof String) {
                            // 如果是JSON字符串，手动反序列化
                            String jsonStr = (String) cache;
                            ChatMessage message = JSON.parseObject(jsonStr, ChatMessage.class);
                            allMessages.add(message);
                        } else {
                            // 使用配置的序列化器反序列化
                            FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(ChatMessage.class);
                            ChatMessage message = (ChatMessage) serializer.deserialize(cache.toString().getBytes());
                            allMessages.add(message);
                        }
                    } catch (Exception e) {
                        log.warn("反序列化消息失败, 原始数据: {}", cache);
                    }
                }
            }
        }

        if(allMessages.isEmpty()) {
            // 从数据库查询
            for (Long sessionId : sessionIds) {
                ChatMessage query = new ChatMessage();
                query.setSessionId(sessionId);
                List<ChatMessage> dbMsgs = chatMessageMapper.selectChatMessageWithAttachmentsJoin(query);
                allMessages.addAll(dbMsgs);
            }
        }

        return allMessages.stream()
                .sorted(Comparator.comparing(ChatMessage::getSendTime))
                .collect(Collectors.toList());
    }

    public List<ChatMessage> selectMultiSessionMessages(Long fromType, Long fromId, Long toType, Long toId) {
        List<Long> sessionIds = chatSessionService.selectChatSessionIdListByFromTo(fromType, fromId, toType, toId);

        return selectMultiSessionMessages(sessionIds);
    }


}



