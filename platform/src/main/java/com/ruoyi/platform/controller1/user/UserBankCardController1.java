package com.ruoyi.platform.controller1.user;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.platform.domain.UserBankCard;
import com.ruoyi.platform.service.IUserBankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户银行卡绑定Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/user/bankcard")
public class UserBankCardController1 {

    @Autowired
    private IUserBankCardService userBankCardService;

    /**
     * 查询用户银行卡绑定详情
     */
    @GetMapping("/{id}")
    public R<UserBankCard> getBankCard(@PathVariable("id") Long id) {
        UserBankCard bankCard = userBankCardService.selectUserBankCardById(id);
        return bankCard != null ? R.ok(bankCard) : R.fail("银行卡绑定记录不存在");
    }

    /**
     * 查询用户银行卡绑定列表
     */
    @GetMapping("/list")
    public R<List<UserBankCard>> list(UserBankCard userBankCard) {
        List<UserBankCard> list = userBankCardService.selectUserBankCardList(userBankCard);
        return R.ok(list);
    }

    /**
     * 新增用户银行卡绑定
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserBankCard userBankCard) {
        int result = userBankCardService.insertUserBankCard(userBankCard);
        return result > 0 ? R.ok(result, "新增银行卡绑定成功") : R.fail("新增银行卡绑定失败");
    }

    /**
     * 修改用户银行卡绑定
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserBankCard userBankCard) {
        int result = userBankCardService.updateUserBankCard(userBankCard);
        return result > 0 ? R.ok(result, "修改银行卡绑定成功") : R.fail("修改银行卡绑定失败");
    }

    /**
     * 批量删除用户银行卡绑定
     */
    @DeleteMapping("/{ids}")
    public R<Integer> remove(@PathVariable Long[] ids) {
        int result = userBankCardService.deleteUserBankCardByIds(ids);
        return result > 0 ? R.ok(result, "删除银行卡绑定成功") : R.fail("删除银行卡绑定失败");
    }
}