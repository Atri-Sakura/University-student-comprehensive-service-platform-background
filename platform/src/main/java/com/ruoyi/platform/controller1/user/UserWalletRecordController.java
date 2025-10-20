package com.ruoyi.platform.controller1.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.platform.domain.UserWalletRecord;
import com.ruoyi.platform.service.IUserWalletRecordService;
import com.ruoyi.common.core.domain.R;

/**
 * 用户钱包流水Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/wallet/record")
public class UserWalletRecordController {

    @Autowired
    private IUserWalletRecordService userWalletRecordService;

    /**
     * 查询用户钱包流水详情
     */
    @GetMapping("/{userWalletRecordId}")
    public R<UserWalletRecord> getUserWalletRecord(@PathVariable("userWalletRecordId") Long userWalletRecordId) {
        UserWalletRecord record = userWalletRecordService.selectUserWalletRecordByUserWalletRecordId(userWalletRecordId);
        return record != null ? R.ok(record) : R.fail("用户钱包流水不存在");
    }

    /**
     * 查询用户钱包流水列表
     */
    @GetMapping("/list")
    public R<List<UserWalletRecord>> list(UserWalletRecord userWalletRecord) {
        List<UserWalletRecord> list = userWalletRecordService.selectUserWalletRecordList(userWalletRecord);
        return R.ok(list);
    }

    /**
     * 新增用户钱包流水
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserWalletRecord userWalletRecord) {
        int result = userWalletRecordService.insertUserWalletRecord(userWalletRecord);
        return result > 0 ? R.ok(result, "新增成功") : R.fail("新增失败");
    }

    /**
     * 修改用户钱包流水
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserWalletRecord userWalletRecord) {
        int result = userWalletRecordService.updateUserWalletRecord(userWalletRecord);
        return result > 0 ? R.ok(result, "修改成功") : R.fail("修改失败");
    }

    /**
     * 删除用户钱包流水
     */
    @DeleteMapping("/{userWalletRecordIds}")
    public R<Integer> remove(@PathVariable Long[] userWalletRecordIds) {
        int result = userWalletRecordService.deleteUserWalletRecordByUserWalletRecordIds(userWalletRecordIds);
        return result > 0 ? R.ok(result, "删除成功") : R.fail("删除失败");
    }
}