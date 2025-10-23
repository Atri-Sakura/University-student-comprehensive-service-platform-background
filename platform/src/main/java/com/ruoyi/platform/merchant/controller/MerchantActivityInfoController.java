package com.ruoyi.platform.merchant.controller;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.platform.domain.MerchantActivity;
import com.ruoyi.platform.service.IMerchantActivityService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 商家活动管理 Controller
 */
@RestController
@RequestMapping("/merchant/Activity")
public class MerchantActivityInfoController {

    @Autowired
    private IMerchantActivityService merchantActivityService;

    /**
     * 查询商家活动列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MerchantActivity merchantActivity) {
        // 用当前登录商家ID
        merchantActivity.setMerchantBaseId(SecurityUtils.getMerchantBaseId());
        List<MerchantActivity> list = merchantActivityService.selectMerchantActivityList(merchantActivity);
        return new TableDataInfo(list, list.size());
    }

    /**
     * 获取商家活动详细信息
     */
    @GetMapping("/{merchantActivityId}")
    public AjaxResult getInfo(@PathVariable Long merchantActivityId) {
        MerchantActivity activity = merchantActivityService.selectMerchantActivityByMerchantActivityId(merchantActivityId);
        // 检查活动是否属于本商家
        if (activity == null || !activity.getMerchantBaseId().equals(SecurityUtils.getMerchantBaseId())) {
            return AjaxResult.error("无权访问该活动");
        }
        return AjaxResult.success(activity);
    }

    /**
     * 新增商家活动
     */
    @PostMapping
    public AjaxResult add(@RequestBody MerchantActivity merchantActivity) {
        merchantActivity.setMerchantBaseId(SecurityUtils.getMerchantBaseId());
        int result = merchantActivityService.insertMerchantActivity(merchantActivity);
        return AjaxResult.success(result);
    }

    /**
     * 修改商家活动
     */
    @PutMapping
    public AjaxResult edit(@RequestBody MerchantActivity merchantActivity) {
        // 查询原活动，校验商家ID
        MerchantActivity oldActivity = merchantActivityService.selectMerchantActivityByMerchantActivityId(merchantActivity.getMerchantActivityId());
        if (oldActivity == null || !oldActivity.getMerchantBaseId().equals(SecurityUtils.getMerchantBaseId())) {
            return AjaxResult.error("无权修改该活动");
        }
        merchantActivity.setMerchantBaseId(SecurityUtils.getMerchantBaseId());
        int result = merchantActivityService.updateMerchantActivity(merchantActivity);
        return AjaxResult.success(result);
    }

    /**
     * 删除商家活动
     */
    @DeleteMapping("/{merchantActivityIds}")
    public AjaxResult remove(@PathVariable Long[] merchantActivityIds) {
        for (Long id : merchantActivityIds) {
            MerchantActivity activity = merchantActivityService.selectMerchantActivityByMerchantActivityId(id);
            if (activity == null || !activity.getMerchantBaseId().equals(SecurityUtils.getMerchantBaseId())) {
                return AjaxResult.error("无权删除活动ID：" + id);
            }
        }
        int result = merchantActivityService.deleteMerchantActivityByMerchantActivityIds(merchantActivityIds);
        return AjaxResult.success(result);
    }
}