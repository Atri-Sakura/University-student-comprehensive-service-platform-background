package com.ruoyi.platform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.platform.chat.protobuf.ChatMessageProto;
import com.ruoyi.platform.domain.ChatMessage;
import com.ruoyi.platform.mapper.ChatMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.ChatSessionMapper;
import com.ruoyi.platform.domain.ChatSession;
import com.ruoyi.platform.service.IChatSessionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 聊天会话（管理双方的聊天窗口关系）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
@Slf4j
public class ChatSessionServiceImpl implements IChatSessionService 
{
    @Autowired
    private ChatSessionMapper chatSessionMapper;
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    @Autowired
    private RedisCache redisCache;

    /**
     * 查询聊天会话（管理双方的聊天窗口关系）
     * 
     * @param sessionId 聊天会话（管理双方的聊天窗口关系）主键
     * @return 聊天会话（管理双方的聊天窗口关系）
     */
    @Override
    public ChatSession selectChatSessionBySessionId(Long sessionId)
    {
        return chatSessionMapper.selectChatSessionBySessionId(sessionId);
    }

    /**
     * 查询聊天会话（管理双方的聊天窗口关系）列表
     * 
     * @param chatSession 聊天会话（管理双方的聊天窗口关系）
     * @return 聊天会话（管理双方的聊天窗口关系）
     */
    @Override
    public List<ChatSession> selectChatSessionList(ChatSession chatSession)
    {
        return chatSessionMapper.selectChatSessionList(chatSession);
    }

    /**
     * 新增聊天会话（管理双方的聊天窗口关系）
     * 
     * @param chatSession 聊天会话（管理双方的聊天窗口关系）
     * @return 结果
     */
    @Override
    public int insertChatSession(ChatSession chatSession)
    {

        chatSession.setCreateTime(DateUtils.getNowDate());
        return chatSessionMapper.insertChatSession(chatSession);
    }

    /**
     * 修改聊天会话（管理双方的聊天窗口关系）
     * 
     * @param chatSession 聊天会话（管理双方的聊天窗口关系）
     * @return 结果
     */
    @Override
    public int updateChatSession(ChatSession chatSession)
    {
        chatSession.setUpdateTime(DateUtils.getNowDate());
        return chatSessionMapper.updateChatSession(chatSession);
    }

    /**
     * 批量删除聊天会话（管理双方的聊天窗口关系）
     * 
     * @param sessionIds 需要删除的聊天会话（管理双方的聊天窗口关系）主键
     * @return 结果
     */
    @Override
    public int deleteChatSessionBySessionIds(Long[] sessionIds)
    {
        return chatSessionMapper.deleteChatSessionBySessionIds(sessionIds);
    }

    /**
     * 删除聊天会话（管理双方的聊天窗口关系）信息
     * 
     * @param sessionId 聊天会话（管理双方的聊天窗口关系）主键
     * @return 结果
     */
    @Override
    public int deleteChatSessionBySessionId(Long sessionId)
    {
        return chatSessionMapper.deleteChatSessionBySessionId(sessionId);
    }

    @Override
    public Long selectChatSessionIdByFromTo(Long fromType, Long fromId, Long toType, Long toId)
    {
        return chatSessionMapper.selectChatSessionIdByFromTo(fromType, fromId, toType, toId);
    }

    public List<Long> selectChatSessionIdListByFromTo(Long fromType, Long fromId, Long toType, Long toId)
    {
        List<Long> sessionIds = new ArrayList<>();
        Long l1 = selectChatSessionIdByFromTo(fromType, fromId, toType, toId);
        Long l2 = selectChatSessionIdByFromTo(toType, toId, fromType, fromId);
        sessionIds.add(l1);
        sessionIds.add(l2);
        return sessionIds;
    }

    @Override
    public List<ChatSession> selectRecentChatSessions(Long fromType, Long fromId) {
        return chatSessionMapper.selectRecentChatSessions(fromType, fromId);
    }


    @Override
    public List<ChatSession> selectUnreadChatSessionList(Long fromType,Long fromId)
    {
        return chatSessionMapper.selectUnreadChatSessionList(fromType,fromId);
    }

    @Override
    public Integer increaseUnreadCount(Long sessionId)
    {
        if(sessionId.equals(null)){
            log.warn("会话ID为空，无法增加未读计数");
            return 0;
        }
        ChatSession chatSession = selectChatSessionBySessionId(sessionId);
        chatSession.setUnreadCount(chatSession.getUnreadCount()+1);
        return chatSessionMapper.updateChatSession(chatSession);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String readUnreadCount(Long sessionId) {
        // 1. 校验会话是否存在
        ChatSession chatSession = selectChatSessionBySessionId(sessionId);
        if (chatSession == null) {
            throw new RuntimeException("会话不存在，sessionId: " + sessionId);
        }

        // 2. 更新会话未读数量为0
        chatSession.setUnreadCount(0L);
        chatSessionMapper.updateChatSession(chatSession);

        // 3. 批量更新该会话下的所有未读消息（一次性SQL）
        Date now = DateUtils.getNowDate();
        int updatedCount = chatMessageMapper.batchUpdateUnreadToRead(sessionId, now);
        if( updatedCount > 0 ) {
            String key = "chat:sessionId:" + sessionId + ":messages";
            List<ChatMessage> chatMessages = redisCache.getCacheList(key);
            if( chatMessages != null && !chatMessages.isEmpty()) {
                int redisUpdateCount = 0;
                for (ChatMessage chatMessage : chatMessages) {
                    if(chatMessage.getMsgStatus() != 2){
                        chatMessage.setMsgStatus(2L);
                        chatMessage.setReadTime(now);
                        redisUpdateCount++;
                    }
                }
                redisCache.setCacheObject(key, chatMessages);
                log.info("redis[{}]条未读消息批量已读",redisUpdateCount);
            }
        }
        log.info("会话[{}]的未读消息已批量标记为已读，更新数量: {}", sessionId, updatedCount);


        return updatedCount == 0 ? "没有需要读取的消息哦":"已读" + updatedCount +"条消息";
    }

//    @Override
//    public List<ChatSession> getSessionList(Long fromType, Long fromId) {
//        return chatSessionMapper.getSessionList(fromType,fromId);
//    }


}
