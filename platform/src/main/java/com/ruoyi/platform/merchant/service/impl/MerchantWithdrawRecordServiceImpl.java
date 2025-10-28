package com.ruoyi.platform.merchant.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.platform.domain.MerchantWallet;
import com.ruoyi.platform.domain.MerchantWalletFlow;
import com.ruoyi.platform.domain.MerchantWithdrawRecord;
import com.ruoyi.platform.domain.dto.WithdrawApplyDTO;
import com.ruoyi.platform.domain.vo.MerchantWithdrawOverviewVO;
import com.ruoyi.platform.domain.vo.MerchantWithdrawRecordVO;
import com.ruoyi.platform.domain.vo.WithdrawApplyResultVO;
import com.ruoyi.platform.mapper.MerchantWalletMapper;
import com.ruoyi.platform.merchant.mapper.MerchantDailySummaryMapper;
import com.ruoyi.platform.merchant.mapper.MerchantWalletFlowMapper;
import com.ruoyi.platform.merchant.mapper.MerchantWithdrawRecordMapper;
import com.ruoyi.platform.merchant.payment.PaymentGatewayClient;
import com.ruoyi.platform.merchant.service.IMerchantWithdrawRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 商家提现记录 Service 业务层实现
 *
 * 封装商家提现记录的具体业务逻辑，包括数据查询、插入与更新操作。
 *
 * @author Jinx
 * @date 2025-10-24
 */
@Service
public class MerchantWithdrawRecordServiceImpl implements IMerchantWithdrawRecordService {
    @Autowired
    private MerchantWalletMapper merchantWalletMapper;

    @Autowired
    private MerchantDailySummaryMapper merchantDailySummaryMapper;
    @Autowired
    private MerchantWithdrawRecordMapper merchantWithdrawRecordMapper;

    @Autowired
    private MerchantWalletFlowMapper merchantWalletFlowMapper;

    @Autowired
    private PaymentGatewayClient paymentGatewayClient;
    /**
     * 提现申请实现（冻结模型 + 幂等）
     * 可加redis并发锁避免多进程安全问题
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WithdrawApplyResultVO applyWithdraw(Long merchantId,WithdrawApplyDTO dto) {

        // === 1. 幂等校验 ===
        MerchantWithdrawRecord exist = merchantWithdrawRecordMapper
                .selectByMerchantAndKey(merchantId, dto.getIdempotentKey());
        if (exist != null) {
            // 已存在同幂等Key的提现记录，直接返回
            WithdrawApplyResultVO vo = new WithdrawApplyResultVO();
            vo.setWithdrawId(exist.getWithdrawId());
            vo.setWithdrawStatus(exist.getWithdrawStatus());
            vo.setMessage("重复提现请求已忽略");
            return vo;
        }
        // === 2. 锁定钱包记录 ===
        MerchantWallet wallet = merchantWalletMapper.selectWalletForUpdate(merchantId);
        if (wallet == null) {
            throw new ServiceException("商家钱包不存在");
        }

        BigDecimal amount = dto.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("提现金额必须大于0");
        }
        // === 3. 手续费计算（可配置，这里固定1%示例） ===
        BigDecimal fee = amount.multiply(new BigDecimal("0.01"));
        BigDecimal total = amount.add(fee);

        if (wallet.getBalance().compareTo(total) < 0) {
            throw new ServiceException("可用余额不足");
        }

        // === 4. 扣减余额 & 增加冻结金额 ===
        merchantWalletMapper.updateBalanceAndFreeze(merchantId, total);
        // === 5. 生成提现记录 ===
        MerchantWithdrawRecord record = new MerchantWithdrawRecord();
        record.setMerchantBaseId(merchantId);
        record.setAccountId(dto.getAccountId());
        record.setWithdrawAmount(amount);
        record.setFeeAmount(fee);
        record.setActualAmount(amount);
        record.setWithdrawStatus("PENDING");
        record.setRequestTime(new Date());
        record.setIdempotentKey(dto.getIdempotentKey());
        merchantWithdrawRecordMapper.insertMerchantWithdrawRecord(record);

        // === 6. 写钱包流水 ===
        merchantWalletFlowMapper.insertWithdrawFreezeFlow(merchantId, record.getWithdrawId(), total);
        //待接入：调用第三方提现接口
        CompletableFuture.runAsync(() -> processWithdraw(record));

        // === 7. 构造返回对象 === 待优化这里只是扣除账户余额还未进行提现转发
        WithdrawApplyResultVO vo = new WithdrawApplyResultVO();
        vo.setWithdrawId(record.getWithdrawId());
        vo.setWithdrawStatus("PENDING");
        vo.setMessage("提现申请成功，系统处理中");
        return vo;
    }
    /**
     * 提现处理（异步）
     *
     * @param record 提现记录
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processWithdraw(MerchantWithdrawRecord record) {
        boolean success = paymentGatewayClient.transfer(record);
        if (success) {
            // ✅ 出款成功
            merchantWithdrawRecordMapper.updateStatus(record.getWithdrawId(), "SUCCESS", null);
            merchantWalletMapper.decreaseFreeze(record.getMerchantBaseId(), record.getWithdrawAmount().add(record.getFeeAmount()));
            merchantWalletFlowMapper.insertWithdrawSuccessFlow(record);
        } else {
            // ❌ 出款失败（回滚冻结）
            merchantWithdrawRecordMapper.updateStatus(record.getWithdrawId(), "FAILED", "支付网关处理失败");
            merchantWalletMapper.rollbackFreeze(record.getMerchantBaseId(), record.getWithdrawAmount().add(record.getFeeAmount()));
            merchantWalletFlowMapper.insertWithdrawFailFlow(record);
        }
    }

    /**
     * 提现页概览信息
     */
    @Override
    public List<MerchantWithdrawRecordVO> selectWithdrawRecordVOList(Long merchantId,String status){
//
        List<MerchantWithdrawRecordVO> list = merchantWithdrawRecordMapper.selectWithdrawRecordVOList(merchantId,status);
        if (CollectionUtils.isEmpty(list)) {
            list = Collections.emptyList();
        }
        return list;
    }
    /**
     * 获取提现页概览信息
     *
     * @param merchantBaseId 商家ID
     * @return 概览信息
     */
    @Override
    public MerchantWithdrawOverviewVO getWithdrawOverview(Long merchantBaseId) {
        MerchantWallet wallet = merchantWalletMapper.selectMerchantWalletByMerchantId(merchantBaseId);
        if (wallet == null) {
            return null;
        }

        BigDecimal withdrawingAmount = merchantWithdrawRecordMapper.selectWithdrawingAmount(merchantBaseId);

        // 可配置的参数，可来自配置文件或常量表
        BigDecimal feeRate = new BigDecimal("0.005");
        BigDecimal minWithdraw = new BigDecimal("10.00");
        BigDecimal maxWithdraw = new BigDecimal("5000.00");

        // 查询今日收入（来自汇总表）
        String todayStr = LocalDate.now().toString();
        BigDecimal todayIncome = merchantDailySummaryMapper.selectTodayIncome(merchantBaseId, todayStr);
        if (todayIncome == null) todayIncome = BigDecimal.ZERO;

        MerchantWithdrawOverviewVO vo = new MerchantWithdrawOverviewVO();
        vo.setAvailableBalance(wallet.getBalance());
        vo.setSettlingAmount(wallet.getFreezeAmount());
        vo.setWithdrawingAmount(withdrawingAmount);
        vo.setFeeRate(feeRate);
        vo.setMinWithdraw(minWithdraw);
        vo.setMaxWithdraw(maxWithdraw);
        vo.setTodayIncome(todayIncome);
        return vo;
    }
    /**
     * 查询商家提现记录
     *
     * @param withdrawId 提现记录ID
     * @return 商家提现记录
     */
    @Override
    public MerchantWithdrawRecord selectMerchantWithdrawRecordById(Long withdrawId)
    {
        return merchantWithdrawRecordMapper.selectMerchantWithdrawRecordById(withdrawId);
    }

    /**
     * 查询商家提现记录列表
     *
     * @param record 查询条件
     * @return 商家提现记录集合
     */
    @Override
    public List<MerchantWithdrawRecord> selectMerchantWithdrawRecordList(MerchantWithdrawRecord record)
    {
        return merchantWithdrawRecordMapper.selectMerchantWithdrawRecordList(record);
    }

    /**
     * 新增商家提现记录
     *
     * @param record 数据对象
     * @return 结果
     */
    @Override
    public int insertMerchantWithdrawRecord(MerchantWithdrawRecord record)
    {
        return merchantWithdrawRecordMapper.insertMerchantWithdrawRecord(record);
    }

    /**
     * 修改商家提现记录
     *
     * @param record 数据对象
     * @return 结果
     */
    @Override
    public int updateMerchantWithdrawRecord(MerchantWithdrawRecord record)
    {
        return merchantWithdrawRecordMapper.updateMerchantWithdrawRecord(record);
    }

    /**
     * 批量删除商家提现记录
     *
     * @param withdrawIds 要删除的ID数组
     * @return 结果
     */
    @Override
    public int deleteMerchantWithdrawRecordByIds(Long[] withdrawIds)
    {
        return merchantWithdrawRecordMapper.deleteMerchantWithdrawRecordByIds(withdrawIds);
    }

    /**
     * 删除单个商家提现记录
     *
     * @param withdrawId 提现记录ID
     * @return 结果
     */
    @Override
    public int deleteMerchantWithdrawRecordById(Long withdrawId)
    {
        return merchantWithdrawRecordMapper.deleteMerchantWithdrawRecordById(withdrawId);

    }

    /**
     * 根据ID查询提现记录
     *
     * @param withdrawId 提现记录ID
     * @return 提现记录
     */
    @Override
    public MerchantWithdrawRecord selectWithdrawRecordById(Long withdrawId) {
        return merchantWithdrawRecordMapper.selectWithdrawRecordById(withdrawId);
    }

}
