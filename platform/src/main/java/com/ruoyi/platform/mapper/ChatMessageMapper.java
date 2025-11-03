package com.ruoyi.platform.mapper;

import com.ruoyi.common.core.domain.entity.ChatMessage;

import java.util.Date;
import java.util.List;


/**
 * 聊天消息（存储单条消息的核心信息）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface ChatMessageMapper 
{
    /**
     * 查询聊天消息（存储单条消息的核心信息）
     * 
     * @param messageId 聊天消息（存储单条消息的核心信息）主键
     * @return 聊天消息（存储单条消息的核心信息）
     */
    public ChatMessage selectChatMessageByMessageId(Long messageId);

    /**
     * 查询聊天消息（存储单条消息的核心信息）列表
     * 
     * @param chatMessage 聊天消息（存储单条消息的核心信息）
     * @return 聊天消息（存储单条消息的核心信息）集合
     */
    public List<ChatMessage> selectChatMessageList(ChatMessage chatMessage);

    /**
     * 新增聊天消息（存储单条消息的核心信息）
     * 
     * @param chatMessage 聊天消息（存储单条消息的核心信息）
     * @return 结果
     */
    public int insertChatMessage(ChatMessage chatMessage);

    /**
     * 修改聊天消息（存储单条消息的核心信息）
     * 
     * @param chatMessage 聊天消息（存储单条消息的核心信息）
     * @return 结果
     */
    public int updateChatMessage(ChatMessage chatMessage);

    /**
     * 删除聊天消息（存储单条消息的核心信息）
     * 
     * @param messageId 聊天消息（存储单条消息的核心信息）主键
     * @return 结果
     */
    public int deleteChatMessageByMessageId(Long messageId);

    /**
     * 批量删除聊天消息（存储单条消息的核心信息）
     * 
     * @param messageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatMessageByMessageIds(Long[] messageIds);

    /**
     * 批量阅读未读消息
     * @param sessionId
     * @param readTime
     * @return
     */
    int batchUpdateUnreadToRead( Long sessionId,  Date readTime);

    /**
     * 查询最近更新的消息
     * @param days
     * @return
     */
    List<ChatMessage> selectRecentlyUpdatedMessages(Date date);
}
