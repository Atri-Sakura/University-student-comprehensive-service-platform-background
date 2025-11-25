package com.ruoyi.platform.rider.controller;

import java.util.List;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.platform.domain.UserFeedback;
import com.ruoyi.platform.service.IUserFeedbackService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 骑手反馈Controller
 *
 * @author ruoyi
 * @date 2025-11-25
 */
@RestController
@RequestMapping("/rider/feedback")
public class RiderFeedbackController extends BaseController
{
    @Autowired
    private IUserFeedbackService userFeedbackService;

    /**
     * 查询我的反馈列表
     * 骑手只能查询自己的反馈
     */
    @GetMapping("/list")
    public TableDataInfo list(UserFeedback userFeedback)
    {
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        // 强制设置为当前骑手，防止越权
        userFeedback.setUserType(2); // 骑手类型
        userFeedback.setUserId(riderBaseId);
        startPage();
        List<UserFeedback> list = userFeedbackService.selectUserFeedbackList(userFeedback);
        return getDataTable(list);
    }

    /**
     * 获取反馈详细信息
     * 只能查看自己的反馈详情
     */
    @GetMapping(value = "/{feedbackId}")
    public AjaxResult getInfo(@PathVariable("feedbackId") Long feedbackId)
    {
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        UserFeedback feedback = userFeedbackService.selectUserFeedbackById(feedbackId);

        if (feedback == null) {
            return AjaxResult.error("反馈不存在");
        }

        // 验证是否是自己的反馈
        if (!feedback.getUserId().equals(riderBaseId) || feedback.getUserType() != 2) {
            return AjaxResult.error("无权访问该反馈");
        }

        return success(feedback);
    }

    /**
     * 新增反馈
     */
    @Log(title = "骑手反馈", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserFeedback userFeedback)
    {
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        String username = SecurityUtils.getUsername();

        // 强制设置当前骑手信息，防止伪造
        userFeedback.setUserType(2);
        userFeedback.setUserId(riderBaseId);
        userFeedback.setUserNickname(username);

        return toAjax(userFeedbackService.insertUserFeedback(userFeedback));
    }

    /**
     * 修改反馈
     * 只能修改自己的反馈
     */
    @Log(title = "骑手反馈", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserFeedback userFeedback)
    {
        Long riderBaseId = SecurityUtils.getRiderBaseId();

        // 先查询原反馈，验证权限
        UserFeedback existingFeedback = userFeedbackService.selectUserFeedbackById(userFeedback.getFeedbackId());
        if (existingFeedback == null) {
            return AjaxResult.error("反馈不存在");
        }

        // 验证是否是自己的反馈
        if (!existingFeedback.getUserId().equals(riderBaseId) || existingFeedback.getUserType() != 2) {
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
    @Log(title = "骑手反馈", businessType = BusinessType.DELETE)
    @DeleteMapping("/{feedbackIds}")
    public AjaxResult remove(@PathVariable Long[] feedbackIds)
    {
        Long riderBaseId = SecurityUtils.getRiderBaseId();

        // 验证每个反馈是否属于当前骑手
        for (Long feedbackId : feedbackIds) {
            UserFeedback feedback = userFeedbackService.selectUserFeedbackById(feedbackId);
            if (feedback == null) {
                return AjaxResult.error("反馈ID:" + feedbackId + " 不存在");
            }
            if (!feedback.getUserId().equals(riderBaseId) || feedback.getUserType() != 2) {
                return AjaxResult.error("无权删除反馈ID:" + feedbackId);
            }
        }

        return toAjax(userFeedbackService.deleteUserFeedbackByIds(feedbackIds));
    }
}