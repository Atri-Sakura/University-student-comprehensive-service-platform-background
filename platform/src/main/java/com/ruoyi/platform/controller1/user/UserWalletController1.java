package com.ruoyi.platform.controller1.user;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.UserWallet;
import com.ruoyi.platform.service.IUserWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户钱包Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/wallet")
public class UserWalletController1 {

    @Autowired
    private IUserWalletService userWalletService;

    /**
     * 查询用户钱包详情
     */
    @GetMapping("/{userWalletId}")
    public R<UserWallet> getUserWallet(@PathVariable("userWalletId") Long userWalletId) {
        UserWallet wallet = userWalletService.selectUserWalletByUserWalletId(userWalletId);
        return wallet != null ? R.ok(wallet) : R.fail("用户钱包不存在");
    }

    /**
     * 查询用户钱包列表
     */
    @GetMapping("/list")
    public R<List<UserWallet>> list(UserWallet userWallet) {
        List<UserWallet> list = userWalletService.selectUserWalletList(userWallet);
        return R.ok(list);
    }

    /**
     * 新增用户钱包
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserWallet userWallet) {
        int result = userWalletService.insertUserWallet(userWallet);
        return result > 0 ? R.ok(result, "新增成功") : R.fail("新增失败");
    }

    /**
     * 修改用户钱包
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserWallet userWallet) {
        int result = userWalletService.updateUserWallet(userWallet);
        return result > 0 ? R.ok(result, "修改成功") : R.fail("修改失败");
    }

    /**
     * 删除用户钱包
     */
    @DeleteMapping("/{userWalletIds}")
    public R<Integer> remove(@PathVariable Long[] userWalletIds) {
        int result = userWalletService.deleteUserWalletByUserWalletIds(userWalletIds);
        return result > 0 ? R.ok(result, "删除成功") : R.fail("删除失败");
    }
}