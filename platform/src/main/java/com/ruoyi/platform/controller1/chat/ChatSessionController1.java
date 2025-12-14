package com.ruoyi.platform.controller1.chat;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.ChatSession;
import com.ruoyi.platform.service.IChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.AbstractDocument;
import java.util.List;

/**
 * 聊天会话Controller
 *
 * @author ruoyi
 * @date 2025-10-22
 */
@RestController
@RequestMapping("/platform/chat/session")
public class ChatSessionController1 {

    @Autowired
    private IChatSessionService chatSessionService;

    /**
     * 查询聊天会话详情
     */
    @GetMapping("/{sessionId}")
    public R<ChatSession> getSession(@PathVariable("sessionId") Long sessionId) {
        ChatSession session = chatSessionService.selectChatSessionBySessionId(sessionId);
        return session != null ? R.ok(session) : R.fail("聊天会话不存在");
    }

    /**
     * 查询聊天会话列表
     */
    @GetMapping("/list")
    public R<List<ChatSession>> list(ChatSession chatSession) {
        List<ChatSession> list = chatSessionService.selectChatSessionList(chatSession);
        return R.ok(list);
    }

    /**
     * 新增聊天会话
     */
    @PostMapping
    public R<Integer> add(@RequestBody ChatSession chatSession) {
        int result = chatSessionService.insertChatSession(chatSession);
        return result > 0 ? R.ok(result, "新增聊天会话成功") : R.fail("新增聊天会话失败");
    }

    /**
     * 修改聊天会话
     */
    @PutMapping
    public R<Integer> edit(@RequestBody ChatSession chatSession) {
        int result = chatSessionService.updateChatSession(chatSession);
        return result > 0 ? R.ok(result, "修改聊天会话成功") : R.fail("修改聊天会话失败");
    }

    /**
     * 批量删除聊天会话
     */
    @DeleteMapping("/{sessionIds}")
    public R<Integer> remove(@PathVariable Long[] sessionIds) {
        int result = chatSessionService.deleteChatSessionBySessionIds(sessionIds);
        return result > 0 ? R.ok(result, "删除聊天会话成功") : R.fail("删除聊天会话失败");
    }

    @GetMapping("/unread")
    public AjaxResult unread(Long fromType, Long fromId) {
        return AjaxResult.success(chatSessionService.selectUnreadChatSessionList(fromType, fromId));
    }

    @GetMapping("/sessions")
    public AjaxResult getChatSessionList(Long fromType, Long fromId) {
        return AjaxResult.success(chatSessionService.selectRecentChatSessions(fromType,fromId));
    }

    @PostMapping("/increaseUnreadCount")
    public AjaxResult increaseUnreadCount(Long sessionId) {
        return AjaxResult.success(chatSessionService.increaseUnreadCount(sessionId));
    }

    @PostMapping("/readUnreadCount")
    public AjaxResult readUnreadCount(Long sessionId) {
        return AjaxResult.success(chatSessionService.readUnreadCount(sessionId));
    }

    @GetMapping("/systemSession")
    public AjaxResult getSystemSession(Long fromType, Long fromId) {
        Long sessionId = chatSessionService.selectChatSessionIdByFromTo(4L,0L,fromType,fromId);
        ChatSession chatSession = chatSessionService.selectChatSessionBySessionId(sessionId);
        return AjaxResult.success("操作成功",chatSession);
    }
}