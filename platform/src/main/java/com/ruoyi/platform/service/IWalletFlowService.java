package com.ruoyi.platform.service;

import java.math.BigDecimal;

/**
 * 钱包流转服务接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface IWalletFlowService {

    /**
     * 用户支付（钱进入平台钱包并冻结）
     *
     * @param userId 用户ID
     * @param orderMainId 订单ID
     * @param amount 支付金额
     */
    void userPay(Long userId, Long orderMainId, BigDecimal amount);

    /**
     * 结算给商家（商品金额）
     *
     * @param merchantId 商家ID
     * @param orderMainId 订单ID
     * @param goodsAmount 商品金额
     */
    void settleMerchant(Long merchantId, Long orderMainId, BigDecimal goodsAmount);

    /**
     * 结算给骑手（配送费）
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @param deliveryFee 配送费
     */
    void settleRider(Long riderId, Long orderMainId, BigDecimal deliveryFee);

    /**
     * 退款给用户
     *
     * @param userId 用户ID
     * @param orderMainId 订单ID
     * @param refundAmount 退款金额
     */
    void refundUser(Long userId, Long orderMainId, BigDecimal refundAmount);
}