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
import com.ruoyi.platform.domain.SecondhandGoods;
import com.ruoyi.platform.service.ISecondhandGoodsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 二手商品(简化版)Controller
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@RestController
@RequestMapping("/secondhand/goods")
public class SecondhandGoodsController extends BaseController
{
    @Autowired
    private ISecondhandGoodsService secondhandGoodsService;

    /**
     * 查询二手商品(简化版)列表
     */
    @PreAuthorize("@ss.hasPermi('secondhand:goods:list')")
    @GetMapping("/list")
    public TableDataInfo list(SecondhandGoods secondhandGoods)
    {
        startPage();
        List<SecondhandGoods> list = secondhandGoodsService.selectSecondhandGoodsList(secondhandGoods);
        return getDataTable(list);
    }

    /**
     * 导出二手商品(简化版)列表
     */
    @PreAuthorize("@ss.hasPermi('secondhand:goods:export')")
    @Log(title = "二手商品(简化版)", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SecondhandGoods secondhandGoods)
    {
        List<SecondhandGoods> list = secondhandGoodsService.selectSecondhandGoodsList(secondhandGoods);
        ExcelUtil<SecondhandGoods> util = new ExcelUtil<SecondhandGoods>(SecondhandGoods.class);
        util.exportExcel(response, list, "二手商品(简化版)数据");
    }

    /**
     * 获取二手商品(简化版)详细信息
     */
    @PreAuthorize("@ss.hasPermi('secondhand:goods:query')")
    @GetMapping(value = "/{secondhandGoodsId}")
    public AjaxResult getInfo(@PathVariable("secondhandGoodsId") Long secondhandGoodsId)
    {
        return success(secondhandGoodsService.selectSecondhandGoodsBySecondhandGoodsId(secondhandGoodsId));
    }

    /**
     * 新增二手商品(简化版)
     */
    @PreAuthorize("@ss.hasPermi('secondhand:goods:add')")
    @Log(title = "二手商品(简化版)", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SecondhandGoods secondhandGoods)
    {
        return toAjax(secondhandGoodsService.insertSecondhandGoods(secondhandGoods));
    }

    /**
     * 修改二手商品(简化版)
     */
    @PreAuthorize("@ss.hasPermi('secondhand:goods:edit')")
    @Log(title = "二手商品(简化版)", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SecondhandGoods secondhandGoods)
    {
        return toAjax(secondhandGoodsService.updateSecondhandGoods(secondhandGoods));
    }

    /**
     * 删除二手商品(简化版)
     */
    @PreAuthorize("@ss.hasPermi('secondhand:goods:remove')")
    @Log(title = "二手商品(简化版)", businessType = BusinessType.DELETE)
	@DeleteMapping("/{secondhandGoodsIds}")
    public AjaxResult remove(@PathVariable Long[] secondhandGoodsIds)
    {
        return toAjax(secondhandGoodsService.deleteSecondhandGoodsBySecondhandGoodsIds(secondhandGoodsIds));
    }
}
