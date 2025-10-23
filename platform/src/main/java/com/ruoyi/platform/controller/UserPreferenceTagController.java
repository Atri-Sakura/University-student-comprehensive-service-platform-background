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
import com.ruoyi.platform.domain.UserPreferenceTag;
import com.ruoyi.platform.service.IUserPreferenceTagService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户偏好标签Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/user1/tag")
public class UserPreferenceTagController extends BaseController
{
    @Autowired
    private IUserPreferenceTagService userPreferenceTagService;

    /**
     * 查询用户偏好标签列表
     */
    @PreAuthorize("@ss.hasPermi('user1:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserPreferenceTag userPreferenceTag)
    {
        startPage();
        List<UserPreferenceTag> list = userPreferenceTagService.selectUserPreferenceTagList(userPreferenceTag);
        return getDataTable(list);
    }

    /**
     * 导出用户偏好标签列表
     */
    @PreAuthorize("@ss.hasPermi('user1:tag:export')")
    @Log(title = "用户偏好标签", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserPreferenceTag userPreferenceTag)
    {
        List<UserPreferenceTag> list = userPreferenceTagService.selectUserPreferenceTagList(userPreferenceTag);
        ExcelUtil<UserPreferenceTag> util = new ExcelUtil<UserPreferenceTag>(UserPreferenceTag.class);
        util.exportExcel(response, list, "用户偏好标签数据");
    }

    /**
     * 获取用户偏好标签详细信息
     */
    @PreAuthorize("@ss.hasPermi('user1:tag:query')")
    @GetMapping(value = "/{userPreferenceTagId}")
    public AjaxResult getInfo(@PathVariable("userPreferenceTagId") Long userPreferenceTagId)
    {
        return success(userPreferenceTagService.selectUserPreferenceTagByUserPreferenceTagId(userPreferenceTagId));
    }

    /**
     * 新增用户偏好标签
     */
    @PreAuthorize("@ss.hasPermi('user1:tag:add')")
    @Log(title = "用户偏好标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserPreferenceTag userPreferenceTag)
    {
        return toAjax(userPreferenceTagService.insertUserPreferenceTag(userPreferenceTag));
    }

    /**
     * 修改用户偏好标签
     */
    @PreAuthorize("@ss.hasPermi('user1:tag:edit')")
    @Log(title = "用户偏好标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserPreferenceTag userPreferenceTag)
    {
        return toAjax(userPreferenceTagService.updateUserPreferenceTag(userPreferenceTag));
    }

    /**
     * 删除用户偏好标签
     */
    @PreAuthorize("@ss.hasPermi('user1:tag:remove')")
    @Log(title = "用户偏好标签", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userPreferenceTagIds}")
    public AjaxResult remove(@PathVariable Long[] userPreferenceTagIds)
    {
        return toAjax(userPreferenceTagService.deleteUserPreferenceTagByUserPreferenceTagIds(userPreferenceTagIds));
    }
}
