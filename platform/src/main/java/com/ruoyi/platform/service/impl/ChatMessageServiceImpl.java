package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.ChatMessageMapper;
import com.ruoyi.platform.domain.ChatMessage;
import com.ruoyi.platform.service.IChatMessageService;

/**
 * 聊天消息（存储单条消息的核心信息）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class ChatMessageServiceImpl implements IChatMessageService 
{
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    /**
     * 查询聊天消息（存储单条消息的核心信息）
     * 
     * @param messageId 聊天消息（存储单条消息的核心信息）主键
     * @return 聊天消息（存储单条消息的核心信息）
     */
    @Override
    public ChatMessage selectChatMessageByMessageId(Long messageId)
    {
        return chatMessageMapper.selectChatMessageByMessageId(messageId);
    }

    /**
     * 查询聊天消息（存储单条消息的核心信息）列表
     * 
     * @param chatMessage 聊天消息（存储单条消息的核心信息）
     * @return 聊天消息（存储单条消息的核心信息）
     */
    @Override
    public List<ChatMessage> selectChatMessageList(ChatMessage chatMessage)
    {
        return chatMessageMapper.selectChatMessageList(chatMessage);
    }

    /**
     * 新增聊天消息（存储单条消息的核心信息）
     * 
     * @param chatMessage 聊天消息（存储单条消息的核心信息）
     * @return 结果
     */
    @Override
    public int insertChatMessage(ChatMessage chatMessage)
    {
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
    public int updateChatMessage(ChatMessage chatMessage)
    {
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
    public int deleteChatMessageByMessageIds(Long[] messageIds)
    {
        return chatMessageMapper.deleteChatMessageByMessageIds(messageIds);
    }

    /**
     * 删除聊天消息（存储单条消息的核心信息）信息
     * 
     * @param messageId 聊天消息（存储单条消息的核心信息）主键
     * @return 结果
     */
    @Override
    public int deleteChatMessageByMessageId(Long messageId)
    {
        return chatMessageMapper.deleteChatMessageByMessageId(messageId);
    }

    public void updateChatMessageStatus(ChatMessage chatMessage,Long statusId){
        chatMessage.setMsgStatus(statusId);
        chatMessageMapper.updateChatMessage(chatMessage);
    }


}
