package com.ruoyi.platform.service;


import com.ruoyi.platform.domain.ChatMessage;

import java.util.List;


/**
 * 聊天消息（存储单条消息的核心信息）Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IChatMessageService 
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
     * 批量删除聊天消息（存储单条消息的核心信息）
     * 
     * @param messageIds 需要删除的聊天消息（存储单条消息的核心信息）主键集合
     * @return 结果
     */
    public int deleteChatMessageByMessageIds(Long[] messageIds);

    /**
     * 删除聊天消息（存储单条消息的核心信息）信息
     * 
     * @param messageId 聊天消息（存储单条消息的核心信息）主键
     * @return 结果
     */
    public int deleteChatMessageByMessageId(Long messageId);


    /**
     * 修改状态
     * @param chatMessage
     * @param statusId
     */
    public void updateChatMessageStatus(ChatMessage chatMessage,Long statusId);

    /**
     * 查询离线消息
     * @param chatMessage
     * @return
     */
    public List<ChatMessage> selectOfflineChatMessageList(ChatMessage chatMessage);

    /**
     * 查询最近更新的消息
     * @return
     */
    public List<ChatMessage> selectRecentlyUpdatedMessages(Integer days);

    /**
     * 查询关联消息
     * @param chatMessage
     * @return
     */
    public List<ChatMessage> selectChatMessageWithAttachmentsJoin(ChatMessage chatMessage);

    /**
     * 查询两个会话消息
     * @param sessionIds
     * @return
     */
    public List<ChatMessage> selectMultiSessionMessages(List<Long> sessionIds);

    /**
     * 查询两个消息会话通过角色
     * @param fromType
     * @param fromId
     * @param toType
     * @param toId
     * @return
     */
    public List<ChatMessage> selectMultiSessionMessages(Long fromType, Long fromId, Long toType, Long toId);
    }
