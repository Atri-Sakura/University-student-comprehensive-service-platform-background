package com.ruoyi.platform.user.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.UserWallet;
import com.ruoyi.platform.user.service.ISysUserWalletRecordService;
import com.ruoyi.platform.user.vo.UserWalletRecordVO;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/walletRecord")
@Slf4j
public class UserWalletRecord1Controller {
    // TODO: 实现用户钱包流水相关功能
    @Autowired
    private ISysUserWalletRecordService sysUserWalletRecordService;

    @GetMapping("getUserWalletBalance")
    public AjaxResult getUserWalletBalance(){
        Long userId = SecurityUtils.getUserBaseId();
        if (userId == null) {
            return AjaxResult.error("用户未登录");
        }
        UserWallet userWallet = sysUserWalletRecordService.getUserWalletBalance(userId);
        return userWallet != null ? AjaxResult.success("查询成功",userWallet) : AjaxResult.error("查询失败");
    }

    @GetMapping("/getUserWalletRecord")
    public AjaxResult getUserWalletRecord(){
        Long userId = SecurityUtils.getUserBaseId();
        if (userId == null) {
            return AjaxResult.error("用户未登录");
        }
//        log.info(String.valueOf(userId));
        List<UserWalletRecordVO> result = sysUserWalletRecordService.getUserWalletRecord(userId);
        return result != null ? AjaxResult.success("查询成功",result): AjaxResult.error("查询失败",result);
    }

    @PostMapping("/addWallet")
    public AjaxResult addWallet(){
        Long userId = SecurityUtils.getUserBaseId();
        if (userId == null) {
            return AjaxResult.error("用户未登录");
        }
        int rows = sysUserWalletRecordService.addWallet(userId);
        return AjaxResult.success("添加成功");
    }
}
