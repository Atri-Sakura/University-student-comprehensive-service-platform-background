package com.ruoyi.platform.merchant.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
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
     * @return 商家基础信息
     */
    @GetMapping("/base")
    public AjaxResult getMerchantBaseInfo() {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
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
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        if (merchantBase == null || merchantBase.getMerchantBaseId() == null || !merchantBase.getMerchantBaseId().equals(merchantBaseId)) {
            return AjaxResult.error("无权修改该商家信息");
        }
        int result = merchantInfoService.updateMerchantBase(merchantBase);
        if (result > 0) {
            return AjaxResult.success("商家信息修改成功");
        }
        return AjaxResult.error("商家信息修改失败");
    }

    /**
     * 查询商家地址信息
     * @return 商家地址信息
     */
    @GetMapping("/address")
    public AjaxResult getMerchantAddress() {
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        MerchantAddress address = merchantAddressInfoService.selectMerchantAddressByMerchantBaseId(merchantBaseId);
        if (address == null) {
            return AjaxResult.error("未找到地址信息");
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
        Long merchantBaseId = SecurityUtils.getMerchantBaseId();
        MerchantAddress dbAddress = merchantAddressInfoService.selectMerchantAddressByMerchantBaseId(merchantBaseId);
        if (merchantAddress == null || dbAddress == null || !dbAddress.getMerchantAddressId().equals(merchantAddress.getMerchantAddressId())) {
            return AjaxResult.error("无权修改该商家地址信息");
        }
        int result = merchantAddressInfoService.updateMerchantAddress(merchantAddress);
        if (result > 0) {
            return AjaxResult.success("商家地址修改成功");
        }
        return AjaxResult.error("商家地址修改失败");
    }
}