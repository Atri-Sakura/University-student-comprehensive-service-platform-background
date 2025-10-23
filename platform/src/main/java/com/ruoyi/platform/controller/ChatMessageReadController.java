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
import com.ruoyi.platform.domain.ChatMessageRead;
import com.ruoyi.platform.service.IChatMessageReadService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/chat/read")
public class ChatMessageReadController extends BaseController
{
    @Autowired
    private IChatMessageReadService chatMessageReadService;

    /**
     * 查询消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）列表
     */
    @PreAuthorize("@ss.hasPermi('chat:read:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatMessageRead chatMessageRead)
    {
        startPage();
        List<ChatMessageRead> list = chatMessageReadService.selectChatMessageReadList(chatMessageRead);
        return getDataTable(list);
    }

    /**
     * 导出消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）列表
     */
    @PreAuthorize("@ss.hasPermi('chat:read:export')")
    @Log(title = "消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatMessageRead chatMessageRead)
    {
        List<ChatMessageRead> list = chatMessageReadService.selectChatMessageReadList(chatMessageRead);
        ExcelUtil<ChatMessageRead> util = new ExcelUtil<ChatMessageRead>(ChatMessageRead.class);
        util.exportExcel(response, list, "消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）数据");
    }

    /**
     * 获取消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）详细信息
     */
    @PreAuthorize("@ss.hasPermi('chat:read:query')")
    @GetMapping(value = "/{readId}")
    public AjaxResult getInfo(@PathVariable("readId") Long readId)
    {
        return success(chatMessageReadService.selectChatMessageReadByReadId(readId));
    }

    /**
     * 新增消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     */
    @PreAuthorize("@ss.hasPermi('chat:read:add')")
    @Log(title = "消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatMessageRead chatMessageRead)
    {
        return toAjax(chatMessageReadService.insertChatMessageRead(chatMessageRead));
    }

    /**
     * 修改消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     */
    @PreAuthorize("@ss.hasPermi('chat:read:edit')")
    @Log(title = "消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatMessageRead chatMessageRead)
    {
        return toAjax(chatMessageReadService.updateChatMessageRead(chatMessageRead));
    }

    /**
     * 删除消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
     */
    @PreAuthorize("@ss.hasPermi('chat:read:remove')")
    @Log(title = "消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{readIds}")
    public AjaxResult remove(@PathVariable Long[] readIds)
    {
        return toAjax(chatMessageReadService.deleteChatMessageReadByReadIds(readIds));
    }
}
