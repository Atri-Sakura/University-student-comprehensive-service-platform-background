package com.ruoyi.platform.controller1.user;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.UserPreferenceTag;
import com.ruoyi.platform.service.IUserPreferenceTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户偏好标签Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/user/preference/tag")
public class UserPreferenceTagController {

    @Autowired
    private IUserPreferenceTagService userPreferenceTagService;

    /**
     * 查询用户偏好标签详情
     */
    @GetMapping("/{userPreferenceTagId}")
    public R<UserPreferenceTag> getPreferenceTag(
            @PathVariable("userPreferenceTagId") Long userPreferenceTagId) {
        UserPreferenceTag tag = userPreferenceTagService.selectUserPreferenceTagByUserPreferenceTagId(userPreferenceTagId);
        return tag != null ? R.ok(tag) : R.fail("偏好标签不存在");
    }

    /**
     * 查询用户偏好标签列表
     */
    @GetMapping("/list")
    public R<List<UserPreferenceTag>> list(UserPreferenceTag userPreferenceTag) {
        List<UserPreferenceTag> list = userPreferenceTagService.selectUserPreferenceTagList(userPreferenceTag);
        return R.ok(list);
    }

    /**
     * 新增用户偏好标签
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserPreferenceTag userPreferenceTag) {
        int result = userPreferenceTagService.insertUserPreferenceTag(userPreferenceTag);
        return result > 0 ? R.ok(result, "新增偏好标签成功") : R.fail("新增偏好标签失败");
    }

    /**
     * 修改用户偏好标签
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserPreferenceTag userPreferenceTag) {
        int result = userPreferenceTagService.updateUserPreferenceTag(userPreferenceTag);
        return result > 0 ? R.ok(result, "修改偏好标签成功") : R.fail("修改偏好标签失败");
    }

    /**
     * 批量删除用户偏好标签
     */
    @DeleteMapping("/{userPreferenceTagIds}")
    public R<Integer> remove(@PathVariable Long[] userPreferenceTagIds) {
        int result = userPreferenceTagService.deleteUserPreferenceTagByUserPreferenceTagIds(userPreferenceTagIds);
        return result > 0 ? R.ok(result, "删除偏好标签成功") : R.fail("删除偏好标签失败");
    }
}