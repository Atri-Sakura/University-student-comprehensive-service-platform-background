package com.ruoyi.platform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.ruoyi.common.enums.PayBizType;
import com.ruoyi.common.enums.PayChannel;
import com.ruoyi.common.enums.PayOrderStatus;
import com.ruoyi.common.enums.PayOwnerType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.platform.chat.utils.SnowflakeIdGenerator;
import com.ruoyi.platform.config.AlipayProperties;
import com.ruoyi.platform.domain.MerchantWallet;
import com.ruoyi.platform.domain.MerchantWalletFlow;
import com.ruoyi.platform.domain.MerchantWithdrawRecord;
import com.ruoyi.platform.domain.PayOrder;
import com.ruoyi.platform.domain.dto.MerchantRechargeRequest;
import com.ruoyi.platform.domain.dto.MerchantWithdrawRequest;
import com.ruoyi.platform.mapper.MerchantWalletMapper;
import com.ruoyi.platform.mapper.PayOrderMapper;
import com.ruoyi.platform.merchant.mapper.MerchantWalletFlowMapper;
import com.ruoyi.platform.merchant.mapper.MerchantWithdrawRecordMapper;
import com.ruoyi.platform.service.IMerchantFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class MerchantFinanceServiceImpl implements IMerchantFinanceService {
    @Autowired
    private MerchantWalletMapper merchantWalletMapper;
    @Autowired
    private MerchantWalletFlowMapper merchantWalletFlowMapper;
    @Autowired
    private PayOrderMapper payOrderMapper;
    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private MerchantWithdrawRecordMapper merchantWithdrawRecordMapper; // ✅ 新增

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawByAlipay(Long merchantBaseId, MerchantWithdrawRequest req) throws Exception {

        // 1. 校验金额 & 渠道
        BigDecimal amount = req.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("提现金额必须大于0");
        }

        if (!Objects.equals(req.getPayChannel(), PayChannel.ALIPAY.getCode())) {
            throw new RuntimeException("当前仅支持支付宝提现");
        }

        // 2. 查询商家钱包 & 校验余额
        MerchantWallet wallet = merchantWalletMapper.selectMerchantWalletByMerchantId(merchantBaseId);
        if (wallet == null) {
            throw new RuntimeException("商家钱包不存在，请先完成充值或初始化钱包");
        }

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("商家钱包余额不足，无法提现");
        }

        // 3. 生成 outTradeNo
        String outTradeNo = buildMerchantWithdrawOutTradeNo(merchantBaseId);
        Date now = new Date();
        // ================== ✅ 3.1 先写一条商家提现记录 ==================
        MerchantWithdrawRecord withdrawRecord = new MerchantWithdrawRecord();
        // withdraw_id 自增，这里不手动 set
        withdrawRecord.setMerchantBaseId(merchantBaseId);

        // 这里没有前端选择提现账户，就先用 0 占位（逻辑外键，没有物理 FK 约束没关系）
        withdrawRecord.setAccountId(0L);

        withdrawRecord.setWithdrawAmount(amount);
        withdrawRecord.setFeeAmount(BigDecimal.ZERO);          // 现在不收手续费
        withdrawRecord.setActualAmount(amount);                // 到账 = 提现金额
        withdrawRecord.setWithdrawStatus("PROCESSING");        // 已经在处理中
        withdrawRecord.setRequestTime(now);
        withdrawRecord.setProcessTime(null);
        withdrawRecord.setRemark("商家支付宝即时提现发起，outTradeNo=" + outTradeNo);
        withdrawRecord.setOperatorName("SYSTEM");
        withdrawRecord.setIdempotentKey(outTradeNo);           // 幂等 key 用 outTradeNo

        merchantWithdrawRecordMapper.insertMerchantWithdrawRecord(withdrawRecord);
        Long withdrawId = withdrawRecord.getWithdrawId();

        // 4. 冻结余额：可用余额减少，冻结金额增加
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setFreezeAmount(wallet.getFreezeAmount().add(amount));
        wallet.setUpdateTime(now);
        merchantWalletMapper.updateMerchantWallet(wallet);

        // 5. 写 pay_order（业务类型：商家提现）
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(SnowflakeIdGenerator.getInstance().nextId());
        payOrder.setOutTradeNo(outTradeNo);
        payOrder.setChannel(PayChannel.ALIPAY.getCode());

        payOrder.setOwnerType(PayOwnerType.MERCHANT.getCode());
        payOrder.setOwnerId(merchantBaseId);

        // ⚠️ 这里需要你在 PayBizType 里有一个 MERCHANT_WITHDRAW
        payOrder.setBizType(PayBizType.MERCHANT_WITHDRAW.getCode());
        payOrder.setBizId(withdrawId);

        payOrder.setTotalAmount(amount);
        payOrder.setStatus(PayOrderStatus.PROCESSING.getCode());  // 提现中
        payOrder.setNotifyStatus(0);
        payOrder.setNotifyTimes(0);
        payOrder.setCreateTime(now);
        payOrder.setUpdateTime(now);

        payOrderMapper.insertPayOrder(payOrder);

        // 6. 调用支付宝转账（后端直连）
        String payeeType    = alipayProperties.getPayeeType();
        String payeeAccount = alipayProperties.getPayeeAccount();
        String payeeName    = alipayProperties.getPayeeName();

        AlipayFundTransUniTransferResponse response = callAlipayTransferApi(
                outTradeNo,
                amount,
                payeeType,
                payeeAccount,
                payeeName
        );

        // 7. 处理支付宝返回结果
        if (response == null || !response.isSuccess()) {

            // 7.1 失败：回滚冻结金额，恢复可用余额
            MerchantWallet freshWallet = merchantWalletMapper.selectMerchantWalletByMerchantId(merchantBaseId);
            freshWallet.setBalance(freshWallet.getBalance().add(amount));
            freshWallet.setFreezeAmount(freshWallet.getFreezeAmount().subtract(amount));
            freshWallet.setUpdateTime(new Date());
            merchantWalletMapper.updateMerchantWallet(freshWallet);

            // 7.2 pay_order 标记为失败
            payOrder.setStatus(PayOrderStatus.FAILED.getCode());
            payOrder.setUpdateTime(new Date());
            payOrderMapper.updateById(payOrder);

            // 5.3 提现记录标记为失败
            MerchantWithdrawRecord failRecord = new MerchantWithdrawRecord();
            failRecord.setWithdrawId(withdrawId);
            failRecord.setWithdrawStatus("FAILED");
            failRecord.setProcessTime(new Date());

            String errorMsg = (response == null)
                    ? "response is null"
                    : ("code=" + response.getCode() + ", subCode=" + response.getSubCode()
                    + ", subMsg=" + response.getSubMsg());
            merchantWithdrawRecordMapper.updateMerchantWithdrawRecord(failRecord);
            throw new RuntimeException("调用支付宝转账失败：" + errorMsg);
        }

        // 8. 成功：更新 pay_order、解冻金额、记流水
        // 8.1 pay_order 成功
        payOrder.setStatus(PayOrderStatus.SUCCESS.getCode());
        // 这里建议用 orderId 作为三方交易号
        payOrder.setChannelTradeNo(response.getOrderId());
        payOrder.setNotifyStatus(1);
        payOrder.setNotifyTimes(payOrder.getNotifyTimes() + 1);
        payOrder.setUpdateTime(new Date());
        payOrderMapper.updateById(payOrder);

        // 8.2 解冻金额（钱真正离开平台）
        MerchantWallet freshWallet2 = merchantWalletMapper.selectMerchantWalletByMerchantId(merchantBaseId);
        freshWallet2.setFreezeAmount(freshWallet2.getFreezeAmount().subtract(amount));
        freshWallet2.setUpdateTime(new Date());
        merchantWalletMapper.updateMerchantWallet(freshWallet2);

        // 8.3 写一条商家提现流水（flow_amount 为负数）
        MerchantWalletFlow flow = new MerchantWalletFlow();
        // flowId 是自增的，可不手动填
        flow.setMerchantBaseId(merchantBaseId);
        flow.setOrderMainId(payOrder.getPayOrderId());      // 用 pay_order_id 做关联
        flow.setWithdrawId(withdrawId);                           // 你这条不是走原来的提现申请表，可为空
        flow.setFlowType("WITHDRAW");                       // 提现
        flow.setFlowAmount(amount.negate());                // 负数 = 支出
        flow.setBalanceAfter(freshWallet2.getBalance());    // 变动后的可用余额
        flow.setDescription("商家支付宝提现成功，outTradeNo=" + outTradeNo);
        flow.setCreateTime(new Date());

        // 6.4 提现记录标记为 SUCCESS
        MerchantWithdrawRecord successRecord = new MerchantWithdrawRecord();
        successRecord.setWithdrawId(withdrawId);
        successRecord.setWithdrawStatus("SUCCESS");
        successRecord.setProcessTime(new Date());
        successRecord.setRemark("支付宝打款成功，orderId=" + response.getOrderId());
        successRecord.setOperatorName("SYSTEM");
        merchantWithdrawRecordMapper.updateMerchantWithdrawRecord(successRecord);

        merchantWalletFlowMapper.insertMerchantWalletFlow(flow);
    }

    private String buildMerchantWithdrawOutTradeNo(Long merchantBaseId) {
        // MW = Merchant Withdraw
        return "MW" + merchantBaseId + System.currentTimeMillis();
    }

    /**
     * 复用你之前已经调通的 alipay.fund.trans.uni.transfer 调用
     */
    private AlipayFundTransUniTransferResponse callAlipayTransferApi(
            String outTradeNo,
            BigDecimal amount,
            String payeeType,
            String payeeAccount,
            String payeeName) throws AlipayApiException {

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
        bizContent.put("order_title", "商家余额提现");
        bizContent.put("biz_scene", "DIRECT_TRANSFER");
        bizContent.put("out_biz_no", outTradeNo);
        bizContent.put("trans_amount", amount.setScale(2, RoundingMode.HALF_UP).toPlainString());
        bizContent.put("product_code", "TRANS_ACCOUNT_NO_PWD");

        // 收款方
        JSONObject payeeInfo = new JSONObject();
        payeeInfo.put("identity_type", payeeType);   // ALIPAY_LOGON_ID / ALIPAY_USER_ID
        payeeInfo.put("identity", payeeAccount);     // 你的沙箱收款账号
        if (StringUtils.isNotBlank(payeeName)) {
            payeeInfo.put("name", payeeName);        // 必须和沙箱账号姓名一致
        }
        bizContent.put("payee_info", payeeInfo);

        request.setBizContent(bizContent.toJSONString());

        return client.execute(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String rechargeByAlipay(Long merchantBaseId,
                                   MerchantRechargeRequest req) throws Exception {

        // 1. 校验金额 & 渠道
        BigDecimal amount = req.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("充值金额必须大于0");
        }

        if (!Objects.equals(req.getPayChannel(), PayChannel.ALIPAY.getCode())) {
            throw new RuntimeException("当前仅支持支付宝充值");
        }

        // 2. 查询/初始化商家钱包
        MerchantWallet wallet = merchantWalletMapper.selectMerchantWalletByMerchantId(merchantBaseId);
        if (wallet == null) {
            wallet = new MerchantWallet();
            wallet.setMerchantWalletId(SnowflakeIdGenerator.getInstance().nextId());
            wallet.setMerchantBaseId(merchantBaseId);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setFreezeAmount(BigDecimal.ZERO);
            wallet.setCreateTime(new Date());
            wallet.setUpdateTime(new Date());
            merchantWalletMapper.insertMerchantWallet(wallet);
        }

        // 3. 生成 outTradeNo / requestId
        String outTradeNo = buildMerchantRechargeOutTradeNo(merchantBaseId);
        // 这里你可以暂时不用 requestId，或者用前端传的
        String requestId = UUID.randomUUID().toString().replace("-", "");

        Date now = new Date();

        // 4. 插 pay_order（状态 PENDING）
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(SnowflakeIdGenerator.getInstance().nextId());
        payOrder.setOutTradeNo(outTradeNo);
        payOrder.setChannel(PayChannel.ALIPAY.getCode());

        payOrder.setOwnerType(PayOwnerType.MERCHANT.getCode());
        payOrder.setOwnerId(merchantBaseId);

        payOrder.setBizType(PayBizType.MERCHANT_RECHARGE.getCode());
        // bizId 暂时为空

        payOrder.setTotalAmount(amount);
        payOrder.setStatus(PayOrderStatus.PENDING.getCode());
        payOrder.setNotifyStatus(0);
        payOrder.setNotifyTimes(0);
        payOrder.setCreateTime(now);
        payOrder.setUpdateTime(now);

        payOrderMapper.insertPayOrder(payOrder);

        // 5. 调用支付宝页面支付，生成 payPageHtml（和用户充值那套一模一样）
        String subject = "商家余额充值";
        String totalAmount = amount.setScale(2, RoundingMode.HALF_UP).toPlainString();

        String returnUrl = null; // 或者 null

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

    private String buildMerchantRechargeOutTradeNo(Long merchantBaseId) {
        // MR = Merchant Recharge
        return "MR" + merchantBaseId + System.currentTimeMillis();
    }
}
