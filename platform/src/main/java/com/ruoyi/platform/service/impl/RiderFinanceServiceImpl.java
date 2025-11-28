package com.ruoyi.platform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.platform.chat.utils.SnowflakeIdGenerator;
import com.ruoyi.platform.config.AlipayProperties;
import com.ruoyi.platform.domain.PayOrder;
import com.ruoyi.platform.domain.RiderWallet;
import com.ruoyi.platform.domain.RiderWalletRecord;
import com.ruoyi.platform.domain.dto.RiderRechargeRequest;
import com.ruoyi.platform.domain.dto.RiderWithdrawRequest;
import com.ruoyi.platform.domain.dto.UserRechargeRequest;
import com.ruoyi.platform.mapper.PayOrderMapper;
import com.ruoyi.platform.mapper.RiderWalletMapper;
import com.ruoyi.platform.mapper.RiderWalletRecordMapper;
import com.ruoyi.platform.service.IRiderFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class RiderFinanceServiceImpl implements IRiderFinanceService {

    @Autowired
    private RiderWalletMapper riderWalletMapper;
    @Autowired
    private RiderWalletRecordMapper riderWalletRecordMapper;
    @Autowired
    private PayOrderMapper payOrderMapper;
    @Autowired
    private AlipayProperties alipayProperties;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawByAlipay(Long riderBaseId, RiderWithdrawRequest req) throws Exception {

        // 0.（预留）校验支付密码 —— 先注释掉
        // if (!payPasswordService.checkRiderPayPassword(riderBaseId, req.getPayPassword())) {
        //     throw new RuntimeException("支付密码错误");
        // }

        // 1. 校验金额 & 渠道
        BigDecimal amount = req.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("提现金额必须大于0");
        }

        if (!Objects.equals(req.getPayChannel(), PayChannel.ALIPAY.getCode())) {
            throw new RuntimeException("当前仅支持支付宝提现");
        }

        // 2. 查询骑手钱包 & 校验余额
        RiderWallet wallet = riderWalletMapper.selectRiderWalletByRiderBaseId(riderBaseId);
        if (wallet == null) {
            throw new RuntimeException("骑手钱包不存在，请先初始化钱包");
        }
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足，无法提现");
        }

        // 3. 生成 outTradeNo / requestId
        String outTradeNo = buildRiderWithdrawOutTradeNo(riderBaseId);
        String requestId = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();

        // 4. 先“冻结余额”：可用余额减少，冻结金额增加
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setFreezeAmount(wallet.getFreezeAmount().add(amount));
        riderWalletMapper.updateRiderWallet(wallet);

        // 5. 写 pay_order（业务类型：骑手提现）
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(generateId());
        payOrder.setOutTradeNo(outTradeNo);
        payOrder.setChannel(PayChannel.ALIPAY.getCode());
        payOrder.setOwnerType(PayOwnerType.RIDER.getCode());
        payOrder.setOwnerId(riderBaseId);
        payOrder.setBizType(PayBizType.RIDER_WITHDRAW.getCode());
        // 暂时没有单独的骑手提现业务表，bizId 先为空
        payOrder.setTotalAmount(amount);
        payOrder.setStatus(PayOrderStatus.PROCESSING.getCode()); // 提现中
        payOrder.setNotifyStatus(0);
        payOrder.setNotifyTimes(0);
        payOrder.setCreateTime(now);
        payOrder.setUpdateTime(now);
        payOrderMapper.insertPayOrder(payOrder);

        // 6. 写骑手钱包流水（金额为负数，表示支出）
        RiderWalletRecord record = new RiderWalletRecord();
        record.setRiderWalletRecordId(generateId());
        record.setRiderWalletId(wallet.getRiderWalletId());
        record.setRiderBaseId(riderBaseId);
        record.setAmount(amount.negate()); // 支出 = 负数
        record.setTradeType(2L);           // 2 = 提现，或者用 RiderWalletTradeType.WITHDRAW.getCode()
        record.setRelatedId(payOrder.getPayOrderId());
        record.setTradeStatus(0L);         // 0 = 处理中
        record.setTradeTime(now);
        record.setRemark("骑手支付宝提现申请，outTradeNo=" + outTradeNo);
        riderWalletRecordMapper.insertRiderWalletRecord(record);

        // 7. 调用支付宝转账接口（后端直连，无页面）
        String payeeType = alipayProperties.getPayeeType();       // ALIPAY_LOGON_ID / ALIPAY_USER_ID
        String payeeAccount = alipayProperties.getPayeeAccount(); // dqotky2423@sandbox.com
        String payeeName = alipayProperties.getPayeeName();       // 必须跟沙箱买家姓名完全一致

        AlipayFundTransUniTransferResponse response =
                callAlipayTransferApi(outTradeNo, amount, payeeType, payeeAccount, payeeName);

        // 8. 根据支付宝返回结果更新本地状态 （同 user 提现）
        if (response == null || !response.isSuccess()) {

            // 8.1 转账失败：回滚冻结金额，恢复余额
            RiderWallet freshWallet = riderWalletMapper.selectRiderWalletByRiderBaseId(riderBaseId);
            freshWallet.setBalance(freshWallet.getBalance().add(amount));
            freshWallet.setFreezeAmount(freshWallet.getFreezeAmount().subtract(amount));
            riderWalletMapper.updateRiderWallet(freshWallet);

            // 8.2 pay_order 标记为失败
            payOrder.setStatus(PayOrderStatus.FAILED.getCode());
            payOrder.setUpdateTime(new Date());
            payOrderMapper.updateById(payOrder);

            // 8.3 钱包流水标记为失败
            riderWalletRecordMapper.updateTradeStatusByRelatedId(
                    payOrder.getPayOrderId(),
                    2L  // 2=失败
            );

            throw new RuntimeException("调用支付宝转账失败：" +
                    (response == null ? "response is null" : response.getSubMsg()));
        }

        // 9. 成功逻辑（这边不等 notify，直接以同步返回为准）

        // 9.1 更新 pay_order
        payOrder.setStatus(PayOrderStatus.SUCCESS.getCode());
        // 这个字段名具体看 SDK response；常见的是 getOrderId()/getTransId()，你实际调试一下
        payOrder.setChannelTradeNo(response.getOrderId());
        payOrder.setNotifyStatus(1);
        payOrder.setNotifyTimes(payOrder.getNotifyTimes() + 1);
        payOrder.setUpdateTime(new Date());
        payOrderMapper.updateById(payOrder);

        // 9.2 解冻金额（钱真正出账）
        RiderWallet freshWallet2 = riderWalletMapper.selectRiderWalletByRiderBaseId(riderBaseId);
        freshWallet2.setFreezeAmount(freshWallet2.getFreezeAmount().subtract(amount));
        riderWalletMapper.updateRiderWallet(freshWallet2);

        // 9.3 更新骑手钱包流水为成功
        riderWalletRecordMapper.updateTradeStatusByRelatedId(
                payOrder.getPayOrderId(),
                1L  // 1=成功
        );
    }

    private String buildRiderWithdrawOutTradeNo(Long riderBaseId) {
        return "RW" + riderBaseId + System.currentTimeMillis();
    }

    private Long generateId() {
        return SnowflakeIdGenerator.getInstance().nextId();
    }

    /**
     * 复用你之前写好的转账封装
     */
    private AlipayFundTransUniTransferResponse callAlipayTransferApi(
            String outTradeNo,
            BigDecimal amount,
            String payeeType,
            String payeeAccount,
            String payeeName) throws AlipayApiException {
        // 这里直接复用你在 UserFinanceServiceImpl 里那段实现就行
        // ✅ 拼接完整的网关地址
        String serverUrl = alipayProperties.getProtocol() + "://" + alipayProperties.getGatewayHost();
        if (!serverUrl.endsWith("/gateway.do")) {
            serverUrl = serverUrl + "/gateway.do";
        }
        AlipayClient client = new DefaultAlipayClient(
                serverUrl,
                alipayProperties.getAppId(),
                alipayProperties.getMerchantPrivateKey(),
                "json",
                "UTF-8",
                alipayProperties.getAlipayPublicKey(),
                alipayProperties.getSignType()
        );


        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_biz_no", outTradeNo);
        bizContent.put("trans_amount", amount.setScale(2, RoundingMode.HALF_UP).toPlainString());
        bizContent.put("product_code", "TRANS_ACCOUNT_NO_PWD");

        // 🔴 必填：biz_scene
        bizContent.put("biz_scene", "DIRECT_TRANSFER");  // 官方推荐值之一

        // ✅ 建议加：转账标题
        bizContent.put("order_title", "用户余额提现");

        // ✅ 必填：收款方信息
        JSONObject payeeInfo = new JSONObject();
        payeeInfo.put("identity_type", payeeType);
        payeeInfo.put("identity", payeeAccount);
        if (StringUtils.isNotBlank(payeeName)) {
            payeeInfo.put("name", payeeName);
        }
        bizContent.put("payee_info", payeeInfo);

        request.setBizContent(bizContent.toJSONString());

        return client.execute(request);
    }
    private String buildRiderRechargeOutTradeNo(Long riderBaseId) {
        return "RR" + riderBaseId + System.currentTimeMillis();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String rechargeByAlipay(Long riderBaseId, RiderRechargeRequest req) throws Exception {
        // 1. 校验金额 & 渠道
        BigDecimal amount = req.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("充值金额必须大于0");
        }
        if (!Objects.equals(req.getPayChannel(), PayChannel.ALIPAY.getCode())) {
            throw new RuntimeException("当前仅支持支付宝充值");
        }

        // 2. 查询/初始化骑手钱包
        RiderWallet wallet = riderWalletMapper.selectRiderWalletByRiderBaseId(riderBaseId);
        if (wallet == null) {
            wallet = new RiderWallet();
            wallet.setRiderWalletId(SnowflakeIdGenerator.getInstance().nextId());
            wallet.setRiderBaseId(riderBaseId);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setFreezeAmount(BigDecimal.ZERO);
            riderWalletMapper.insertRiderWallet(wallet);
        }

        // 3. 生成 outTradeNo、requestId
        String outTradeNo = buildRiderRechargeOutTradeNo(riderBaseId);
        String requestId = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();

        // 4. 写 pay_order（状态 PENDING）
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(SnowflakeIdGenerator.getInstance().nextId());
        payOrder.setOutTradeNo(outTradeNo);
        payOrder.setChannel(PayChannel.ALIPAY.getCode());
        payOrder.setOwnerType(PayOwnerType.RIDER.getCode());        // ⭐ 骑手
        payOrder.setOwnerId(riderBaseId);
        payOrder.setBizType(PayBizType.RIDER_RECHARGE.getCode());   // ⭐ 骑手充值
        payOrder.setTotalAmount(amount);
        payOrder.setStatus(PayOrderStatus.PENDING.getCode());
        payOrder.setNotifyStatus(0);
        payOrder.setNotifyTimes(0);
        payOrder.setCreateTime(now);
        payOrder.setUpdateTime(now);
        payOrderMapper.insertPayOrder(payOrder);

        // 5. 写骑手钱包流水（金额为正）
        RiderWalletRecord record = new RiderWalletRecord();
        record.setRiderWalletRecordId(SnowflakeIdGenerator.getInstance().nextId());
        record.setRiderWalletId(wallet.getRiderWalletId());
        record.setRiderBaseId(riderBaseId);
        record.setAmount(amount);   // 充值：正数
        record.setTradeType(UserWalletTradeType.RECHARGE.getCode());     // 看你约定，1=收入/充值 或 4=平台补贴
        record.setTradeStatus(TradeStatus.PROCESSING.getCode());   // 0=处理中
        record.setTradeTime(now);
        record.setRemark("骑手支付宝充值，outTradeNo=" + outTradeNo);
        record.setRelatedId(payOrder.getPayOrderId());  // ⭐ 关键关联
        riderWalletRecordMapper.insertRiderWalletRecord(record);

        // 6. 调用支付宝页面支付
        String subject = "骑手钱包充值";
        String totalAmount = amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
        String returnUrl = null; // 你可以先不用，APP 自己处理

        try {
            AlipayTradePagePayResponse response = Factory.Payment.Page()
                    .pay(subject, outTradeNo, totalAmount, returnUrl);

            // 能跑到这里，说明请求已经成功发给支付宝，返回的是一整页 HTML 表单
            return response.getBody();
        } catch (Exception e) {
            // 调用支付宝失败：把 pay_order 标成失败即可（钱包流水保持 “处理中” 也行）
            payOrderMapper.updateStatusById(
                    payOrder.getPayOrderId(),
                    PayOrderStatus.FAILED.getCode()
            );
            // 这里先不动 user_wallet_record，或者你也可以写一个 updateTradeStatusByRelatedId
            throw new RuntimeException("调用支付宝下单失败", e);
        }

    }
}
