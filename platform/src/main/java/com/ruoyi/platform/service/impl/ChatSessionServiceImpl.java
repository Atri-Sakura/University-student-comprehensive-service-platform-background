package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.ChatSessionMapper;
import com.ruoyi.platform.domain.ChatSession;
import com.ruoyi.platform.service.IChatSessionService;

/**
 * 聊天会话（管理双方的聊天窗口关系）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class ChatSessionServiceImpl implements IChatSessionService 
{
    @Autowired
    private ChatSessionMapper chatSessionMapper;

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
}
