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
import com.ruoyi.platform.domain.UserPrivacy;
import com.ruoyi.platform.service.IUserPrivacyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户隐私设置Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/user1/privacy")
public class UserPrivacyController extends BaseController
{
    @Autowired
    private IUserPrivacyService userPrivacyService;

    /**
     * 查询用户隐私设置列表
     */
    @PreAuthorize("@ss.hasPermi('user1:privacy:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserPrivacy userPrivacy)
    {
        startPage();
        List<UserPrivacy> list = userPrivacyService.selectUserPrivacyList(userPrivacy);
        return getDataTable(list);
    }

    /**
     * 导出用户隐私设置列表
     */
    @PreAuthorize("@ss.hasPermi('user1:privacy:export')")
    @Log(title = "用户隐私设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserPrivacy userPrivacy)
    {
        List<UserPrivacy> list = userPrivacyService.selectUserPrivacyList(userPrivacy);
        ExcelUtil<UserPrivacy> util = new ExcelUtil<UserPrivacy>(UserPrivacy.class);
        util.exportExcel(response, list, "用户隐私设置数据");
    }

    /**
     * 获取用户隐私设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('user1:privacy:query')")
    @GetMapping(value = "/{userPrivacyId}")
    public AjaxResult getInfo(@PathVariable("userPrivacyId") Long userPrivacyId)
    {
        return success(userPrivacyService.selectUserPrivacyByUserPrivacyId(userPrivacyId));
    }

    /**
     * 新增用户隐私设置
     */
    @PreAuthorize("@ss.hasPermi('user1:privacy:add')")
    @Log(title = "用户隐私设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserPrivacy userPrivacy)
    {
        return toAjax(userPrivacyService.insertUserPrivacy(userPrivacy));
    }

    /**
     * 修改用户隐私设置
     */
    @PreAuthorize("@ss.hasPermi('user1:privacy:edit')")
    @Log(title = "用户隐私设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserPrivacy userPrivacy)
    {
        return toAjax(userPrivacyService.updateUserPrivacy(userPrivacy));
    }

    /**
     * 删除用户隐私设置
     */
    @PreAuthorize("@ss.hasPermi('user1:privacy:remove')")
    @Log(title = "用户隐私设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userPrivacyIds}")
    public AjaxResult remove(@PathVariable Long[] userPrivacyIds)
    {
        return toAjax(userPrivacyService.deleteUserPrivacyByUserPrivacyIds(userPrivacyIds));
    }
}
