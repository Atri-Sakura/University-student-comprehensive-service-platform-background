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
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.service.IUserBaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户基础信息Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/user1/base")
public class UserBaseController extends BaseController
{
    @Autowired
    private IUserBaseService userBaseService;

    /**
     * 查询用户基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('user1:base:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserBase userBase)
    {
        startPage();
        List<UserBase> list = userBaseService.selectUserBaseList(userBase);
        return getDataTable(list);
    }

    /**
     * 导出用户基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('user1:base:export')")
    @Log(title = "用户基础信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserBase userBase)
    {
        List<UserBase> list = userBaseService.selectUserBaseList(userBase);
        ExcelUtil<UserBase> util = new ExcelUtil<UserBase>(UserBase.class);
        util.exportExcel(response, list, "用户基础信息数据");
    }

    /**
     * 获取用户基础信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('user1:base:query')")
    @GetMapping(value = "/{userBaseId}")
    public AjaxResult getInfo(@PathVariable("userBaseId") Long userBaseId)
    {
        return success(userBaseService.selectUserBaseByUserBaseId(userBaseId));
    }

    /**
     * 新增用户基础信息
     */
    @PreAuthorize("@ss.hasPermi('user1:base:add')")
    @Log(title = "用户基础信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserBase userBase)
    {
        return toAjax(userBaseService.insertUserBase(userBase));
    }

    /**
     * 修改用户基础信息
     */
    @PreAuthorize("@ss.hasPermi('user1:base:edit')")
    @Log(title = "用户基础信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserBase userBase)
    {
        return toAjax(userBaseService.updateUserBase(userBase));
    }

    /**
     * 删除用户基础信息
     */
    @PreAuthorize("@ss.hasPermi('user1:base:remove')")
    @Log(title = "用户基础信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userBaseIds}")
    public AjaxResult remove(@PathVariable Long[] userBaseIds)
    {
        return toAjax(userBaseService.deleteUserBaseByUserBaseIds(userBaseIds));
    }
}
