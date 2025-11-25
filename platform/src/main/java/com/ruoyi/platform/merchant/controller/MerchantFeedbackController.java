package com.ruoyi.platform.merchant.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.platform.domain.UserFeedback;
import com.ruoyi.platform.service.IUserFeedbackService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商家反馈Controller
 *
 * @author ruoyi
 * @date 2025-11-25
 */
@RestController
@RequestMapping("/merchant/feedback")
public class MerchantFeedbackController extends BaseController
{
    @Autowired
    private IUserFeedbackService userFeedbackService;

    /**
     * 查询我的反馈列表
     * 商家只能查询自己的反馈
     */
    @GetMapping("/list")
    public TableDataInfo list(UserFeedback userFeedback)
    {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        // 强制设置为当前商家，防止越权
        userFeedback.setUserType(3); // 商家类型
        userFeedback.setUserId(merchantBaseId);
        startPage();
        List<UserFeedback> list = userFeedbackService.selectUserFeedbackList(userFeedback);
        return getDataTable(list);
    }

    /**
     * 导出我的反馈列表
     */
    @Log(title = "商家反馈", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserFeedback userFeedback)
    {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        userFeedback.setUserType(3);
        userFeedback.setUserId(merchantBaseId);
        List<UserFeedback> list = userFeedbackService.selectUserFeedbackList(userFeedback);
        ExcelUtil<UserFeedback> util = new ExcelUtil<UserFeedback>(UserFeedback.class);
        util.exportExcel(response, list, "商家反馈数据");
    }

    /**
     * 获取反馈详细信息
     * 只能查看自己的反馈详情
     */
    @GetMapping(value = "/{feedbackId}")
    public AjaxResult getInfo(@PathVariable("feedbackId") Long feedbackId)
    {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        UserFeedback feedback = userFeedbackService.selectUserFeedbackById(feedbackId);

        if (feedback == null) {
            return AjaxResult.error("反馈不存在");
        }

        // 验证是否是自己的反馈
        if (!feedback.getUserId().equals(merchantBaseId) || feedback.getUserType() != 3) {
            return AjaxResult.error("无权访问该反馈");
        }

        return success(feedback);
    }

    /**
     * 新增反馈
     */
    @Log(title = "商家反馈", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserFeedback userFeedback)
    {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        String username = SecurityUtils.getUsername();

        // 强制设置当前商家信息，防止伪造
        userFeedback.setUserType(3);
        userFeedback.setUserId(merchantBaseId);
        userFeedback.setUserNickname(username);

        return toAjax(userFeedbackService.insertUserFeedback(userFeedback));
    }

    /**
     * 修改反馈
     * 只能修改自己的反馈
     */
    @Log(title = "商家反馈", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserFeedback userFeedback)
    {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();

        // 先查询原反馈，验证权限
        UserFeedback existingFeedback = userFeedbackService.selectUserFeedbackById(userFeedback.getFeedbackId());
        if (existingFeedback == null) {
            return AjaxResult.error("反馈不存在");
        }

        // 验证是否是自己的反馈
        if (!existingFeedback.getUserId().equals(merchantBaseId) || existingFeedback.getUserType() != 3) {
            return AjaxResult.error("无权修改该反馈");
        }

        // 不允许修改用户类型和用户ID
        userFeedback.setUserType(null);
        userFeedback.setUserId(null);
        userFeedback.setUserNickname(null);

        return toAjax(userFeedbackService.updateUserFeedback(userFeedback));
    }

    /**
     * 删除反馈
     * 只能删除自己的反馈
     */
    @Log(title = "商家反馈", businessType = BusinessType.DELETE)
    @DeleteMapping("/{feedbackIds}")
    public AjaxResult remove(@PathVariable Long[] feedbackIds)
    {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();

        // 验证每个反馈是否属于当前商家
        for (Long feedbackId : feedbackIds) {
            UserFeedback feedback = userFeedbackService.selectUserFeedbackById(feedbackId);
            if (feedback == null) {
                return AjaxResult.error("反馈ID:" + feedbackId + " 不存在");
            }
            if (!feedback.getUserId().equals(merchantBaseId) || feedback.getUserType() != 3) {
                return AjaxResult.error("无权删除反馈ID:" + feedbackId);
            }
        }

        return toAjax(userFeedbackService.deleteUserFeedbackByIds(feedbackIds));
    }
}