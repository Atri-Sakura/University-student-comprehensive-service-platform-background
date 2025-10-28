package com.ruoyi.platform.merchant.payment.impl;

import com.ruoyi.platform.domain.MerchantWithdrawRecord;
import com.ruoyi.platform.merchant.payment.PaymentGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 模拟支付网关出款接口（用于测试阶段）
 */
@Slf4j
@Component("mockPaymentGatewayClient")
public class MockPaymentGatewayClient implements PaymentGatewayClient {
    /**
     * 模拟支付网关处理提现请求
     *缺少通知前端
     * @param record 提现记录
     * @return 是否处理成功
     */
    @Override
    public boolean transfer(MerchantWithdrawRecord record) {
        try {
            log.info("💸 [Mock支付网关] 开始处理提现请求：withdrawId={}", record.getWithdrawId());
            Thread.sleep(2000); // 模拟网关处理延迟

            // 模拟 80% 成功率
            boolean success = Math.random() > 0.2;
            if (success) {
                log.info("✅ [Mock支付网关] 提现成功，到账金额：{}", record.getActualAmount());
            } else {
                log.warn("❌ [Mock支付网关] 提现失败（模拟原因：接口超时）");
            }
            return success;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
