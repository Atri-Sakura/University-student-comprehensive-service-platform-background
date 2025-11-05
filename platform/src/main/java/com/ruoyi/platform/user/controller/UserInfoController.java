package com.ruoyi.platform.user.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.service.IUserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 个人信息用户视角接口
 * 仅允许用户访问、修改自己的个人信息
 *
 * @author ruoyi
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Autowired
    private IUserBaseService userBaseService;

    /**
     * 查询当前用户个人信息
     * 当前用户只能查自己的信息
     */
    @GetMapping
    public AjaxResult getMyInfo() {
        Long userBaseId = SecurityUtils.getUserBaseId();
        UserBase userBase = userBaseService.selectUserBaseByUserBaseId(userBaseId);
        if (userBase == null) {
            return AjaxResult.error("未找到当前用户信息");
        }
        return AjaxResult.success(userBase);
    }

    /**
     * 修改当前用户个人信息
     * 仅允许用户修改自己的信息
     */
    @PutMapping
    public AjaxResult updateMyInfo(@RequestBody UserBase userBase) {
        Long userBaseId = SecurityUtils.getUserBaseId();
        // 防止越权：只能修改自己的信息
        userBase.setUserBaseId(userBaseId);
        int rows = userBaseService.updateUserBase(userBase);
        if (rows > 0) {
            return AjaxResult.success("修改成功");
        } else {
            return AjaxResult.error("修改失败");
        }
    }
}