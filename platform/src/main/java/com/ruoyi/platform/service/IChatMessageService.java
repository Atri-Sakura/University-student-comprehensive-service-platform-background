package com.ruoyi.platform.service;

import com.ruoyi.common.core.domain.entity.ChatMessage;

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


    public void updateChatMessageStatus(ChatMessage chatMessage,Long statusId);
}
