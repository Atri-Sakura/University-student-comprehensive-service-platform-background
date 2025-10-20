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
import com.ruoyi.platform.domain.ChatMessage;
import com.ruoyi.platform.service.IChatMessageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 聊天消息（存储单条消息的核心信息）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/chat/message")
public class ChatMessageController extends BaseController
{
    @Autowired
    private IChatMessageService chatMessageService;

    /**
     * 查询聊天消息（存储单条消息的核心信息）列表
     */
    @PreAuthorize("@ss.hasPermi('chat:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatMessage chatMessage)
    {
        startPage();
        List<ChatMessage> list = chatMessageService.selectChatMessageList(chatMessage);
        return getDataTable(list);
    }

    /**
     * 导出聊天消息（存储单条消息的核心信息）列表
     */
    @PreAuthorize("@ss.hasPermi('chat:message:export')")
    @Log(title = "聊天消息（存储单条消息的核心信息）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatMessage chatMessage)
    {
        List<ChatMessage> list = chatMessageService.selectChatMessageList(chatMessage);
        ExcelUtil<ChatMessage> util = new ExcelUtil<ChatMessage>(ChatMessage.class);
        util.exportExcel(response, list, "聊天消息（存储单条消息的核心信息）数据");
    }

    /**
     * 获取聊天消息（存储单条消息的核心信息）详细信息
     */
    @PreAuthorize("@ss.hasPermi('chat:message:query')")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable("messageId") Long messageId)
    {
        return success(chatMessageService.selectChatMessageByMessageId(messageId));
    }

    /**
     * 新增聊天消息（存储单条消息的核心信息）
     */
    @PreAuthorize("@ss.hasPermi('chat:message:add')")
    @Log(title = "聊天消息（存储单条消息的核心信息）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatMessage chatMessage)
    {
        return toAjax(chatMessageService.insertChatMessage(chatMessage));
    }

    /**
     * 修改聊天消息（存储单条消息的核心信息）
     */
    @PreAuthorize("@ss.hasPermi('chat:message:edit')")
    @Log(title = "聊天消息（存储单条消息的核心信息）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatMessage chatMessage)
    {
        return toAjax(chatMessageService.updateChatMessage(chatMessage));
    }

    /**
     * 删除聊天消息（存储单条消息的核心信息）
     */
    @PreAuthorize("@ss.hasPermi('chat:message:remove')")
    @Log(title = "聊天消息（存储单条消息的核心信息）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds)
    {
        return toAjax(chatMessageService.deleteChatMessageByMessageIds(messageIds));
    }
}
