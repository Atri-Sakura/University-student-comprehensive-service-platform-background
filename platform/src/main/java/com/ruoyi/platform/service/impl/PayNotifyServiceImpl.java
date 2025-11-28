package com.ruoyi.platform.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.ruoyi.common.enums.PayBizType;
import com.ruoyi.common.enums.PayOrderStatus;
import com.ruoyi.common.enums.PayOwnerType;
import com.ruoyi.platform.chat.utils.SnowflakeIdGenerator;
import com.ruoyi.platform.domain.*;
import com.ruoyi.platform.mapper.*;
import com.ruoyi.platform.merchant.mapper.MerchantWalletFlowMapper;
import com.ruoyi.platform.service.IPayNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class PayNotifyServiceImpl implements IPayNotifyService {
    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private UserWalletMapper userWalletMapper;

    @Autowired
    private UserWalletRecordMapper userWalletRecordMapper;

    @Autowired
    private RiderWalletMapper riderWalletMapper;

    @Autowired
    private RiderWalletRecordMapper riderWalletRecordMapper;

    @Autowired
    private MerchantWalletMapper merchantWalletMapper;

    @Autowired
    private MerchantWalletFlowMapper merchantWalletFlowMapper;

    //add rider and merchant later
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handleAlipayNotify(Map<String, String> params) throws Exception{
        // 处理支付宝异步通知逻辑
        String outTradeNo  = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");
        String totalAmount = params.get("total_amount");
        String channelTradeNo = params.get("trade_no");

        // 验证签名、更新订单状态等
        boolean signVerified = Factory.Payment.Common().verifyNotify(params);
        if (!signVerified) {
            System.out.println("【支付宝沙箱回调】验签失败：" + params);
            return "fail";
        }

        // 1. 查 pay_order
        PayOrder payOrder = payOrderMapper.selectByOutTradeNo(outTradeNo);
        if (payOrder == null) {
            // 查不到订单，直接返回 success，避免支付宝反复重试
            return "success";
        }

        // 2. 幂等：如果已经是成功 / 失败 / 关闭，就直接返回 success
        if (Objects.equals(payOrder.getStatus(), PayOrderStatus.SUCCESS.getCode())
                || Objects.equals(payOrder.getStatus(), PayOrderStatus.FAILED.getCode())
                || Objects.equals(payOrder.getStatus(), PayOrderStatus.CLOSED.getCode())) {
            return "success";
        }
        // 3. 校验金额
        BigDecimal notifyAmount = new BigDecimal(totalAmount);
        if (payOrder.getTotalAmount().compareTo(notifyAmount) != 0) {
            // 金额不一致，标记失败
            payOrderMapper.updateStatusById(payOrder.getPayOrderId(), PayOrderStatus.FAILED.getCode());
            return "fail";
        }

        // 4. 根据 trade_status 处理，这里只关心成功
        if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {

            // 4.1 更新 pay_order
            payOrder.setStatus(PayOrderStatus.SUCCESS.getCode());
            payOrder.setChannelTradeNo(channelTradeNo);
            payOrder.setNotifyStatus(1);
            payOrder.setNotifyTimes(payOrder.getNotifyTimes() + 1);
            payOrder.setUpdateTime(new Date());
            payOrderMapper.updateById(payOrder); // 你可以写一个 updatePayOrder 调用

            // 4.2 增加用户钱包余额（这里只处理用户充值业务）
            if (Objects.equals(payOrder.getOwnerType(), PayOwnerType.USER.getCode())
                    && Objects.equals(payOrder.getBizType(), PayBizType.USER_RECHARGE.getCode())) {

                // 查钱包
                UserWallet wallet = userWalletMapper.selectUserWalletByUserBaseId(payOrder.getOwnerId());
                if (wallet != null) {
                    wallet.setBalance(wallet.getBalance().add(notifyAmount));
                    userWalletMapper.updateUserWallet(wallet);
                }

                // 更新钱包流水状态（如果你后面把 out_trade_no 字段写进流水表，就可以按 outTradeNo 更新）
                // 这里先按 related_id = pay_order_id 来更新，你可以写一个专门 SQL：
                // update user_wallet_record set trade_status = 1 where related_id = #{payOrderId}
                userWalletRecordMapper.updateTradeStatusByOutTradeNo(
                        payOrder.getOutTradeNo(),
                        1L // 1=成功
                );
            }// 4.3 增加骑手钱包余额（这里只处理骑手充值业务）
            else if (Objects.equals(payOrder.getOwnerType(), PayOwnerType.RIDER.getCode())
                    && Objects.equals(payOrder.getBizType(), PayBizType.RIDER_RECHARGE.getCode())) {

                notifyAmount = new BigDecimal(totalAmount);

                // 查骑手钱包
                RiderWallet wallet = riderWalletMapper.selectRiderWalletByRiderBaseId(payOrder.getOwnerId());
                if (wallet != null) {
                    wallet.setBalance(wallet.getBalance().add(notifyAmount));
                    riderWalletMapper.updateRiderWallet(wallet);
                }

                // 更新骑手流水状态为成功
                riderWalletRecordMapper.updateStatusOnRechargeSuccessByRelatedId(
                        payOrder.getPayOrderId(), 1  // 1=成功
                );
            }// ===== ✅ 商家充值到账逻辑 =====
            else if (Objects.equals(payOrder.getOwnerType(), PayOwnerType.MERCHANT.getCode())
                    && Objects.equals(payOrder.getBizType(), PayBizType.MERCHANT_RECHARGE.getCode())) {

                Long merchantBaseId = payOrder.getOwnerId();

                // 1) 更新商家钱包余额
                MerchantWallet wallet = merchantWalletMapper.selectMerchantWalletByMerchantId(merchantBaseId);
                if (wallet != null) {
                    BigDecimal beforeBalance = wallet.getBalance();
                    wallet.setBalance(beforeBalance.add(notifyAmount));
                    wallet.setUpdateTime(new Date());
                    merchantWalletMapper.updateMerchantWallet(wallet);

                    // 2) 写一条商家钱包流水
                    MerchantWalletFlow flow = new MerchantWalletFlow();
                    // flowId 由数据库自增生成，不必在这里 set
                    flow.setMerchantBaseId(merchantBaseId);
                    flow.setOrderMainId(payOrder.getPayOrderId()); // 这里用 pay_order_id 关联起来
                    flow.setWithdrawId(null);
                    flow.setFlowType("RECHARGE");              // 你也可以用 "INCOME"，统一就行
                    flow.setFlowAmount(notifyAmount);          // 正数 = 收入
                    flow.setBalanceAfter(wallet.getBalance()); // 变动后的余额
                    flow.setDescription("商家支付宝充值成功，outTradeNo=" + outTradeNo);
                    flow.setCreateTime(new Date());

                    merchantWalletFlowMapper.insertMerchantWalletFlow(flow);

                }

            }
            return "success";
        }
        // 示例返回值，实际应根据处理结果返回
        return "success";
    }
}
