package com.ruoyi.platform.controller1.user;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.UserCreditScoreRecord;
import com.ruoyi.platform.service.IUserCreditScoreRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信用分流水Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/user/credit/record")
public class UserCreditScoreRecordController {

    @Autowired
    private IUserCreditScoreRecordService userCreditScoreRecordService;

    /**
     * 查询用户信用分流水详情
     */
    @GetMapping("/{id}")
    public R<UserCreditScoreRecord> getCreditRecord(@PathVariable("id") Long id) {
        UserCreditScoreRecord record = userCreditScoreRecordService.selectUserCreditScoreRecordById(id);
        return record != null ? R.ok(record) : R.fail("信用分流水不存在");
    }

    /**
     * 查询用户信用分流水列表
     */
    @GetMapping("/list")
    public R<List<UserCreditScoreRecord>> list(UserCreditScoreRecord userCreditScoreRecord) {
        List<UserCreditScoreRecord> list = userCreditScoreRecordService.selectUserCreditScoreRecordList(userCreditScoreRecord);
        return R.ok(list);
    }

    /**
     * 新增用户信用分流水
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserCreditScoreRecord userCreditScoreRecord) {
        int result = userCreditScoreRecordService.insertUserCreditScoreRecord(userCreditScoreRecord);
        return result > 0 ? R.ok(result, "新增信用分流水成功") : R.fail("新增信用分流水失败");
    }

    /**
     * 修改用户信用分流水
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserCreditScoreRecord userCreditScoreRecord) {
        int result = userCreditScoreRecordService.updateUserCreditScoreRecord(userCreditScoreRecord);
        return result > 0 ? R.ok(result, "修改信用分流水成功") : R.fail("修改信用分流水失败");
    }

    /**
     * 批量删除用户信用分流水
     */
    @DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids) {
        int result = userCreditScoreRecordService.deleteUserCreditScoreRecordByIds(ids);
        return result > 0 ? R.ok(result, "删除信用分流水成功") : R.fail("删除信用分流水失败");
    }
}