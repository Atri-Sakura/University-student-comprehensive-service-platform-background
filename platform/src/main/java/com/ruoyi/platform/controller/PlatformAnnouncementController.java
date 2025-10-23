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
import com.ruoyi.platform.domain.PlatformAnnouncement;
import com.ruoyi.platform.service.IPlatformAnnouncementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 系统公告Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/announcement")
public class PlatformAnnouncementController extends BaseController
{
    @Autowired
    private IPlatformAnnouncementService platformAnnouncementService;

    /**
     * 查询系统公告列表
     */
    @PreAuthorize("@ss.hasPermi('platform:announcement:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformAnnouncement platformAnnouncement)
    {
        startPage();
        List<PlatformAnnouncement> list = platformAnnouncementService.selectPlatformAnnouncementList(platformAnnouncement);
        return getDataTable(list);
    }

    /**
     * 导出系统公告列表
     */
    @PreAuthorize("@ss.hasPermi('platform:announcement:export')")
    @Log(title = "系统公告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlatformAnnouncement platformAnnouncement)
    {
        List<PlatformAnnouncement> list = platformAnnouncementService.selectPlatformAnnouncementList(platformAnnouncement);
        ExcelUtil<PlatformAnnouncement> util = new ExcelUtil<PlatformAnnouncement>(PlatformAnnouncement.class);
        util.exportExcel(response, list, "系统公告数据");
    }

    /**
     * 获取系统公告详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:announcement:query')")
    @GetMapping(value = "/{platformAnnouncementId}")
    public AjaxResult getInfo(@PathVariable("platformAnnouncementId") Long platformAnnouncementId)
    {
        return success(platformAnnouncementService.selectPlatformAnnouncementByPlatformAnnouncementId(platformAnnouncementId));
    }

    /**
     * 新增系统公告
     */
    @PreAuthorize("@ss.hasPermi('platform:announcement:add')")
    @Log(title = "系统公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlatformAnnouncement platformAnnouncement)
    {
        return toAjax(platformAnnouncementService.insertPlatformAnnouncement(platformAnnouncement));
    }

    /**
     * 修改系统公告
     */
    @PreAuthorize("@ss.hasPermi('platform:announcement:edit')")
    @Log(title = "系统公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformAnnouncement platformAnnouncement)
    {
        return toAjax(platformAnnouncementService.updatePlatformAnnouncement(platformAnnouncement));
    }

    /**
     * 删除系统公告
     */
    @PreAuthorize("@ss.hasPermi('platform:announcement:remove')")
    @Log(title = "系统公告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{platformAnnouncementIds}")
    public AjaxResult remove(@PathVariable Long[] platformAnnouncementIds)
    {
        return toAjax(platformAnnouncementService.deletePlatformAnnouncementByPlatformAnnouncementIds(platformAnnouncementIds));
    }
}
