package com.ruoyi.platform.rider.controller;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.RiderWallet;
import com.ruoyi.platform.domain.RiderWalletRecord;
import com.ruoyi.platform.domain.vo.RiderWalletBalanceVO;
import com.ruoyi.platform.service.IRiderWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("api/rider/wallet")
public class RiderWalletRiderController extends BaseController {

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

    /**
     * 初始化骑手钱包
     * @return
     */
    @PostMapping("init")
    public AjaxResult addRiderWallet() {
        Long riderId = SecurityUtils.getUserBaseId();
        if (riderId == null) {
            return AjaxResult.error("用户Id为空");
        }
        if(riderWalletService.selectRiderWalletByRiderBaseId(riderId) != null) {
            return AjaxResult.error("钱包已存在,请勿重复创建。");
        }
        try {
            Random random = new Random();
            int randomId = random.nextInt(90000000) + 10000000;
            Long riderWalletId = Long.valueOf(randomId);
            RiderWallet wallet = new RiderWallet();
            wallet.setRiderBaseId(riderId);
            wallet.setRiderWalletId(riderWalletId);
            wallet.setFreezeAmount(new BigDecimal(0.00));
            wallet.setBalance(new BigDecimal(0.00));
            riderWalletService.insertRiderWallet(wallet);
            return AjaxResult.success("初始化钱包成功");
        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 骑手钱包流水查询
     */
    @GetMapping("/flow")
    public TableDataInfo getRiderWalletFlow() {
        Long riderId = SecurityUtils.getUserBaseId();
        startPage();
        List<RiderWalletRecord> riderWalletRecords = riderWalletService.selectRiderWalletRecordByRiderBaseId(riderId);
        return getDataTable(riderWalletRecords);
    }


}
