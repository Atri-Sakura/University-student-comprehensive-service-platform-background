package com.ruoyi.platform.controller1.user;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.service.IUserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户基础信息Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/user/base")
public class UserBaseController1 extends BaseController {

    @Autowired
    private IUserBaseService userBaseService;

    /**
     * 查询用户基础信息详情
     */
    @GetMapping("/{userBaseId}")
    public R<UserBase> getUserBase(@PathVariable("userBaseId") Long userBaseId) {
        UserBase userBase = userBaseService.selectUserBaseByUserBaseId(userBaseId);
        return userBase != null ? R.ok(userBase) : R.fail("用户基础信息不存在");
    }

    /**
     * 查询用户基础信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserBase userBase) {
        startPage();
        List<UserBase> list = userBaseService.selectUserBaseList(userBase);
        return getDataTable(list);
    }

    /**
     * 新增用户基础信息
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserBase userBase) {
        int result = userBaseService.insertUserBase(userBase);
        return result > 0 ? R.ok(result, "新增用户基础信息成功") : R.fail("新增用户基础信息失败");
    }

    /**
     * 修改用户基础信息
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserBase userBase) {
        int result = userBaseService.updateUserBase(userBase);
        return result > 0 ? R.ok(result, "修改用户基础信息成功") : R.fail("修改用户基础信息失败");
    }

    /**
     * 批量删除用户基础信息
     */
    @DeleteMapping("/{userBaseIds}")
    public R<Integer> remove(@PathVariable Long[] userBaseIds) {
        int result = userBaseService.deleteUserBaseByUserBaseIds(userBaseIds);
        return result > 0 ? R.ok(result, "删除用户基础信息成功") : R.fail("删除用户基础信息失败");
    }


    /**
     * 用户头像更新
     */
    @PostMapping("/avatar/upload")
    public R<String> uploadAvatar(@RequestParam("file") MultipartFile file, Long userBaseId) {
        return R.ok(userBaseService.updateAvatar(file,userBaseId));
    }
}