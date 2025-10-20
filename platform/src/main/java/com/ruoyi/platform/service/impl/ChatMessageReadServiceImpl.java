package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.ChatMessageReadMapper;
import com.ruoyi.platform.domain.ChatMessageRead;
import com.ruoyi.platform.service.IChatMessageReadService;

/**
 * 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class ChatMessageReadServiceImpl implements IChatMessageReadService 
{
    @Autowired
    private ChatMessageReadMapper chatMessageReadMapper;

    /**
     * 查询消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * 
     * @param readId 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）主键
     * @return 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     */
    @Override
    public ChatMessageRead selectChatMessageReadByReadId(Long readId)
    {
        return chatMessageReadMapper.selectChatMessageReadByReadId(readId);
    }

    /**
     * 查询消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）列表
     * 
     * @param chatMessageRead 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * @return 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     */
    @Override
    public List<ChatMessageRead> selectChatMessageReadList(ChatMessageRead chatMessageRead)
    {
        return chatMessageReadMapper.selectChatMessageReadList(chatMessageRead);
    }

    /**
     * 新增消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * 
     * @param chatMessageRead 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * @return 结果
     */
    @Override
    public int insertChatMessageRead(ChatMessageRead chatMessageRead)
    {
        chatMessageRead.setCreateTime(DateUtils.getNowDate());
        return chatMessageReadMapper.insertChatMessageRead(chatMessageRead);
    }

    /**
     * 修改消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * 
     * @param chatMessageRead 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * @return 结果
     */
    @Override
    public int updateChatMessageRead(ChatMessageRead chatMessageRead)
    {
        chatMessageRead.setUpdateTime(DateUtils.getNowDate());
        return chatMessageReadMapper.updateChatMessageRead(chatMessageRead);
    }

    /**
     * 批量删除消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * 
     * @param readIds 需要删除的消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）主键
     * @return 结果
     */
    @Override
    public int deleteChatMessageReadByReadIds(Long[] readIds)
    {
        return chatMessageReadMapper.deleteChatMessageReadByReadIds(readIds);
    }

    /**
     * 删除消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）信息
     * 
     * @param readId 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）主键
     * @return 结果
     */
    @Override
    public int deleteChatMessageReadByReadId(Long readId)
    {
        return chatMessageReadMapper.deleteChatMessageReadByReadId(readId);
    }
}
