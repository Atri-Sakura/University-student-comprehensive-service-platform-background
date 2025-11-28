package com.ruoyi.platform.service;

import com.ruoyi.platform.domain.dto.RiderRechargeRequest;
import com.ruoyi.platform.domain.dto.RiderWithdrawRequest;


public interface IRiderFinanceService {

    String rechargeByAlipay(Long userBaseId, RiderRechargeRequest req) throws Exception;

    void withdrawByAlipay(Long riderBaseId, RiderWithdrawRequest req) throws Exception;
}
