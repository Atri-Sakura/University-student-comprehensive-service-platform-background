package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.platform.domain.MerchantBase;
import com.ruoyi.platform.domain.MerchantAddress;
import com.ruoyi.platform.merchant.service.IMerchantInfoService;
import com.ruoyi.platform.merchant.service.IMerchantAddressInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商家基础信息控制器
 * 支持 merchant_base 和 merchant_address 基础信息的查询与修改
 */
@RestController
@RequestMapping("/merchant/info")
public class MerchantInfoController {

    @Autowired
    private IMerchantInfoService merchantInfoService;

    @Autowired
    private IMerchantAddressInfoService merchantAddressInfoService;

    /**
     * 查询商家基础信息
     * @param merchantBaseId 商家ID
     * @return 商家基础信息
     */
    @GetMapping("/base/{merchantBaseId}")
    public AjaxResult getMerchantBaseInfo(@PathVariable Long merchantBaseId) {
        MerchantBase merchantBase = merchantInfoService.selectMerchantBaseByMerchantBaseId(merchantBaseId);
        if (merchantBase == null) {
            return AjaxResult.error("未找到该商家基础信息");
        }
        return AjaxResult.success(merchantBase);
    }

    /**
     * 修改商家基础信息
     * @param merchantBase 商家基础信息对象
     * @return 操作结果
     */
    @PutMapping("/base")
    public AjaxResult updateMerchantBase(@RequestBody MerchantBase merchantBase) {
        if (merchantBase == null || merchantBase.getMerchantBaseId() == null) {
            return AjaxResult.error("参数错误，缺少商家ID");
        }
        int result = merchantInfoService.updateMerchantBase(merchantBase);
        if (result > 0) {
            return AjaxResult.success("商家信息修改成功");
        }
        return AjaxResult.error("商家信息修改失败");
    }

    /**
     * 查询商家地址信息
     * @param merchantBaseId 商家ID
     * @return 商家地址信息
     */
    @GetMapping("/address/{merchantBaseId}")
    public AjaxResult getMerchantAddress(@PathVariable Long merchantBaseId) {
        MerchantAddress address = merchantAddressInfoService.selectMerchantAddressByMerchantBaseId(merchantBaseId);
        if (address == null) {
            return AjaxResult.error("未找到该商家地址信息");
        }
        return AjaxResult.success(address);
    }

    /**
     * 修改商家地址信息
     * @param merchantAddress 商家地址对象
     * @return 操作结果
     */
    @PutMapping("/address")
    public AjaxResult updateMerchantAddress(@RequestBody MerchantAddress merchantAddress) {
        if (merchantAddress == null || merchantAddress.getMerchantAddressId() == null) {
            return AjaxResult.error("参数错误，缺少地址ID");
        }
        int result = merchantAddressInfoService.updateMerchantAddress(merchantAddress);
        if (result > 0) {
            return AjaxResult.success("商家地址修改成功");
        }
        return AjaxResult.error("商家地址修改失败");
    }
}