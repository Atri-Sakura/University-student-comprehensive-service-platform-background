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
import com.ruoyi.platform.domain.UserBehaviorLog;
import com.ruoyi.platform.service.IUserBehaviorLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户行为记录Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/user1/log")
public class UserBehaviorLogController extends BaseController
{
    @Autowired
    private IUserBehaviorLogService userBehaviorLogService;

    /**
     * 查询用户行为记录列表
     */
    @PreAuthorize("@ss.hasPermi('user1:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserBehaviorLog userBehaviorLog)
    {
        startPage();
        List<UserBehaviorLog> list = userBehaviorLogService.selectUserBehaviorLogList(userBehaviorLog);
        return getDataTable(list);
    }

    /**
     * 导出用户行为记录列表
     */
    @PreAuthorize("@ss.hasPermi('user1:log:export')")
    @Log(title = "用户行为记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserBehaviorLog userBehaviorLog)
    {
        List<UserBehaviorLog> list = userBehaviorLogService.selectUserBehaviorLogList(userBehaviorLog);
        ExcelUtil<UserBehaviorLog> util = new ExcelUtil<UserBehaviorLog>(UserBehaviorLog.class);
        util.exportExcel(response, list, "用户行为记录数据");
    }

    /**
     * 获取用户行为记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('user1:log:query')")
    @GetMapping(value = "/{userBehaviorLogId}")
    public AjaxResult getInfo(@PathVariable("userBehaviorLogId") Long userBehaviorLogId)
    {
        return success(userBehaviorLogService.selectUserBehaviorLogByUserBehaviorLogId(userBehaviorLogId));
    }

    /**
     * 新增用户行为记录
     */
    @PreAuthorize("@ss.hasPermi('user1:log:add')")
    @Log(title = "用户行为记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserBehaviorLog userBehaviorLog)
    {
        return toAjax(userBehaviorLogService.insertUserBehaviorLog(userBehaviorLog));
    }

    /**
     * 修改用户行为记录
     */
    @PreAuthorize("@ss.hasPermi('user1:log:edit')")
    @Log(title = "用户行为记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserBehaviorLog userBehaviorLog)
    {
        return toAjax(userBehaviorLogService.updateUserBehaviorLog(userBehaviorLog));
    }

    /**
     * 删除用户行为记录
     */
    @PreAuthorize("@ss.hasPermi('user1:log:remove')")
    @Log(title = "用户行为记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userBehaviorLogIds}")
    public AjaxResult remove(@PathVariable Long[] userBehaviorLogIds)
    {
        return toAjax(userBehaviorLogService.deleteUserBehaviorLogByUserBehaviorLogIds(userBehaviorLogIds));
    }
}
