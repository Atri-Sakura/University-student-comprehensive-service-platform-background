package com.ruoyi.platform.controller1.chat;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.ChatAttachment;
import com.ruoyi.platform.service.IChatAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息附件Controller
 *
 * @author ruoyi
 * @date 2025-10-22
 */
@RestController
@RequestMapping("/platform/chat/attachment")
public class ChatAttachmentController1 {

    @Autowired
    private IChatAttachmentService chatAttachmentService;

    /**
     * 查询消息附件详情
     */
    @GetMapping("/{attachmentId}")
    public R<ChatAttachment> getAttachment(@PathVariable("attachmentId") Long attachmentId) {
        ChatAttachment attachment = chatAttachmentService.selectChatAttachmentByAttachmentId(attachmentId);
        return attachment != null ? R.ok(attachment) : R.fail("消息附件不存在");
    }

    /**
     * 查询消息附件列表
     */
    @GetMapping("/list")
    public R<List<ChatAttachment>> list(ChatAttachment chatAttachment) {
        List<ChatAttachment> list = chatAttachmentService.selectChatAttachmentList(chatAttachment);
        return R.ok(list);
    }

    /**
     * 新增消息附件
     */
    @PostMapping
    public R<Integer> add(@RequestBody ChatAttachment chatAttachment) {
        int result = chatAttachmentService.insertChatAttachment(chatAttachment);
        return result > 0 ? R.ok(result, "新增附件成功") : R.fail("新增附件失败");
    }

    /**
     * 修改消息附件
     */
    @PutMapping
    public R<Integer> edit(@RequestBody ChatAttachment chatAttachment) {
        int result = chatAttachmentService.updateChatAttachment(chatAttachment);
        return result > 0 ? R.ok(result, "修改附件成功") : R.fail("修改附件失败");
    }

    /**
     * 批量删除消息附件
     */
    @DeleteMapping("/{attachmentIds}")
    public R<Integer> remove(@PathVariable Long[] attachmentIds) {
        int result = chatAttachmentService.deleteChatAttachmentByAttachmentIds(attachmentIds);
        return result > 0 ? R.ok(result, "删除附件成功") : R.fail("删除附件失败");
    }
}