package com.ruoyi.platform.user.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.domain.UserAddress;
import com.ruoyi.platform.service.IUserAddressService;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户视角的用户地址信息控制器（只能操作自己的收货地址）
 *
 * @author ruoyi
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/user/address")
public class UserAddressInfoController {

    @Autowired
    private IUserAddressService userAddressService;

    /**
     * 查询当前用户的所有收货地址
     *
     * @return 收货地址列表
     */
    @GetMapping("/list")
    public AjaxResult list() {
        Long userBaseId = SecurityUtils.getUserBaseId();
        UserAddress criteria = new UserAddress();
        criteria.setUserBaseId(userBaseId);
        List<UserAddress> list = userAddressService.selectUserAddressList(criteria);
        return AjaxResult.success(list);
    }

    /**
     * 新增收货地址
     *
     * @param userAddress 地址信息（body传递部分字段）
     * @return 操作结果
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody UserAddress userAddress) {
        // 强制限定当前用户
        userAddress.setUserBaseId(SecurityUtils.getUserBaseId());
        int result = userAddressService.insertUserAddress(userAddress);
        return AjaxResult.success(result > 0 ? "新增收货地址成功" : "新增失败");
    }

    /**
     * 更新收货地址（只能修改自己的地址）
     *
     * @param userAddress 新的地址内容（body传递，包含userAddressId）
     * @return 操作结果
     */
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody UserAddress userAddress) {
        // 校验该地址是否属于当前用户
        UserAddress origin = userAddressService.selectUserAddressByUserAddressId(userAddress.getUserAddressId());
        Long userBaseId = SecurityUtils.getUserBaseId();
        if (origin == null || !userBaseId.equals(origin.getUserBaseId())) {
            return AjaxResult.error("无权限操作该地址！");
        }
        userAddress.setUserBaseId(userBaseId);
        int result = userAddressService.updateUserAddress(userAddress);
        return AjaxResult.success(result > 0 ? "修改收货地址成功" : "修改失败");
    }

    /**
     * 根据地址ID查询收货地址详情（只能查自己的地址）
     *
     * @param userAddressId 地址主键
     * @return 地址详情
     */
    @GetMapping("/detail/{userAddressId}")
    public AjaxResult detail(@PathVariable("userAddressId") Long userAddressId) {
        UserAddress address = userAddressService.selectUserAddressByUserAddressId(userAddressId);
        Long userBaseId = SecurityUtils.getUserBaseId();
        if (address == null || !userBaseId.equals(address.getUserBaseId())) {
            return AjaxResult.error("无权限查看该地址！");
        }
        return AjaxResult.success(address);
    }

    /**
     * 删除收货地址（支持批量删，只能删属于自己的地址）
     *
     * @param userAddressIds 地址主键数组
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public AjaxResult remove(@RequestBody Long[] userAddressIds) {
        Long userBaseId = SecurityUtils.getUserBaseId();
        // 校验所有id都属于当前用户
        for (Long id : userAddressIds) {
            UserAddress address = userAddressService.selectUserAddressByUserAddressId(id);
            if (address == null || !userBaseId.equals(address.getUserBaseId())) {
                return AjaxResult.error("无权限删除地址：" + id);
            }
        }
        int result = userAddressService.deleteUserAddressByUserAddressIds(userAddressIds);
        return AjaxResult.success(result > 0 ? "删除收货地址成功" : "删除失败");
    }
}