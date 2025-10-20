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
import com.ruoyi.platform.domain.ChatSession;
import com.ruoyi.platform.service.IChatSessionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 聊天会话（管理双方的聊天窗口关系）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/chat/session")
public class ChatSessionController extends BaseController
{
    @Autowired
    private IChatSessionService chatSessionService;

    /**
     * 查询聊天会话（管理双方的聊天窗口关系）列表
     */
    @PreAuthorize("@ss.hasPermi('chat:session:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatSession chatSession)
    {
        startPage();
        List<ChatSession> list = chatSessionService.selectChatSessionList(chatSession);
        return getDataTable(list);
    }

    /**
     * 导出聊天会话（管理双方的聊天窗口关系）列表
     */
    @PreAuthorize("@ss.hasPermi('chat:session:export')")
    @Log(title = "聊天会话（管理双方的聊天窗口关系）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatSession chatSession)
    {
        List<ChatSession> list = chatSessionService.selectChatSessionList(chatSession);
        ExcelUtil<ChatSession> util = new ExcelUtil<ChatSession>(ChatSession.class);
        util.exportExcel(response, list, "聊天会话（管理双方的聊天窗口关系）数据");
    }

    /**
     * 获取聊天会话（管理双方的聊天窗口关系）详细信息
     */
    @PreAuthorize("@ss.hasPermi('chat:session:query')")
    @GetMapping(value = "/{sessionId}")
    public AjaxResult getInfo(@PathVariable("sessionId") Long sessionId)
    {
        return success(chatSessionService.selectChatSessionBySessionId(sessionId));
    }

    /**
     * 新增聊天会话（管理双方的聊天窗口关系）
     */
    @PreAuthorize("@ss.hasPermi('chat:session:add')")
    @Log(title = "聊天会话（管理双方的聊天窗口关系）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatSession chatSession)
    {
        return toAjax(chatSessionService.insertChatSession(chatSession));
    }

    /**
     * 修改聊天会话（管理双方的聊天窗口关系）
     */
    @PreAuthorize("@ss.hasPermi('chat:session:edit')")
    @Log(title = "聊天会话（管理双方的聊天窗口关系）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatSession chatSession)
    {
        return toAjax(chatSessionService.updateChatSession(chatSession));
    }

    /**
     * 删除聊天会话（管理双方的聊天窗口关系）
     */
    @PreAuthorize("@ss.hasPermi('chat:session:remove')")
    @Log(title = "聊天会话（管理双方的聊天窗口关系）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sessionIds}")
    public AjaxResult remove(@PathVariable Long[] sessionIds)
    {
        return toAjax(chatSessionService.deleteChatSessionBySessionIds(sessionIds));
    }
}
