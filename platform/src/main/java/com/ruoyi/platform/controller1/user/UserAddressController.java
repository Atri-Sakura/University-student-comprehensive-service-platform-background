package com.ruoyi.platform.controller1.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.platform.domain.UserAddress;
import com.ruoyi.platform.service.IUserAddressService;
import com.ruoyi.common.core.domain.R;

/**
 * 用户地址Controller
 *
 * @author ruoyi
 * @date 2025-10-16
 */
@RestController
@RequestMapping("/platform/user/address")
public class UserAddressController {

    @Autowired
    private IUserAddressService userAddressService;

    /**
     * 查询用户地址详情
     */
    @GetMapping("/{userAddressId}")
    public R<UserAddress> getAddress(@PathVariable("userAddressId") Long userAddressId) {
        UserAddress address = userAddressService.selectUserAddressByUserAddressId(userAddressId);
        return address != null ? R.ok(address) : R.fail("用户地址不存在");
    }

    /**
     * 查询用户地址列表
     */
    @GetMapping("/list")
    public R<List<UserAddress>> list(UserAddress userAddress) {
        List<UserAddress> list = userAddressService.selectUserAddressList(userAddress);
        return R.ok(list);
    }

    /**
     * 新增用户地址
     */
    @PostMapping
    public R<Integer> add(@RequestBody UserAddress userAddress) {
        int result = userAddressService.insertUserAddress(userAddress);
        return result > 0 ? R.ok(result, "新增地址成功") : R.fail("新增地址失败");
    }

    /**
     * 修改用户地址
     */
    @PutMapping
    public R<Integer> edit(@RequestBody UserAddress userAddress) {
        int result = userAddressService.updateUserAddress(userAddress);
        return result > 0 ? R.ok(result, "修改地址成功") : R.fail("修改地址失败");
    }

    /**
     * 批量删除用户地址
     */
    @DeleteMapping("/{userAddressIds}")
    public R<Integer> remove(@PathVariable Long[] userAddressIds) {
        int result = userAddressService.deleteUserAddressByUserAddressIds(userAddressIds);
        return result > 0 ? R.ok(result, "删除地址成功") : R.fail("删除地址失败");
    }
}