package com.ruoyi.platform.rider.controller;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.RiderWallet;
import com.ruoyi.platform.domain.vo.RiderWalletBalanceVO;
import com.ruoyi.platform.service.IRiderWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/rider/wallet")
public class RiderWalletRiderController {

    @Autowired
    private IRiderWalletService riderWalletService;

    /**
     * 获取当前骑手钱包余额
     * this is a bug interface stop used
     */
    @GetMapping("/balance")
    public AjaxResult getRiderWalletBalance(){
        Long riderId = SecurityUtils.getUserId();

        if (riderId == null) {
            return AjaxResult.error("未检测到登录用户，请先登录或启用");
        }

        RiderWallet wallet = riderWalletService.selectRiderWalletByRiderBaseId(riderId);

        if (wallet == null) {
            return AjaxResult.error("未找到钱包信息");
        }

        RiderWalletBalanceVO vo = new RiderWalletBalanceVO(wallet.getBalance());
        return AjaxResult.success("查询成功",vo);
    }

}
