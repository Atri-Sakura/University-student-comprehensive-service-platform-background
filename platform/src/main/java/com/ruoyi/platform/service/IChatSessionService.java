package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.ChatSession;

/**
 * 聊天会话（管理双方的聊天窗口关系）Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IChatSessionService 
{
    /**
     * 查询聊天会话（管理双方的聊天窗口关系）
     * 
     * @param sessionId 聊天会话（管理双方的聊天窗口关系）主键
     * @return 聊天会话（管理双方的聊天窗口关系）
     */
    public ChatSession selectChatSessionBySessionId(Long sessionId);

    /**
     * 查询聊天会话（管理双方的聊天窗口关系）列表
     * 
     * @param chatSession 聊天会话（管理双方的聊天窗口关系）
     * @return 聊天会话（管理双方的聊天窗口关系）集合
     */
    public List<ChatSession> selectChatSessionList(ChatSession chatSession);

    /**
     * 新增聊天会话（管理双方的聊天窗口关系）
     *
     * @param chatSession 聊天会话（管理双方的聊天窗口关系）
     * @return 生成的sessionId
     */
    ChatSession insertChatSession(ChatSession chatSession);


    /**
     * 修改聊天会话（管理双方的聊天窗口关系）
     * 
     * @param chatSession 聊天会话（管理双方的聊天窗口关系）
     * @return 结果
     */
    public int updateChatSession(ChatSession chatSession);

    /**
     * 批量删除聊天会话（管理双方的聊天窗口关系）
     * 
     * @param sessionIds 需要删除的聊天会话（管理双方的聊天窗口关系）主键集合
     * @return 结果
     */
    public int deleteChatSessionBySessionIds(Long[] sessionIds);

    /**
     * 删除聊天会话（管理双方的聊天窗口关系）信息
     * 
     * @param sessionId 聊天会话（管理双方的聊天窗口关系）主键
     * @return 结果
     */
    public int deleteChatSessionBySessionId(Long sessionId);


    /**
     * 查询会话Id
     * @param fromType
     * @param toType
     * @param fromId
     * @param toId
     * @return
     */
    public Long selectChatSessionIdByFromTo(Long fromType, Long fromId, Long toType, Long toId);

    /**
     * 获取未读会话
     * @param fromType
     * @param fromId
     * @return
     */
    public List<ChatSession> selectUnreadChatSessionList(Long fromType,Long fromId);

    /**
     * 增加未读数量
     * @param sessionId
     * @return
     */
    public Integer increaseUnreadCount(Long sessionId);

    /**
     * 已读所有消息
     * @param sessionId
     * @return
     */
    public String readUnreadCount(Long sessionId);

    /**
     * 查询sessionId的
     * @param fromType
     * @param fromId
     * @param toType
     * @param toId
     * @return
     */
    public List<Long> selectChatSessionIdListByFromTo(Long fromType, Long fromId, Long toType, Long toId);

    public List<ChatSession> selectRecentChatSessions (Long fromType, Long fromId);
}
