package com.ruoyi.platform.controller1.chat;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.ChatMessageRead;
import com.ruoyi.platform.service.IChatMessageReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息已读状态Controller
 *
 * @author ruoyi
 * @date 2025-10-22
 */
@RestController
@RequestMapping("/platform/chat/message/read")
public class ChatMessageReadController1 {

    @Autowired
    private IChatMessageReadService chatMessageReadService;

    /**
     * 查询消息已读状态详情
     */
    @GetMapping("/{readId}")
    public R<ChatMessageRead> getMessageRead(@PathVariable("readId") Long readId) {
        ChatMessageRead messageRead = chatMessageReadService.selectChatMessageReadByReadId(readId);
        return messageRead != null ? R.ok(messageRead) : R.fail("消息已读状态记录不存在");
    }

    /**
     * 查询消息已读状态列表
     */
    @GetMapping("/list")
    public R<List<ChatMessageRead>> list(ChatMessageRead chatMessageRead) {
        List<ChatMessageRead> list = chatMessageReadService.selectChatMessageReadList(chatMessageRead);
        return R.ok(list);
    }

    /**
     * 新增消息已读状态
     */
    @PostMapping
    public R<Integer> add(@RequestBody ChatMessageRead chatMessageRead) {
        int result = chatMessageReadService.insertChatMessageRead(chatMessageRead);
        return result > 0 ? R.ok(result, "新增消息已读状态成功") : R.fail("新增消息已读状态失败");
    }

    /**
     * 修改消息已读状态
     */
    @PutMapping
    public R<Integer> edit(@RequestBody ChatMessageRead chatMessageRead) {
        int result = chatMessageReadService.updateChatMessageRead(chatMessageRead);
        return result > 0 ? R.ok(result, "修改消息已读状态成功") : R.fail("修改消息已读状态失败");
    }

    /**
     * 批量删除消息已读状态
     */
    @DeleteMapping("/{readIds}")
    public R<Integer> remove(@PathVariable Long[] readIds) {
        int result = chatMessageReadService.deleteChatMessageReadByReadIds(readIds);
        return result > 0 ? R.ok(result, "删除消息已读状态成功") : R.fail("删除消息已读状态失败");
    }
}