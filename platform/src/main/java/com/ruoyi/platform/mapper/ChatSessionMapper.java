package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.ChatSession;

/**
 * 聊天会话（管理双方的聊天窗口关系）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface ChatSessionMapper 
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
     * @return 结果
     */
    public int insertChatSession(ChatSession chatSession);

    /**
     * 修改聊天会话（管理双方的聊天窗口关系）
     * 
     * @param chatSession 聊天会话（管理双方的聊天窗口关系）
     * @return 结果
     */
    public int updateChatSession(ChatSession chatSession);

    /**
     * 删除聊天会话（管理双方的聊天窗口关系）
     * 
     * @param sessionId 聊天会话（管理双方的聊天窗口关系）主键
     * @return 结果
     */
    public int deleteChatSessionBySessionId(Long sessionId);

    /**
     * 批量删除聊天会话（管理双方的聊天窗口关系）
     * 
     * @param sessionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatSessionBySessionIds(Long[] sessionIds);
}
