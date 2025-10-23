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
import com.ruoyi.platform.domain.GoodsEvaluation;
import com.ruoyi.platform.service.IGoodsEvaluationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品评价Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/goods/evaluation")
public class GoodsEvaluationController extends BaseController
{
    @Autowired
    private IGoodsEvaluationService goodsEvaluationService;

    /**
     * 查询商品评价列表
     */
    @PreAuthorize("@ss.hasPermi('goods:evaluation:list')")
    @GetMapping("/list")
    public TableDataInfo list(GoodsEvaluation goodsEvaluation)
    {
        startPage();
        List<GoodsEvaluation> list = goodsEvaluationService.selectGoodsEvaluationList(goodsEvaluation);
        return getDataTable(list);
    }

    /**
     * 导出商品评价列表
     */
    @PreAuthorize("@ss.hasPermi('goods:evaluation:export')")
    @Log(title = "商品评价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GoodsEvaluation goodsEvaluation)
    {
        List<GoodsEvaluation> list = goodsEvaluationService.selectGoodsEvaluationList(goodsEvaluation);
        ExcelUtil<GoodsEvaluation> util = new ExcelUtil<GoodsEvaluation>(GoodsEvaluation.class);
        util.exportExcel(response, list, "商品评价数据");
    }

    /**
     * 获取商品评价详细信息
     */
    @PreAuthorize("@ss.hasPermi('goods:evaluation:query')")
    @GetMapping(value = "/{goodsEvaluationId}")
    public AjaxResult getInfo(@PathVariable("goodsEvaluationId") Long goodsEvaluationId)
    {
        return success(goodsEvaluationService.selectGoodsEvaluationByGoodsEvaluationId(goodsEvaluationId));
    }

    /**
     * 新增商品评价
     */
    @PreAuthorize("@ss.hasPermi('goods:evaluation:add')")
    @Log(title = "商品评价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GoodsEvaluation goodsEvaluation)
    {
        return toAjax(goodsEvaluationService.insertGoodsEvaluation(goodsEvaluation));
    }

    /**
     * 修改商品评价
     */
    @PreAuthorize("@ss.hasPermi('goods:evaluation:edit')")
    @Log(title = "商品评价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GoodsEvaluation goodsEvaluation)
    {
        return toAjax(goodsEvaluationService.updateGoodsEvaluation(goodsEvaluation));
    }

    /**
     * 删除商品评价
     */
    @PreAuthorize("@ss.hasPermi('goods:evaluation:remove')")
    @Log(title = "商品评价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{goodsEvaluationIds}")
    public AjaxResult remove(@PathVariable Long[] goodsEvaluationIds)
    {
        return toAjax(goodsEvaluationService.deleteGoodsEvaluationByGoodsEvaluationIds(goodsEvaluationIds));
    }
}
