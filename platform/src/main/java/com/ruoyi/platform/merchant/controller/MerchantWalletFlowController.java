package com. ruoyi.platform.merchant.controller;

import java.util.List;
import java.util.Map;

import com.ruoyi.platform.merchant.service.IMerchantWalletFlowService;
import jakarta.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web. bind.annotation.PostMapping;
import org.springframework. web.bind.annotation.PathVariable;
import org.springframework. web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com. ruoyi.common.core. domain.AjaxResult;
import com.ruoyi.common. enums.BusinessType;
import com.ruoyi.platform.domain.MerchantWalletFlow;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商家钱包流水Controller
 * 提供商家端钱包流水查询、导出、汇总功能
 *
 * @author ruoyi
 * @date 2025-12-09
 */
@RestController
@RequestMapping("/merchant/walletFlow")
public class MerchantWalletFlowController extends BaseController
{
    @Autowired
    private IMerchantWalletFlowService merchantWalletFlowService;

    /**
     * 查询当前商家的钱包流水列表
     * 商家只能查看自己的流水数据
     *
     * @param merchantWalletFlow 查询条件
     * @return 流水列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MerchantWalletFlow merchantWalletFlow)
    {
        // 从Security上下文获取当前登录商家ID
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        if (merchantBaseId == null) {
            return getDataTable(List.of());
        }

        // 强制设置商家ID，防止查询其他商家数据
        merchantWalletFlow.setMerchantBaseId(merchantBaseId);

        startPage();
        List<MerchantWalletFlow> list = merchantWalletFlowService.selectMerchantWalletFlowList(merchantWalletFlow);
        return getDataTable(list);
    }

    /**
     * 获取当前商家钱包流水详细信息
     *
     * @param flowId 流水ID
     * @return 流水详情
     */
    @GetMapping("/{flowId}")
    public AjaxResult getInfo(@PathVariable("flowId") Long flowId)
    {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        if (merchantBaseId == null) {
            return AjaxResult.error("获取商家信息失败");
        }

        MerchantWalletFlow flow = merchantWalletFlowService.selectMerchantWalletFlowById(flowId);

        // 验证流水记录是否属于当前商家
        if (flow == null) {
            return AjaxResult.error("未找到该流水记录");
        }

        if (!merchantBaseId.equals(flow.getMerchantBaseId())) {
            return AjaxResult. error("无权查看该流水记录");
        }

        return AjaxResult.success(flow);
    }

    /**
     * 查询当前商家钱包流水汇总信息
     * 包含总收入、总提现、总退款等统计数据
     *
     * @return 汇总信息
     */
    @GetMapping("/summary")
    public AjaxResult getSummary()
    {
        Long merchantBaseId = SecurityUtils. getMerchantBaseId();
        if (merchantBaseId == null) {
            return AjaxResult.error("获取商家信息失败");
        }

        Map<String, Object> summary = merchantWalletFlowService.getFlowSummary(merchantBaseId);
        return AjaxResult.success(summary);
    }

    /**
     * 导出当前商家钱包流水列表
     *
     * @param response HTTP响应
     * @param merchantWalletFlow 查询条件
     */
    @Log(title = "商家钱包流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MerchantWalletFlow merchantWalletFlow)
    {
        // 从Security上下文获取当前登录商家ID
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        if (merchantBaseId == null) {
            return;
        }

        // 强制设置商家ID，只导出自己的数据
        merchantWalletFlow.setMerchantBaseId(merchantBaseId);

        List<MerchantWalletFlow> list = merchantWalletFlowService.selectMerchantWalletFlowList(merchantWalletFlow);
        ExcelUtil<MerchantWalletFlow> util = new ExcelUtil<>(MerchantWalletFlow.class);
        util.exportExcel(response, list, "商家钱包流水数据");
    }

    /**
     * 按流水类型查询统计
     * 可用于图表展示（如收入趋势、提现记录等）
     *
     * @param flowType 流水类型 (INCOME/WITHDRAW_SUCCESS/REFUND等)
     * @return 流水列表
     */
    @GetMapping("/type/{flowType}")
    public TableDataInfo listByType(@PathVariable("flowType") String flowType)
    {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        if (merchantBaseId == null) {
            return getDataTable(List.of());
        }

        MerchantWalletFlow query = new MerchantWalletFlow();
        query.setMerchantBaseId(merchantBaseId);
        query.setFlowType(flowType);

        startPage();
        List<MerchantWalletFlow> list = merchantWalletFlowService.selectMerchantWalletFlowList(query);
        return getDataTable(list);
    }
}