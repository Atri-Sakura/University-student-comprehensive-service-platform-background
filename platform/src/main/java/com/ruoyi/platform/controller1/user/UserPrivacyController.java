package com.ruoyi.platform.controller1.user;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.UserPrivacy;
import com.ruoyi.platform.service.IUserPrivacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户隐私设置Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/user/privacy")
public class UserPrivacyController {

    @Autowired
    private IUserPrivacyService userPrivacyService;

    /**
     * 查询用户隐私设置详情
     */
    @GetMapping("/{userPrivacyId}")
    public R<UserPrivacy> getPrivacySetting(@PathVariable("userPrivacyId") Long userPrivacyId) {
        UserPrivacy privacy = userPrivacyService.selectUserPrivacyByUserPrivacyId(userPrivacyId);
        return privacy != null ? R.ok(privacy) : R.fail("隐私设置不存在");
    }

    /**
     * 查询用户隐私设置列表
     */
    @GetMapping("/list")
    public R<List<UserPrivacy>> list(UserPrivacy userPrivacy) {
        List<UserPrivacy> list = userPrivacyService.selectUserPrivacyList(userPrivacy);
        return R.ok(list);
    }

    /**
     * 新增用户隐私设置
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserPrivacy userPrivacy) {
        int result = userPrivacyService.insertUserPrivacy(userPrivacy);
        return result > 0 ? R.ok(result, "新增隐私设置成功") : R.fail("新增隐私设置失败");
    }

    /**
     * 修改用户隐私设置
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserPrivacy userPrivacy) {
        int result = userPrivacyService.updateUserPrivacy(userPrivacy);
        return result > 0 ? R.ok(result, "修改隐私设置成功") : R.fail("修改隐私设置失败");
    }

    /**
     * 批量删除用户隐私设置
     */
    @DeleteMapping("/{userPrivacyIds}")
    public R<Integer> remove(@PathVariable Long[] userPrivacyIds) {
        int result = userPrivacyService.deleteUserPrivacyByUserPrivacyIds(userPrivacyIds);
        return result > 0 ? R.ok(result, "删除隐私设置成功") : R.fail("删除隐私设置失败");
    }
}