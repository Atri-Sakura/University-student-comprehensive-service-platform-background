package com.ruoyi.platform.controller1.chat;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.ChatMessage;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.platform.service.IChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天消息Controller
 *
 * @author ruoyi
 * @date 2025-10-22
 */
@RestController
@RequestMapping("/platform/chat/message")
public class ChatMessageController1 {

    @Autowired
    private IChatMessageService chatMessageService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询聊天消息详情
     */
    @GetMapping("/{messageId}")
    public R<ChatMessage> getMessage(@PathVariable("messageId") Long messageId) {
        ChatMessage message = chatMessageService.selectChatMessageByMessageId(messageId);
        return message != null ? R.ok(message) : R.fail("聊天消息不存在");
    }

    /**
     * 查询聊天消息列表
     */
    @GetMapping("/list")
    public R<List<ChatMessage>> list(ChatMessage chatMessage) {
        List<ChatMessage> list = chatMessageService.selectChatMessageList(chatMessage);
        return R.ok(list);
    }

    /**
     * 新增聊天消息
     */
    @PostMapping
    public R<Integer> add(@RequestBody ChatMessage chatMessage) {
        int result = chatMessageService.insertChatMessage(chatMessage);
        return result > 0 ? R.ok(result, "新增聊天消息成功") : R.fail("新增聊天消息失败");
    }

    /**
     * 修改聊天消息
     */
    @PutMapping
    public R<Integer> edit(@RequestBody ChatMessage chatMessage) {
        int result = chatMessageService.updateChatMessage(chatMessage);
        return result > 0 ? R.ok(result, "修改聊天消息成功") : R.fail("修改聊天消息失败");
    }

    /**
     * 批量删除聊天消息
     */
    @DeleteMapping("/{messageIds}")
    public R<Integer> remove(@PathVariable Long[] messageIds) {
        int result = chatMessageService.deleteChatMessageByMessageIds(messageIds);
        return result > 0 ? R.ok(result, "删除聊天消息成功") : R.fail("删除聊天消息失败");
    }
}