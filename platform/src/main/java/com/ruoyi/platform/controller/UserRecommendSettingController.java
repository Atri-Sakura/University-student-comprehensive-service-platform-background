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
import com.ruoyi.platform.domain.UserRecommendSetting;
import com.ruoyi.platform.service.IUserRecommendSettingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户个性化推荐设置Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/user1/setting")
public class UserRecommendSettingController extends BaseController
{
    @Autowired
    private IUserRecommendSettingService userRecommendSettingService;

    /**
     * 查询用户个性化推荐设置列表
     */
    @PreAuthorize("@ss.hasPermi('user1:setting:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserRecommendSetting userRecommendSetting)
    {
        startPage();
        List<UserRecommendSetting> list = userRecommendSettingService.selectUserRecommendSettingList(userRecommendSetting);
        return getDataTable(list);
    }

    /**
     * 导出用户个性化推荐设置列表
     */
    @PreAuthorize("@ss.hasPermi('user1:setting:export')")
    @Log(title = "用户个性化推荐设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserRecommendSetting userRecommendSetting)
    {
        List<UserRecommendSetting> list = userRecommendSettingService.selectUserRecommendSettingList(userRecommendSetting);
        ExcelUtil<UserRecommendSetting> util = new ExcelUtil<UserRecommendSetting>(UserRecommendSetting.class);
        util.exportExcel(response, list, "用户个性化推荐设置数据");
    }

    /**
     * 获取用户个性化推荐设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('user1:setting:query')")
    @GetMapping(value = "/{userRecommendSettingId}")
    public AjaxResult getInfo(@PathVariable("userRecommendSettingId") Long userRecommendSettingId)
    {
        return success(userRecommendSettingService.selectUserRecommendSettingByUserRecommendSettingId(userRecommendSettingId));
    }

    /**
     * 新增用户个性化推荐设置
     */
    @PreAuthorize("@ss.hasPermi('user1:setting:add')")
    @Log(title = "用户个性化推荐设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserRecommendSetting userRecommendSetting)
    {
        return toAjax(userRecommendSettingService.insertUserRecommendSetting(userRecommendSetting));
    }

    /**
     * 修改用户个性化推荐设置
     */
    @PreAuthorize("@ss.hasPermi('user1:setting:edit')")
    @Log(title = "用户个性化推荐设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserRecommendSetting userRecommendSetting)
    {
        return toAjax(userRecommendSettingService.updateUserRecommendSetting(userRecommendSetting));
    }

    /**
     * 删除用户个性化推荐设置
     */
    @PreAuthorize("@ss.hasPermi('user1:setting:remove')")
    @Log(title = "用户个性化推荐设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userRecommendSettingIds}")
    public AjaxResult remove(@PathVariable Long[] userRecommendSettingIds)
    {
        return toAjax(userRecommendSettingService.deleteUserRecommendSettingByUserRecommendSettingIds(userRecommendSettingIds));
    }
}
