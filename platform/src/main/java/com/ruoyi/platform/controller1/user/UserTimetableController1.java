package com.ruoyi.platform.controller1.user;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.UserTimetable;
import com.ruoyi.platform.service.IUserTimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 个人课Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/timetable")
public class UserTimetableController1 {

    @Autowired
    private IUserTimetableService userTimetableService;

    /**
     * 查询个人课详情
     */
    @GetMapping("/{userTimetableId}")
    public R<UserTimetable> getUserTimetable(@PathVariable("userTimetableId") Long userTimetableId) {
        UserTimetable timetable = userTimetableService.selectUserTimetableByUserTimetableId(userTimetableId);
        return timetable != null ? R.ok(timetable) : R.fail("个人课不存在");
    }

    /**
     * 查询个人课列表
     */
    @GetMapping("/list")
    public R<List<UserTimetable>> list(UserTimetable userTimetable) {
        List<UserTimetable> list = userTimetableService.selectUserTimetableList(userTimetable);
        return R.ok(list);
    }

    /**
     * 新增个人课
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserTimetable userTimetable) {
        int result = userTimetableService.insertUserTimetable(userTimetable);
        return result > 0 ? R.ok(result, "新增成功") : R.fail("新增失败");
    }

    /**
     * 修改个人课
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserTimetable userTimetable) {
        int result = userTimetableService.updateUserTimetable(userTimetable);
        return result > 0 ? R.ok(result, "修改成功") : R.fail("修改失败");
    }

    /**
     * 批量删除个人课
     */
    @DeleteMapping("/{userTimetableIds}")
    public R<Integer> remove(@PathVariable Long[] userTimetableIds) {
        int result = userTimetableService.deleteUserTimetableByUserTimetableIds(userTimetableIds);
        return result > 0 ? R.ok(result, "删除成功") : R.fail("删除失败");
    }
}