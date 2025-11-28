package com.ruoyi.platform.controller1.rider;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.dto.RiderRechargeRequest;
import com.ruoyi.platform.domain.dto.RiderWithdrawRequest;
import com.ruoyi.platform.service.IRiderFinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rider/finance")
public class RiderFinanceController1 {

    @Autowired
    private IRiderFinanceService riderFinanceService;

    @PostMapping("/recharge/alipay")
    public AjaxResult rechargeByAlipay(@RequestBody RiderRechargeRequest req) throws Exception {

        Long riderBaseId = SecurityUtils.getRiderBaseId(); // 按你项目里真实获取方式来

        String payPageHtml = riderFinanceService.rechargeByAlipay(riderBaseId, req);

        return AjaxResult.success()
                .put("payPageHtml", payPageHtml);
    }

    @PostMapping("/withdraw/alipay")
    public AjaxResult withdrawByAlipay(@RequestBody RiderWithdrawRequest req) throws Exception {
        Long riderBaseId = SecurityUtils.getRiderBaseId();

        riderFinanceService.withdrawByAlipay(riderBaseId, req);
        // 能走到这里说明转账成功，本地事务也都提交了
        return AjaxResult.success("提现申请成功");
    }
}
