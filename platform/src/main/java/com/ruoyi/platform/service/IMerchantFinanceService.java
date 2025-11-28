package com.ruoyi.platform.service;

import com.ruoyi.platform.domain.dto.MerchantRechargeRequest;
import com.ruoyi.platform.domain.dto.MerchantWithdrawRequest;

public interface IMerchantFinanceService {

    public String rechargeByAlipay(Long merchantBaseId,
                                   MerchantRechargeRequest req) throws Exception;

    void withdrawByAlipay(Long merchantBaseId, MerchantWithdrawRequest req) throws Exception;
}
