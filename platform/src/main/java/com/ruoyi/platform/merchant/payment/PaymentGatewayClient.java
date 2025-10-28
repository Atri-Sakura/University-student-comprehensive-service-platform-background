package com.ruoyi.platform.merchant.payment;

import com.ruoyi.platform.domain.MerchantWithdrawRecord;

/**
 * 出款网关客户端接口
 * 可 Mock、可接入真实支付网关（微信/支付宝）
 *
 * @author Jinx
 */
public interface PaymentGatewayClient {
    /**
     * 发起出款请求
     * @param record 提现记录
     * @return true 表示成功；false 表示失败
     */
    boolean transfer(MerchantWithdrawRecord record);
}
