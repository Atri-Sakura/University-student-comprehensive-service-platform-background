package com.ruoyi.platform.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.platform.domain.ChatAttachment;
import com.ruoyi.platform.service.IChatAttachmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 消息附件（存储图片/语音等附件的元信息）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/chat/attachment")
public class ChatAttachmentController extends BaseController
{
    @Autowired
    private IChatAttachmentService chatAttachmentService;

    /**
     * 查询消息附件（存储图片/语音等附件的元信息）列表
     */
    @PreAuthorize("@ss.hasPermi('chat:attachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatAttachment chatAttachment)
    {
        startPage();
        List<ChatAttachment> list = chatAttachmentService.selectChatAttachmentList(chatAttachment);
        return getDataTable(list);
    }

    /**
     * 导出消息附件（存储图片/语音等附件的元信息）列表
     */
    @PreAuthorize("@ss.hasPermi('chat:attachment:export')")
    @Log(title = "消息附件（存储图片/语音等附件的元信息）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatAttachment chatAttachment)
    {
        List<ChatAttachment> list = chatAttachmentService.selectChatAttachmentList(chatAttachment);
        ExcelUtil<ChatAttachment> util = new ExcelUtil<ChatAttachment>(ChatAttachment.class);
        util.exportExcel(response, list, "消息附件（存储图片/语音等附件的元信息）数据");
    }

    /**
     * 获取消息附件（存储图片/语音等附件的元信息）详细信息
     */
    @PreAuthorize("@ss.hasPermi('chat:attachment:query')")
    @GetMapping(value = "/{attachmentId}")
    public AjaxResult getInfo(@PathVariable("attachmentId") Long attachmentId)
    {
        return success(chatAttachmentService.selectChatAttachmentByAttachmentId(attachmentId));
    }

    /**
     * 新增消息附件（存储图片/语音等附件的元信息）
     */
    @PreAuthorize("@ss.hasPermi('chat:attachment:add')")
    @Log(title = "消息附件（存储图片/语音等附件的元信息）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatAttachment chatAttachment)
    {
        return toAjax(chatAttachmentService.insertChatAttachment(chatAttachment));
    }

    /**
     * 修改消息附件（存储图片/语音等附件的元信息）
     */
    @PreAuthorize("@ss.hasPermi('chat:attachment:edit')")
    @Log(title = "消息附件（存储图片/语音等附件的元信息）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatAttachment chatAttachment)
    {
        return toAjax(chatAttachmentService.updateChatAttachment(chatAttachment));
    }

    /**
     * 删除消息附件（存储图片/语音等附件的元信息）
     */
    @PreAuthorize("@ss.hasPermi('chat:attachment:remove')")
    @Log(title = "消息附件（存储图片/语音等附件的元信息）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{attachmentIds}")
    public AjaxResult remove(@PathVariable Long[] attachmentIds)
    {
        return toAjax(chatAttachmentService.deleteChatAttachmentByAttachmentIds(attachmentIds));
    }
}
