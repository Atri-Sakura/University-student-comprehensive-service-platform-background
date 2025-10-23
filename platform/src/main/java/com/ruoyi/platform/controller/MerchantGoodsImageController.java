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
import com.ruoyi.platform.domain.MerchantGoodsImage;
import com.ruoyi.platform.service.IMerchantGoodsImageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品图片关联（支持多图展示）Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/merchant/image")
public class MerchantGoodsImageController extends BaseController
{
    @Autowired
    private IMerchantGoodsImageService merchantGoodsImageService;

    /**
     * 查询商品图片关联（支持多图展示）列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:image:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantGoodsImage merchantGoodsImage)
    {
        startPage();
        List<MerchantGoodsImage> list = merchantGoodsImageService.selectMerchantGoodsImageList(merchantGoodsImage);
        return getDataTable(list);
    }

    /**
     * 导出商品图片关联（支持多图展示）列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:image:export')")
    @Log(title = "商品图片关联（支持多图展示）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MerchantGoodsImage merchantGoodsImage)
    {
        List<MerchantGoodsImage> list = merchantGoodsImageService.selectMerchantGoodsImageList(merchantGoodsImage);
        ExcelUtil<MerchantGoodsImage> util = new ExcelUtil<MerchantGoodsImage>(MerchantGoodsImage.class);
        util.exportExcel(response, list, "商品图片关联（支持多图展示）数据");
    }

    /**
     * 获取商品图片关联（支持多图展示）详细信息
     */
    @PreAuthorize("@ss.hasPermi('merchant:image:query')")
    @GetMapping(value = "/{merchantGoodsImageId}")
    public AjaxResult getInfo(@PathVariable("merchantGoodsImageId") Long merchantGoodsImageId)
    {
        return success(merchantGoodsImageService.selectMerchantGoodsImageByMerchantGoodsImageId(merchantGoodsImageId));
    }

    /**
     * 新增商品图片关联（支持多图展示）
     */
    @PreAuthorize("@ss.hasPermi('merchant:image:add')")
    @Log(title = "商品图片关联（支持多图展示）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantGoodsImage merchantGoodsImage)
    {
        return toAjax(merchantGoodsImageService.insertMerchantGoodsImage(merchantGoodsImage));
    }

    /**
     * 修改商品图片关联（支持多图展示）
     */
    @PreAuthorize("@ss.hasPermi('merchant:image:edit')")
    @Log(title = "商品图片关联（支持多图展示）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantGoodsImage merchantGoodsImage)
    {
        return toAjax(merchantGoodsImageService.updateMerchantGoodsImage(merchantGoodsImage));
    }

    /**
     * 删除商品图片关联（支持多图展示）
     */
    @PreAuthorize("@ss.hasPermi('merchant:image:remove')")
    @Log(title = "商品图片关联（支持多图展示）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{merchantGoodsImageIds}")
    public AjaxResult remove(@PathVariable Long[] merchantGoodsImageIds)
    {
        return toAjax(merchantGoodsImageService.deleteMerchantGoodsImageByMerchantGoodsImageIds(merchantGoodsImageIds));
    }
}
