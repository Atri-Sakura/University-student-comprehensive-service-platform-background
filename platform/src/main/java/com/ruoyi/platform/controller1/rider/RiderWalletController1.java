package com.ruoyi.platform.controller1.rider;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.service.IRiderWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/rider/wallet")
public class RiderWalletController1 {
    @Autowired
    private IRiderWalletService riderWalletService;

    @GetMapping("/balance")
    public AjaxResult getWalletBalance() {
        // 假设有方法可以获取当前骑手的ID
        Long riderId = SecurityUtils.getRiderBaseId();
        BigDecimal balance = riderWalletService.getWalletBalance(riderId);
        return AjaxResult.success().put("balance", balance);
    }
}
