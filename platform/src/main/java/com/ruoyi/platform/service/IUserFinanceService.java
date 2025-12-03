package com.ruoyi.platform.service;

import com.ruoyi.platform.domain.dto.UserRechargeRequest;
import com.ruoyi.platform.domain.dto.UserWithdrawRequest;
import com.ruoyi.platform.domain.vo.AlipayPagePayResult;

public interface IUserFinanceService {

    /**
     * 用户发起充值（支付宝页面支付）
     * @param userBaseId 当前登录用户ID
     * @param req        充值请求参数
     * @return 支付宝返回的 HTML form（你可以直接透给前端，让前端 innerHTML 渲染）
     */
    String rechargeByAlipay(Long userBaseId, UserRechargeRequest req) throws Exception;

    void withdrawByAlipay(Long userBaseId, UserWithdrawRequest req) throws Exception;
}
