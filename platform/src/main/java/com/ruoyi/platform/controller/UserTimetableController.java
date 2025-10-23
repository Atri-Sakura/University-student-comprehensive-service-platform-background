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
import com.ruoyi.platform.domain.UserTimetable;
import com.ruoyi.platform.service.IUserTimetableService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 个人课Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/user1/timetable")
public class UserTimetableController extends BaseController
{
    @Autowired
    private IUserTimetableService userTimetableService;

    /**
     * 查询个人课列表
     */
    @PreAuthorize("@ss.hasPermi('user1:timetable:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserTimetable userTimetable)
    {
        startPage();
        List<UserTimetable> list = userTimetableService.selectUserTimetableList(userTimetable);
        return getDataTable(list);
    }

    /**
     * 导出个人课列表
     */
    @PreAuthorize("@ss.hasPermi('user1:timetable:export')")
    @Log(title = "个人课", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserTimetable userTimetable)
    {
        List<UserTimetable> list = userTimetableService.selectUserTimetableList(userTimetable);
        ExcelUtil<UserTimetable> util = new ExcelUtil<UserTimetable>(UserTimetable.class);
        util.exportExcel(response, list, "个人课数据");
    }

    /**
     * 获取个人课详细信息
     */
    @PreAuthorize("@ss.hasPermi('user1:timetable:query')")
    @GetMapping(value = "/{userTimetableId}")
    public AjaxResult getInfo(@PathVariable("userTimetableId") Long userTimetableId)
    {
        return success(userTimetableService.selectUserTimetableByUserTimetableId(userTimetableId));
    }

    /**
     * 新增个人课
     */
    @PreAuthorize("@ss.hasPermi('user1:timetable:add')")
    @Log(title = "个人课", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserTimetable userTimetable)
    {
        return toAjax(userTimetableService.insertUserTimetable(userTimetable));
    }

    /**
     * 修改个人课
     */
    @PreAuthorize("@ss.hasPermi('user1:timetable:edit')")
    @Log(title = "个人课", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserTimetable userTimetable)
    {
        return toAjax(userTimetableService.updateUserTimetable(userTimetable));
    }

    /**
     * 删除个人课
     */
    @PreAuthorize("@ss.hasPermi('user1:timetable:remove')")
    @Log(title = "个人课", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userTimetableIds}")
    public AjaxResult remove(@PathVariable Long[] userTimetableIds)
    {
        return toAjax(userTimetableService.deleteUserTimetableByUserTimetableIds(userTimetableIds));
    }
}
