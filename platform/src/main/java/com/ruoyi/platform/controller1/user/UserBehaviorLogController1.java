package com.ruoyi.platform.controller1.user;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.UserBehaviorLog;
import com.ruoyi.platform.service.IUserBehaviorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户行为记录Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/user/behavior/log")
public class UserBehaviorLogController1 {

    @Autowired
    private IUserBehaviorLogService userBehaviorLogService;

    /**
     * 查询用户行为记录详情
     */
    @GetMapping("/{userBehaviorLogId}")
    public R<UserBehaviorLog> getBehaviorLog(
            @PathVariable("userBehaviorLogId") Long userBehaviorLogId) {
        UserBehaviorLog log = userBehaviorLogService.selectUserBehaviorLogByUserBehaviorLogId(userBehaviorLogId);
        return log != null ? R.ok(log) : R.fail("用户行为记录不存在");
    }

    /**
     * 查询用户行为记录列表
     */
    @GetMapping("/list")
    public R<List<UserBehaviorLog>> list(UserBehaviorLog userBehaviorLog) {
        List<UserBehaviorLog> list = userBehaviorLogService.selectUserBehaviorLogList(userBehaviorLog);
        return R.ok(list);
    }

    /**
     * 新增用户行为记录
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserBehaviorLog userBehaviorLog) {
        int result = userBehaviorLogService.insertUserBehaviorLog(userBehaviorLog);
        return result > 0 ? R.ok(result, "新增行为记录成功") : R.fail("新增行为记录失败");
    }

    /**
     * 修改用户行为记录
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserBehaviorLog userBehaviorLog) {
        int result = userBehaviorLogService.updateUserBehaviorLog(userBehaviorLog);
        return result > 0 ? R.ok(result, "修改行为记录成功") : R.fail("修改行为记录失败");
    }

    /**
     * 批量删除用户行为记录
     */
    @DeleteMapping("/{userBehaviorLogIds}")
    public R<Integer> remove(@PathVariable Long[] userBehaviorLogIds) {
        int result = userBehaviorLogService.deleteUserBehaviorLogByUserBehaviorLogIds(userBehaviorLogIds);
        return result > 0 ? R.ok(result, "删除行为记录成功") : R.fail("删除行为记录失败");
    }
}