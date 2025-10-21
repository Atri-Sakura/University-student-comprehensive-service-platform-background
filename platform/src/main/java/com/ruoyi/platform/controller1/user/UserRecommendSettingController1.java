package com.ruoyi.platform.controller1.user;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.UserRecommendSetting;
import com.ruoyi.platform.service.IUserRecommendSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户个性化推荐设置Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/recommend/setting")
public class UserRecommendSettingController1 {

    @Autowired
    private IUserRecommendSettingService userRecommendSettingService;

    /**
     * 查询用户个性化推荐设置详情
     */
    @GetMapping("/{userRecommendSettingId}")
    public R<UserRecommendSetting> getRecommendSetting(
            @PathVariable("userRecommendSettingId") Long userRecommendSettingId) {
        UserRecommendSetting setting = userRecommendSettingService.selectUserRecommendSettingByUserRecommendSettingId(userRecommendSettingId);
        return setting != null ? R.ok(setting) : R.fail("个性化推荐设置不存在");
    }

    /**
     * 查询用户个性化推荐设置列表
     */
    @GetMapping("/list")
    public R<List<UserRecommendSetting>> list(UserRecommendSetting userRecommendSetting) {
        List<UserRecommendSetting> list = userRecommendSettingService.selectUserRecommendSettingList(userRecommendSetting);
        return R.ok(list);
    }

    /**
     * 新增用户个性化推荐设置
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserRecommendSetting userRecommendSetting) {
        int result = userRecommendSettingService.insertUserRecommendSetting(userRecommendSetting);
        return result > 0 ? R.ok(result, "新增推荐设置成功") : R.fail("新增推荐设置失败");
    }

    /**
     * 修改用户个性化推荐设置
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserRecommendSetting userRecommendSetting) {
        int result = userRecommendSettingService.updateUserRecommendSetting(userRecommendSetting);
        return result > 0 ? R.ok(result, "修改推荐设置成功") : R.fail("修改推荐设置失败");
    }

    /**
     * 批量删除用户个性化推荐设置
     */
    @DeleteMapping("/{userRecommendSettingIds}")
    public R<Integer> remove(@PathVariable Long[] userRecommendSettingIds) {
        int result = userRecommendSettingService.deleteUserRecommendSettingByUserRecommendSettingIds(userRecommendSettingIds);
        return result > 0 ? R.ok(result, "删除推荐设置成功") : R.fail("删除推荐设置失败");
    }
}