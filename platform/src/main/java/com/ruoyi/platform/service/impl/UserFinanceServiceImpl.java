package com.ruoyi.platform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.platform.chat.utils.SnowflakeIdGenerator;
import com.ruoyi.platform.config.AlipayProperties;
import com.ruoyi.platform.domain.PayOrder;
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.domain.UserWallet;
import com.ruoyi.platform.domain.UserWalletRecord;
import com.ruoyi.platform.domain.dto.UserRechargeRequest;
import com.ruoyi.platform.domain.dto.UserWithdrawRequest;
import com.ruoyi.platform.mapper.PayOrderMapper;
import com.ruoyi.platform.mapper.UserWalletMapper;
import com.ruoyi.platform.mapper.UserWalletRecordMapper;
import com.ruoyi.platform.service.IUserFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.ruoyi.framework.datasource.DynamicDataSourceContextHolder.log;

@Service
public class UserFinanceServiceImpl implements IUserFinanceService {
    @Autowired
    private PayOrderMapper payOrderMapper;
    @Autowired
    private UserWalletMapper userWalletMapper;

    @Autowired
    private UserWalletRecordMapper userWalletRecordMapper;

    @Autowired
    private AlipayProperties alipayProperties;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawByAlipay(Long userBaseId, UserWithdrawRequest req) throws Exception {


        // 1. 校验金额 & 渠道
        BigDecimal amount = req.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("提现金额必须大于0");
        }

        if (!Objects.equals(req.getPayChannel(), PayChannel.ALIPAY.getCode())) {
            throw new RuntimeException("当前仅支持支付宝提现");
        }

        // 2. 查询用户钱包 & 校验余额
        UserWallet wallet = userWalletMapper.selectUserWalletByUserBaseId(userBaseId);
        if (wallet == null) {
            throw new RuntimeException("用户钱包不存在，请先进行充值或初始化钱包");
        }

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("余额不足，无法提现");
        }
    //  支付密码校验由于现在账户没有对应的支付密码先不管它
//        // 假设你在 user_base 或 user_wallet 里存了一个 payPasswordHash
//        UserBase userBase = userBaseMapper.selectUserBaseById(userBaseId);
//        String inputPwd = req.getPayPassword();
//        if (StringUtils.isBlank(inputPwd)) {
//            throw new RuntimeException("请输入支付密码");
//        }
//
//        // 用 PasswordEncoder 比对（不要明文）
//        if (!passwordEncoder.matches(inputPwd, userBase.getPayPasswordHash())) {
//            throw new RuntimeException("支付密码错误");
//        }

        // 3. 生成 outTradeNo / requestId
        String outTradeNo = buildWithdrawOutTradeNo(userBaseId);

        String requestId = UUID.randomUUID().toString().replace("-", "");

        Date now = new Date();

        // 4. 先“冻结余额”：可用余额减少，冻结金额增加
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setFreezeAmount(wallet.getFreezeAmount().add(amount));
        userWalletMapper.updateUserWallet(wallet);

        // 5. 写 pay_order（业务类型：用户提现）
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(generateId());
        payOrder.setOutTradeNo(outTradeNo);
        payOrder.setChannel(PayChannel.ALIPAY.getCode());
        payOrder.setOwnerType(PayOwnerType.USER.getCode());
        payOrder.setOwnerId(userBaseId);
        payOrder.setBizType(PayBizType.USER_WITHDRAW.getCode());
        // 用户提现暂时没有专门的业务表，就先不填 bizId
        payOrder.setTotalAmount(amount);
        payOrder.setStatus(PayOrderStatus.PROCESSING.getCode()); // 提现中
        payOrder.setNotifyStatus(0);
        payOrder.setNotifyTimes(0);
        payOrder.setCreateTime(now);
        payOrder.setUpdateTime(now);
        payOrderMapper.insertPayOrder(payOrder);

        // 6. 写用户钱包流水（金额为负数，表示支出）
        UserWalletRecord record = new UserWalletRecord();
        record.setUserWalletRecordId(generateId());
        record.setUserWalletId(wallet.getUserWalletId());
        record.setUserBaseId(userBaseId);
        record.setAmount(amount.negate()); // 支出 = 负数
        record.setTradeType(UserWalletTradeType.WITHDRAW.getCode());           // 2-提现
        record.setTradeStatus(TradeStatus.PROCESSING.getCode());         // 0-处理中
        record.setTradeTime(now);
        record.setRemark("用户支付宝提现申请，outTradeNo=" + outTradeNo);
        record.setRequestId(requestId);
        record.setPayChannel((long) PayChannel.ALIPAY.getCode());
        record.setOutTradeNo(outTradeNo);
        record.setRelatedId(payOrder.getPayOrderId());
        // channel_trade_no / notify_time 回头在成功时填
        userWalletRecordMapper.insertForPay(record);

        // 7. 调用支付宝转账接口（后端直连，无跳转页面）
        // ------- 下面这部分根据你接的具体 EasySDK 方法改名字即可 -------

        String payeeType = alipayProperties.getPayeeType();
        String payeeAccount = alipayProperties.getPayeeAccount();
        String payeeName = alipayProperties.getPayeeName();

        //EasyAlipay just call
        AlipayFundTransUniTransferResponse response =
                callAlipayTransferApi(outTradeNo, amount, payeeType, payeeAccount, payeeName);

        // 8. 根据支付宝返回结果更新本地状态（非常关键）
        if (response == null || !response.isSuccess()) {


            // 8.1 转账失败：回滚冻结金额，恢复用户余额
            UserWallet freshWallet = userWalletMapper.selectUserWalletByUserBaseId(userBaseId);
            freshWallet.setBalance(freshWallet.getBalance().add(amount));
            freshWallet.setFreezeAmount(freshWallet.getFreezeAmount().subtract(amount));
            userWalletMapper.updateUserWallet(freshWallet);

            // 8.2 pay_order 标记为失败
            payOrder.setStatus(PayOrderStatus.FAILED.getCode());
            payOrder.setUpdateTime(new Date());
            payOrderMapper.updateById(payOrder);

            // 8.3 钱包流水标记为失败
            userWalletRecordMapper.updateTradeStatusByOutTradeNo(outTradeNo, 2L); // 2=失败

            throw new RuntimeException("调用支付宝转账失败：" +
                    (response == null ? "response is null" : response.getMsg()));
        }

        // 9. 转账成功逻辑（这里就不依赖 notify 异步回调了，直接认为同步结果为准）

        // 9.1 更新 pay_order
        payOrder.setStatus(PayOrderStatus.SUCCESS.getCode());
        payOrder.setChannelTradeNo(response.getSubMsg()); // TODO: 换成真实的交易号字段
        payOrder.setNotifyStatus(1);
        payOrder.setNotifyTimes(payOrder.getNotifyTimes() + 1);
        payOrder.setUpdateTime(new Date());
        payOrderMapper.updateById(payOrder);

        // 9.2 解冻金额（钱真正出账）
        UserWallet freshWallet2 = userWalletMapper.selectUserWalletByUserBaseId(userBaseId);
        freshWallet2.setFreezeAmount(freshWallet2.getFreezeAmount().subtract(amount));
        userWalletMapper.updateUserWallet(freshWallet2);

        // 9.3 更新钱包流水为成功 + 记录 channel_trade_no / notify_time
        userWalletRecordMapper.updateOnWithdrawSuccess(
                outTradeNo,
                1L,                    // 1=成功
                response.getSubMsg()   // TODO: 换成真实交易号
        );
    }


    private String buildWithdrawOutTradeNo(Long userBaseId) {
        return "UW" + userBaseId + System.currentTimeMillis();
    }

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


    /*
    充值
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String rechargeByAlipay(Long userBaseId, UserRechargeRequest req) throws Exception {
        //1.校验金额 & 渠道
        BigDecimal amount = req.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("充值金额必须大于0");
        }

        //2.调用pagechannel
        if (!Objects.equals(req.getPayChannel(), PayChannel.ALIPAY.getCode())) {
            throw new RuntimeException("暂不支持该支付渠道");
        }

        // 2. 查询/初始化用户钱包
        UserWallet wallet = userWalletMapper.selectUserWalletByUserBaseId(userBaseId);
        if (wallet == null) {
            wallet = new UserWallet();
            wallet.setUserWalletId(generateId()); // 你自己的雪花ID
            wallet.setUserBaseId(userBaseId);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setFreezeAmount(BigDecimal.ZERO);
            userWalletMapper.insertUserWallet(wallet);
        }

        // 3. 生成 outTradeNo、requestId
        String outTradeNo = buildOutTradeNo(userBaseId);
        String requestId = UUID.randomUUID().toString().replace("-", "");

        // 4. 插入 pay_order（状态 PENDING）
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(generateId());
        payOrder.setBizType(PayBizType.USER_RECHARGE.getCode());
        payOrder.setOwnerType(PayOwnerType.USER.getCode());
        payOrder.setOwnerId(userBaseId);
        payOrder.setChannel(PayChannel.ALIPAY.getCode());
        payOrder.setTotalAmount(amount);
        payOrder.setStatus(PayOrderStatus.PENDING.getCode());
        payOrder.setOutTradeNo(outTradeNo);
        payOrder.setNotifyStatus(0);            // 0=未处理
        payOrder.setNotifyTimes(0);
        payOrder.setCreateTime(new Date());
        payOrder.setUpdateTime(new Date());
        payOrderMapper.insertPayOrder(payOrder);


        // 5. 插入 user_wallet_record（状态 处理中）
        UserWalletRecord record = new UserWalletRecord();
        record.setUserWalletRecordId(generateId());// 你自己的雪花ID
        record.setUserWalletId(wallet.getUserWalletId());
        record.setUserBaseId(userBaseId);
        record.setAmount(amount);  // 充值：正数
        record.setTradeType(UserWalletTradeType.RECHARGE.getCode());
        record.setTradeStatus(TradeStatus.PROCESSING.getCode());
        record.setTradeTime(new Date());
        record.setRemark("用户支付宝充值，outTradeNo=" + outTradeNo);
        record.setRequestId(requestId);
        record.setPayChannel((long)PayChannel.ALIPAY.getCode());
        record.setOutTradeNo(outTradeNo);
        record.setRelatedId(payOrder.getPayOrderId());
        userWalletRecordMapper.insertForPay(record);

        // 6. 调用支付宝沙箱，拿到 payPageHtml
        String subject = "用户余额充值";
        String totalAmount = amount.setScale(2, RoundingMode.HALF_UP).toPlainString();

        // 你决定不用 returnUrl，可以传 null 或配置里的
        String returnUrl = null; // 或 alipayProperties.getReturnUrl();

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

    private String buildOutTradeNo(Long userBaseId) {
        return "UR" + userBaseId + System.currentTimeMillis();
    }

    private Long generateId(){

        return SnowflakeIdGenerator.getInstance().nextId();
    }

}
