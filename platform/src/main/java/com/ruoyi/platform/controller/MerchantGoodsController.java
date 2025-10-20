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
import com.ruoyi.platform.domain.MerchantGoods;
import com.ruoyi.platform.service.IMerchantGoodsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/merchant/goods")
public class MerchantGoodsController extends BaseController
{
    @Autowired
    private IMerchantGoodsService merchantGoodsService;

    /**
     * 查询商品列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:goods:list')")
    @GetMapping("/list")
    public TableDataInfo list(MerchantGoods merchantGoods)
    {
        startPage();
        List<MerchantGoods> list = merchantGoodsService.selectMerchantGoodsList(merchantGoods);
        return getDataTable(list);
    }

    /**
     * 导出商品列表
     */
    @PreAuthorize("@ss.hasPermi('merchant:goods:export')")
    @Log(title = "商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MerchantGoods merchantGoods)
    {
        List<MerchantGoods> list = merchantGoodsService.selectMerchantGoodsList(merchantGoods);
        ExcelUtil<MerchantGoods> util = new ExcelUtil<MerchantGoods>(MerchantGoods.class);
        util.exportExcel(response, list, "商品数据");
    }

    /**
     * 获取商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('merchant:goods:query')")
    @GetMapping(value = "/{merchantGoodsId}")
    public AjaxResult getInfo(@PathVariable("merchantGoodsId") Long merchantGoodsId)
    {
        return success(merchantGoodsService.selectMerchantGoodsByMerchantGoodsId(merchantGoodsId));
    }

    /**
     * 新增商品
     */
    @PreAuthorize("@ss.hasPermi('merchant:goods:add')")
    @Log(title = "商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MerchantGoods merchantGoods)
    {
        return toAjax(merchantGoodsService.insertMerchantGoods(merchantGoods));
    }

    /**
     * 修改商品
     */
    @PreAuthorize("@ss.hasPermi('merchant:goods:edit')")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantGoods merchantGoods)
    {
        return toAjax(merchantGoodsService.updateMerchantGoods(merchantGoods));
    }

    /**
     * 删除商品
     */
    @PreAuthorize("@ss.hasPermi('merchant:goods:remove')")
    @Log(title = "商品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{merchantGoodsIds}")
    public AjaxResult remove(@PathVariable Long[] merchantGoodsIds)
    {
        return toAjax(merchantGoodsService.deleteMerchantGoodsByMerchantGoodsIds(merchantGoodsIds));
    }
}
