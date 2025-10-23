package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.ChatMessageRead;

/**
 * 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IChatMessageReadService 
{
    /**
     * 查询消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * 
     * @param readId 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）主键
     * @return 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     */
    public ChatMessageRead selectChatMessageReadByReadId(Long readId);

    /**
     * 查询消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）列表
     * 
     * @param chatMessageRead 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * @return 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）集合
     */
    public List<ChatMessageRead> selectChatMessageReadList(ChatMessageRead chatMessageRead);

    /**
     * 新增消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * 
     * @param chatMessageRead 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * @return 结果
     */
    public int insertChatMessageRead(ChatMessageRead chatMessageRead);

    /**
     * 修改消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * 
     * @param chatMessageRead 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * @return 结果
     */
    public int updateChatMessageRead(ChatMessageRead chatMessageRead);

    /**
     * 批量删除消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     * 
     * @param readIds 需要删除的消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）主键集合
     * @return 结果
     */
    public int deleteChatMessageReadByReadIds(Long[] readIds);

    /**
     * 删除消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）信息
     * 
     * @param readId 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）主键
     * @return 结果
     */
    public int deleteChatMessageReadByReadId(Long readId);
}
