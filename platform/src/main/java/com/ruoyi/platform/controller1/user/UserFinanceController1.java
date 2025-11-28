package com.ruoyi.platform.controller1.user;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.dto.UserRechargeRequest;
import com.ruoyi.platform.domain.dto.UserWithdrawRequest;
import com.ruoyi.platform.service.IUserFinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/finance")
public class UserFinanceController1 {

    @Autowired
    private IUserFinanceService userFinanceService;

    //只有支付宝可用
    @PostMapping("/recharge/alipay")
    public AjaxResult rechargeByAlipay(@RequestBody UserRechargeRequest req)  throws Exception {
        Long userId = SecurityUtils.getUserBaseId();

        String payPageHtml = userFinanceService.rechargeByAlipay(userId, req);
        return AjaxResult.success()
                .put("payPageHtml", payPageHtml);

    }

    /**
     * 用户提现到支付宝（后端直连转账，无跳转）
     */
    @PostMapping("/withdraw/alipay")
    public AjaxResult withdrawByAlipay(@RequestBody UserWithdrawRequest req) throws Exception {
        Long userBaseId = SecurityUtils.getUserBaseId();

        userFinanceService.withdrawByAlipay(userBaseId, req);

        return AjaxResult.success("提现申请已受理，请稍后查看余额变动");
    }
}
