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
import com.ruoyi.platform.domain.PlatformTag;
import com.ruoyi.platform.service.IPlatformTagService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 平台标签体系（管理用户和商品标签）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/platform/tag")
public class PlatformTagController extends BaseController
{
    @Autowired
    private IPlatformTagService platformTagService;

    /**
     * 查询平台标签体系（管理用户和商品标签）列表
     */
    @PreAuthorize("@ss.hasPermi('platform:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformTag platformTag)
    {
        startPage();
        List<PlatformTag> list = platformTagService.selectPlatformTagList(platformTag);
        return getDataTable(list);
    }

    /**
     * 导出平台标签体系（管理用户和商品标签）列表
     */
    @PreAuthorize("@ss.hasPermi('platform:tag:export')")
    @Log(title = "平台标签体系（管理用户和商品标签）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PlatformTag platformTag)
    {
        List<PlatformTag> list = platformTagService.selectPlatformTagList(platformTag);
        ExcelUtil<PlatformTag> util = new ExcelUtil<PlatformTag>(PlatformTag.class);
        util.exportExcel(response, list, "平台标签体系（管理用户和商品标签）数据");
    }

    /**
     * 获取平台标签体系（管理用户和商品标签）详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:tag:query')")
    @GetMapping(value = "/{platformTagId}")
    public AjaxResult getInfo(@PathVariable("platformTagId") Long platformTagId)
    {
        return success(platformTagService.selectPlatformTagByPlatformTagId(platformTagId));
    }

    /**
     * 新增平台标签体系（管理用户和商品标签）
     */
    @PreAuthorize("@ss.hasPermi('platform:tag:add')")
    @Log(title = "平台标签体系（管理用户和商品标签）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PlatformTag platformTag)
    {
        return toAjax(platformTagService.insertPlatformTag(platformTag));
    }

    /**
     * 修改平台标签体系（管理用户和商品标签）
     */
    @PreAuthorize("@ss.hasPermi('platform:tag:edit')")
    @Log(title = "平台标签体系（管理用户和商品标签）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformTag platformTag)
    {
        return toAjax(platformTagService.updatePlatformTag(platformTag));
    }

    /**
     * 删除平台标签体系（管理用户和商品标签）
     */
    @PreAuthorize("@ss.hasPermi('platform:tag:remove')")
    @Log(title = "平台标签体系（管理用户和商品标签）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{platformTagIds}")
    public AjaxResult remove(@PathVariable Long[] platformTagIds)
    {
        return toAjax(platformTagService.deletePlatformTagByPlatformTagIds(platformTagIds));
    }
}
