package com.ruoyi.platform.user.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.UserWallet;
import com.ruoyi.platform.user.service.ISysUserWalletRecordService;
import com.ruoyi.platform.user.vo.UserWalletRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/walletRecord")
@Slf4j
public class UserWalletRecord1Controller {
    // TODO: 实现用户钱包流水相关功能
    @Autowired
    private ISysUserWalletRecordService sysUserWalletRecordService;

    @GetMapping("/getUserWalletBalance")
    public AjaxResult getUserWalletBalance() {
        Long userId = SecurityUtils.getUserBaseId();
        if (userId == null) {
            return AjaxResult.error("用户未登录");
        }
        UserWallet userWallet = sysUserWalletRecordService.getUserWalletBalance(userId);
        return userWallet != null ? AjaxResult.success("查询成功", userWallet) : AjaxResult.error("查询失败");
    }

    /**
     * 获取用户钱包记录接口
     *
     * @return 返回AjaxResult对象，包含查询结果状态和数据
     */
    @GetMapping("/getUserWalletRecord")
    public AjaxResult getUserWalletRecord() {
        // 从安全工具类中获取当前登录用户的ID
        Long userId = SecurityUtils.getUserBaseId();
        // 判断用户是否登录，如果未登录则返回错误信息
        if (userId == null) {
            return AjaxResult.error("用户未登录");
        }
        // 注释掉的日志记录代码，用于记录用户ID信息
        //        log.info(String.valueOf(userId));
        // 调用服务层方法获取用户钱包记录
        List<UserWalletRecordVO> result = sysUserWalletRecordService.getUserWalletRecord(userId);
        return result != null ? AjaxResult.success("查询成功", result) : AjaxResult.error("查询失败", result);
    }

    /**
     * 添加钱包接口
     *
     * @return 返回操作结果
     */
    @PostMapping("/addWallet")
    public AjaxResult addWallet(@RequestParam Long userWalletId) {
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserBaseId();
        // 判断用户是否登录
        if (userId == null) {
            return AjaxResult.error("用户未登录");
        }
        // 调用服务层添加钱包记录
        int rows = sysUserWalletRecordService.addWallet(userId,userWalletId);
        // 返回操作成功结果
        return rows > 0 ? AjaxResult.success("添加成功") : AjaxResult.error("添加失败");
    }


    @PostMapping("/freezeWallet")
    public AjaxResult freezeWallet() {
        Long userId = SecurityUtils.getUserBaseId();
        if (userId == null) {
            return AjaxResult.error("用户未登录");
        }
        int rows = sysUserWalletRecordService.freezeWallet(userId);
        // 返回操作成功结果
        return rows > 0 ? AjaxResult.success("冻结成功") : AjaxResult.error("冻结失败");
    }

    @PostMapping("/unfreezeWallet")
    public AjaxResult unfreezeWallet() {
        Long userId = SecurityUtils.getUserBaseId();
        if (userId == null) {
            return AjaxResult.error("用户未登录");
        }
        int rows = sysUserWalletRecordService.unfreezeWallet(userId);
        // 返回操作成功结果
        return rows > 0 ? AjaxResult.success("解冻成功") : AjaxResult.error("解冻失败");
    }

    @PostMapping("/setPayPassword")
    public AjaxResult setPayPassword(@RequestParam String oldPayPassword,@RequestParam String newPayPassword) {
        Long userId = SecurityUtils.getUserBaseId();
        if (userId == null) {
            return AjaxResult.error("用户未登录");
        }
        int rows = sysUserWalletRecordService.setPayPassword(userId,oldPayPassword,newPayPassword);
        // 返回操作成功结果
        return rows > 0 ? AjaxResult.success("设置成功") : AjaxResult.error("设置失败");
    }





}
