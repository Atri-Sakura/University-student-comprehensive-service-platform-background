package com.ruoyi.platform.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.platform.domain.*;
import com.ruoyi.platform.domain.enums.WalletFlowTypeEnum;
import com.ruoyi.platform.mapper.*;
import com.ruoyi.platform.service.IWalletFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包流转服务实现类
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Service
public class WalletFlowServiceImpl implements IWalletFlowService {

    private static final Logger log = LoggerFactory.getLogger(WalletFlowServiceImpl.class);

    /** 平台钱包ID（固定为1） */
    private static final Long PLATFORM_WALLET_ID = 1L;

    @Autowired
    private UserWalletMapper userWalletMapper;

    @Autowired
    private UserWalletRecordMapper userWalletRecordMapper;

    @Autowired
    private MerchantWalletMapper merchantWalletMapper;

    @Autowired
    private MerchantWalletFlowInfoMapper merchantWalletFlowInfoMapper;

    @Autowired
    private RiderWalletMapper riderWalletMapper;

    @Autowired
    private RiderWalletRecordMapper riderWalletRecordMapper;

    @Autowired
    private PlatformWalletMapper platformWalletMapper;

    @Autowired
    private PlatformWalletFlowMapper platformWalletFlowMapper;

    /**
     * 用户支付（钱进入平台钱包并冻结）
     *
     * @param userId 用户ID
     * @param orderMainId 订单ID
     * @param amount 支付金额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userPay(Long userId, Long orderMainId, BigDecimal amount) {
        // 1. 查询用户钱包（加行锁）
        UserWallet userWallet = userWalletMapper.selectUserWalletByUserBaseId(userId);
        if (userWallet == null) {
            throw new ServiceException("用户钱包不存在");
        }

        // 2. 校验余额是否充足
        if (userWallet.getBalance().compareTo(amount) < 0) {
            throw new ServiceException("用户余额不足");
        }

        // 3. 扣减用户余额
        int userResult = userWalletMapper.decreaseBalance(userId, amount);
        if (userResult == 0) {
            throw new ServiceException("用户余额扣减失败");
        }

        // 4. 记录用户钱包流水
        UserWalletRecord userRecord = new UserWalletRecord();
        userRecord.setUserWalletRecordId(generateLongId()); // 【修复】生成主键ID
        userRecord.setUserWalletId(userWallet.getUserWalletId());
        userRecord.setUserBaseId(userId);
        userRecord.setAmount(amount.negate()); // 负数表示支出
        userRecord.setTradeType(3L); // 3-外卖支付
        userRecord.setRelatedId(orderMainId);
        userRecord.setTradeStatus(1L); // 1-成功
        userRecord.setTradeTime(new Date());
        userRecord.setRemark("订单支付");
        userWalletRecordMapper.insertUserWalletRecord(userRecord);

        // 5. 查询平台钱包（加行锁）
        PlatformWallet platformWallet = platformWalletMapper.selectPlatformWalletForUpdate(PLATFORM_WALLET_ID);
        if (platformWallet == null) {
            throw new ServiceException("平台钱包不存在");
        }

        BigDecimal balanceBefore = platformWallet.getBalance();
        BigDecimal freezeBefore = platformWallet.getFreezeAmount();

        // 6. 平台钱包增加余额和冻结金额
        int platformResult = platformWalletMapper.increaseBalanceAndFreeze(PLATFORM_WALLET_ID, amount);
        if (platformResult == 0) {
            throw new ServiceException("平台钱包更新失败");
        }

        // 7. 记录平台钱包流水
        PlatformWalletFlow platformFlow = new PlatformWalletFlow();
        platformFlow.setFlowId(generateLongId()); // 【修复】生成主键ID
        platformFlow.setOrderMainId(orderMainId);
        platformFlow.setFlowType(WalletFlowTypeEnum.USER_PAY.getCode());
        platformFlow.setFlowAmount(amount);
        platformFlow.setBalanceBefore(balanceBefore);
        platformFlow.setBalanceAfter(balanceBefore.add(amount));
        platformFlow.setFreezeBefore(freezeBefore);
        platformFlow.setFreezeAfter(freezeBefore.add(amount));
        platformFlow.setDescription("用户支付订单");
        platformWalletFlowMapper.insertPlatformWalletFlow(platformFlow);

        log.info("用户支付成功，用户ID：{}，订单ID：{}，金额：{}", userId, orderMainId, amount);
    }

    /**
     * 结算给商家（商品金额）
     *
     * @param merchantId 商家ID
     * @param orderMainId 订单ID
     * @param goodsAmount 商品金额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settleMerchant(Long merchantId, Long orderMainId, BigDecimal goodsAmount) {
        // 1. 查询平台钱包（加行锁）
        PlatformWallet platformWallet = platformWalletMapper.selectPlatformWalletForUpdate(PLATFORM_WALLET_ID);
        if (platformWallet == null) {
            throw new ServiceException("平台钱包不存在");
        }

        BigDecimal balanceBefore = platformWallet.getBalance();
        BigDecimal freezeBefore = platformWallet.getFreezeAmount();

        // 2. 校验平台钱包余额和冻结金额是否充足
        if (platformWallet.getBalance().compareTo(goodsAmount) < 0 ||
                platformWallet.getFreezeAmount().compareTo(goodsAmount) < 0) {
            throw new ServiceException("平台钱包余额不足");
        }

        // 3. 平台钱包扣减余额和冻结金额
        int platformResult = platformWalletMapper.decreaseBalanceAndFreeze(PLATFORM_WALLET_ID, goodsAmount);
        if (platformResult == 0) {
            throw new ServiceException("平台钱包扣减失败");
        }

        // 4. 记录平台钱包流水
        PlatformWalletFlow platformFlow = new PlatformWalletFlow();
        platformFlow.setFlowId(generateLongId()); // 【修复】生成主键ID
        platformFlow.setOrderMainId(orderMainId);
        platformFlow.setFlowType(WalletFlowTypeEnum.MERCHANT_SETTLE.getCode());
        platformFlow.setFlowAmount(goodsAmount.negate()); // 负数表示支出
        platformFlow.setBalanceBefore(balanceBefore);
        platformFlow.setBalanceAfter(balanceBefore.subtract(goodsAmount));
        platformFlow.setFreezeBefore(freezeBefore);
        platformFlow.setFreezeAfter(freezeBefore.subtract(goodsAmount));
        platformFlow.setDescription("结算给商家");
        platformWalletFlowMapper.insertPlatformWalletFlow(platformFlow);

        // 5. 查询商家钱包
        MerchantWallet merchantWallet = merchantWalletMapper.selectMerchantWalletByMerchantId(merchantId);
        if (merchantWallet == null) {
            throw new ServiceException("商家钱包不存在");
        }

        // 6. 增加商家余额
        int merchantResult = merchantWalletMapper.increaseBalance(merchantId, goodsAmount);
        if (merchantResult == 0) {
            throw new ServiceException("商家钱包更新失败");
        }

        // 7. 记录商家钱包流水
        MerchantWalletFlow merchantFlow = new MerchantWalletFlow();
        merchantFlow.setFlowId(generateLongId()); // 【修复】生成主键ID
        merchantFlow.setMerchantBaseId(merchantId);
        merchantFlow.setOrderMainId(orderMainId);
        merchantFlow.setFlowType("INCOME");
        merchantFlow.setFlowAmount(goodsAmount);
        merchantFlow.setGoodsAmount(goodsAmount);
        merchantFlow.setBalanceAfter(merchantWallet.getBalance().add(goodsAmount));
        merchantFlow.setDescription("订单商品收入");
        merchantWalletFlowInfoMapper.insertMerchantWalletFlow(merchantFlow);

        log.info("商家结算成功，商家ID：{}，订单ID：{}，金额：{}", merchantId, orderMainId, goodsAmount);
    }

    /**
     * 结算给骑手（配送费）
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @param deliveryFee 配送费
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void settleRider(Long riderId, Long orderMainId, BigDecimal deliveryFee) {
        // 1. 查询平台钱包（加行锁）
        PlatformWallet platformWallet = platformWalletMapper.selectPlatformWalletForUpdate(PLATFORM_WALLET_ID);
        if (platformWallet == null) {
            throw new ServiceException("平台钱包不存在");
        }

        BigDecimal balanceBefore = platformWallet.getBalance();
        BigDecimal freezeBefore = platformWallet.getFreezeAmount();

        // 2. 校验平台钱包余额和冻结金额是否充足
        if (platformWallet.getBalance().compareTo(deliveryFee) < 0 ||
                platformWallet.getFreezeAmount().compareTo(deliveryFee) < 0) {
            throw new ServiceException("平台钱包余额不足");
        }

        // 3. 平台钱包扣减余额和冻结金额
        int platformResult = platformWalletMapper.decreaseBalanceAndFreeze(PLATFORM_WALLET_ID, deliveryFee);
        if (platformResult == 0) {
            throw new ServiceException("平台钱包扣减失败");
        }

        // 4. 记录平台钱包流水
        PlatformWalletFlow platformFlow = new PlatformWalletFlow();
        platformFlow.setFlowId(generateLongId()); // 【修复】生成主键ID
        platformFlow.setOrderMainId(orderMainId);
        platformFlow.setFlowType(WalletFlowTypeEnum.RIDER_SETTLE.getCode());
        platformFlow.setFlowAmount(deliveryFee.negate()); // 负数表示支出
        platformFlow.setBalanceBefore(balanceBefore);
        platformFlow.setBalanceAfter(balanceBefore.subtract(deliveryFee));
        platformFlow.setFreezeBefore(freezeBefore);
        platformFlow.setFreezeAfter(freezeBefore.subtract(deliveryFee));
        platformFlow.setDescription("结算给骑手");
        platformWalletFlowMapper.insertPlatformWalletFlow(platformFlow);

        // 5. 查询骑手钱包
        RiderWallet riderWallet = riderWalletMapper.selectRiderWalletByRiderBaseId(riderId);
        if (riderWallet == null) {
            throw new ServiceException("骑手钱包不存在");
        }

        // 6. 增加骑手余额
        int riderResult = riderWalletMapper.increaseBalance(riderId, deliveryFee);
        if (riderResult == 0) {
            throw new ServiceException("骑手钱包更新失败");
        }

        // 7. 记录骑手钱包流水
        RiderWalletRecord riderRecord = new RiderWalletRecord();
        riderRecord.setRiderWalletRecordId(generateLongId()); // 【修复】生成主键ID
        riderRecord.setRiderWalletId(riderWallet.getRiderWalletId());
        riderRecord.setRiderBaseId(riderId);
        riderRecord.setAmount(deliveryFee);
        riderRecord.setDeliveryFee(deliveryFee);
        riderRecord.setTradeType(1L); // 1-配送收入
        riderRecord.setRelatedId(orderMainId);
        riderRecord.setTradeStatus(1L); // 1-成功
        riderRecord.setTradeTime(new Date());
        riderRecord.setRemark("订单配送收入");
        riderWalletRecordMapper.insertRiderWalletRecord(riderRecord);

        log.info("骑手结算成功，骑手ID：{}，订单ID：{}，金额：{}", riderId, orderMainId, deliveryFee);
    }

    /**
     * 退款给用户
     *
     * @param userId 用户ID
     * @param orderMainId 订单ID
     * @param refundAmount 退款金额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundUser(Long userId, Long orderMainId, BigDecimal refundAmount) {
        // 1. 查询平台钱包（加行锁）
        PlatformWallet platformWallet = platformWalletMapper.selectPlatformWalletForUpdate(PLATFORM_WALLET_ID);
        if (platformWallet == null) {
            throw new ServiceException("平台钱包不存在");
        }

        BigDecimal balanceBefore = platformWallet.getBalance();
        BigDecimal freezeBefore = platformWallet.getFreezeAmount();

        // 2. 校验平台钱包余额和冻结金额是否充足
        if (platformWallet.getBalance().compareTo(refundAmount) < 0 ||
                platformWallet.getFreezeAmount().compareTo(refundAmount) < 0) {
            throw new ServiceException("平台钱包余额不足");
        }

        // 3. 平台钱包扣减余额和冻结金额
        int platformResult = platformWalletMapper.decreaseBalanceAndFreeze(PLATFORM_WALLET_ID, refundAmount);
        if (platformResult == 0) {
            throw new ServiceException("平台钱包扣减失败");
        }

        // 4. 记录平台钱包流水
        PlatformWalletFlow platformFlow = new PlatformWalletFlow();
        platformFlow.setFlowId(generateLongId()); // 【修复】生成主键ID
        platformFlow.setOrderMainId(orderMainId);
        platformFlow.setFlowType(WalletFlowTypeEnum.REFUND.getCode());
        platformFlow.setFlowAmount(refundAmount.negate()); // 负数表示支出
        platformFlow.setBalanceBefore(balanceBefore);
        platformFlow.setBalanceAfter(balanceBefore.subtract(refundAmount));
        platformFlow.setFreezeBefore(freezeBefore);
        platformFlow.setFreezeAfter(freezeBefore.subtract(refundAmount));
        platformFlow.setDescription("退款给用户");
        platformWalletFlowMapper.insertPlatformWalletFlow(platformFlow);

        // 5. 查询用户钱包
        UserWallet userWallet = userWalletMapper.selectUserWalletByUserBaseId(userId);
        if (userWallet == null) {
            throw new ServiceException("用户钱包不存在");
        }

        // 6. 增加用户余额
        int userResult = userWalletMapper.increaseBalance(userId, refundAmount);
        if (userResult == 0) {
            throw new ServiceException("用户钱包更新失败");
        }

        // 7. 记录用户钱包流水
        UserWalletRecord userRecord = new UserWalletRecord();
        userRecord.setUserWalletRecordId(generateLongId()); // 【修复】生成主键ID
        userRecord.setUserWalletId(userWallet.getUserWalletId());
        userRecord.setUserBaseId(userId);
        userRecord.setAmount(refundAmount); // 正数表示收入
        userRecord.setTradeType(5L); // 5-退款
        userRecord.setRelatedId(orderMainId);
        userRecord.setTradeStatus(1L); // 1-成功
        userRecord.setTradeTime(new Date());
        userRecord.setRemark("订单退款");
        userWalletRecordMapper.insertUserWalletRecord(userRecord);

        log.info("用户退款成功，用户ID：{}，订单ID：{}，金额：{}", userId, orderMainId, refundAmount);
    }

    /**
     * 生成Long类型的唯一ID
     * 使用 IdUtils.fastSimpleUUID() 生成UUID字符串，然后转换为Long
     *
     * @return Long类型的唯一ID
     */
    private Long generateLongId() {
        // 获取UUID字符串（32位，去掉横线）
        String uuid = IdUtils.fastSimpleUUID();

        // 取UUID的前15位字符（而不是16位），避免超出Long.MAX_VALUE
        // Long.MAX_VALUE = 9223372036854775807 (19位十进制数)
        // 15位16进制 = 最多60位二进制，在Long范围内
        String hexString = uuid.substring(0, 15);

        try {
            // 将16进制字符串转换为Long
            return Long.parseLong(hexString, 16);
        } catch (NumberFormatException e) {
            // 如果转换失败，使用时间戳 + 随机数作为备用方案
            return System.currentTimeMillis() * 1000 + (long)(Math.random() * 1000);
        }
    }
}