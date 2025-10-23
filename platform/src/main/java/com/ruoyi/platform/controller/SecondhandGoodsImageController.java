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
import com.ruoyi.platform.domain.SecondhandGoodsImage;
import com.ruoyi.platform.service.ISecondhandGoodsImageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 二手商品图片(支持1-9张图片)Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/secondhand/image")
public class SecondhandGoodsImageController extends BaseController
{
    @Autowired
    private ISecondhandGoodsImageService secondhandGoodsImageService;

    /**
     * 查询二手商品图片(支持1-9张图片)列表
     */
    @PreAuthorize("@ss.hasPermi('secondhand:image:list')")
    @GetMapping("/list")
    public TableDataInfo list(SecondhandGoodsImage secondhandGoodsImage)
    {
        startPage();
        List<SecondhandGoodsImage> list = secondhandGoodsImageService.selectSecondhandGoodsImageList(secondhandGoodsImage);
        return getDataTable(list);
    }

    /**
     * 导出二手商品图片(支持1-9张图片)列表
     */
    @PreAuthorize("@ss.hasPermi('secondhand:image:export')")
    @Log(title = "二手商品图片(支持1-9张图片)", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SecondhandGoodsImage secondhandGoodsImage)
    {
        List<SecondhandGoodsImage> list = secondhandGoodsImageService.selectSecondhandGoodsImageList(secondhandGoodsImage);
        ExcelUtil<SecondhandGoodsImage> util = new ExcelUtil<SecondhandGoodsImage>(SecondhandGoodsImage.class);
        util.exportExcel(response, list, "二手商品图片(支持1-9张图片)数据");
    }

    /**
     * 获取二手商品图片(支持1-9张图片)详细信息
     */
    @PreAuthorize("@ss.hasPermi('secondhand:image:query')")
    @GetMapping(value = "/{secondhandGoodsImageId}")
    public AjaxResult getInfo(@PathVariable("secondhandGoodsImageId") Long secondhandGoodsImageId)
    {
        return success(secondhandGoodsImageService.selectSecondhandGoodsImageBySecondhandGoodsImageId(secondhandGoodsImageId));
    }

    /**
     * 新增二手商品图片(支持1-9张图片)
     */
    @PreAuthorize("@ss.hasPermi('secondhand:image:add')")
    @Log(title = "二手商品图片(支持1-9张图片)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SecondhandGoodsImage secondhandGoodsImage)
    {
        return toAjax(secondhandGoodsImageService.insertSecondhandGoodsImage(secondhandGoodsImage));
    }

    /**
     * 修改二手商品图片(支持1-9张图片)
     */
    @PreAuthorize("@ss.hasPermi('secondhand:image:edit')")
    @Log(title = "二手商品图片(支持1-9张图片)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SecondhandGoodsImage secondhandGoodsImage)
    {
        return toAjax(secondhandGoodsImageService.updateSecondhandGoodsImage(secondhandGoodsImage));
    }

    /**
     * 删除二手商品图片(支持1-9张图片)
     */
    @PreAuthorize("@ss.hasPermi('secondhand:image:remove')")
    @Log(title = "二手商品图片(支持1-9张图片)", businessType = BusinessType.DELETE)
	@DeleteMapping("/{secondhandGoodsImageIds}")
    public AjaxResult remove(@PathVariable Long[] secondhandGoodsImageIds)
    {
        return toAjax(secondhandGoodsImageService.deleteSecondhandGoodsImageBySecondhandGoodsImageIds(secondhandGoodsImageIds));
    }
}
