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
import com.ruoyi.platform.domain.GoodsEvaluationImage;
import com.ruoyi.platform.service.IGoodsEvaluationImageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品评价图片Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/goods/image")
public class GoodsEvaluationImageController extends BaseController
{
    @Autowired
    private IGoodsEvaluationImageService goodsEvaluationImageService;

    /**
     * 查询商品评价图片列表
     */
    @PreAuthorize("@ss.hasPermi('goods:image:list')")
    @GetMapping("/list")
    public TableDataInfo list(GoodsEvaluationImage goodsEvaluationImage)
    {
        startPage();
        List<GoodsEvaluationImage> list = goodsEvaluationImageService.selectGoodsEvaluationImageList(goodsEvaluationImage);
        return getDataTable(list);
    }

    /**
     * 导出商品评价图片列表
     */
    @PreAuthorize("@ss.hasPermi('goods:image:export')")
    @Log(title = "商品评价图片", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GoodsEvaluationImage goodsEvaluationImage)
    {
        List<GoodsEvaluationImage> list = goodsEvaluationImageService.selectGoodsEvaluationImageList(goodsEvaluationImage);
        ExcelUtil<GoodsEvaluationImage> util = new ExcelUtil<GoodsEvaluationImage>(GoodsEvaluationImage.class);
        util.exportExcel(response, list, "商品评价图片数据");
    }

    /**
     * 获取商品评价图片详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:image:query')")
    @GetMapping(value = "/{goodsEvaluationImageId}")
    public AjaxResult getInfo(@PathVariable("goodsEvaluationImageId") Long goodsEvaluationImageId)
    {
        return success(goodsEvaluationImageService.selectGoodsEvaluationImageByGoodsEvaluationImageId(goodsEvaluationImageId));
    }

    /**
     * 新增商品评价图片
     */
    @PreAuthorize("@ss.hasPermi('goods:image:add')")
    @Log(title = "商品评价图片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GoodsEvaluationImage goodsEvaluationImage)
    {
        return toAjax(goodsEvaluationImageService.insertGoodsEvaluationImage(goodsEvaluationImage));
    }

    /**
     * 修改商品评价图片
     */
    @PreAuthorize("@ss.hasPermi('goods:image:edit')")
    @Log(title = "商品评价图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GoodsEvaluationImage goodsEvaluationImage)
    {
        return toAjax(goodsEvaluationImageService.updateGoodsEvaluationImage(goodsEvaluationImage));
    }

    /**
     * 删除商品评价图片
     */
    @PreAuthorize("@ss.hasPermi('goods:image:remove')")
    @Log(title = "商品评价图片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{goodsEvaluationImageIds}")
    public AjaxResult remove(@PathVariable Long[] goodsEvaluationImageIds)
    {
        return toAjax(goodsEvaluationImageService.deleteGoodsEvaluationImageByGoodsEvaluationImageIds(goodsEvaluationImageIds));
    }
}
