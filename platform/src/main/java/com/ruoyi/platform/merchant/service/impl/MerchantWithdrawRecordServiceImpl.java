package com.ruoyi.platform.merchant.service.impl;

import com.ruoyi.platform.domain.MerchantWallet;
import com.ruoyi.platform.domain.MerchantWithdrawRecord;
import com.ruoyi.platform.domain.vo.MerchantWithdrawOverviewVO;
import com.ruoyi.platform.mapper.MerchantWalletMapper;
import com.ruoyi.platform.merchant.mapper.MerchantDailySummaryMapper;
import com.ruoyi.platform.merchant.mapper.MerchantWithdrawRecordMapper;
import com.ruoyi.platform.merchant.service.IMerchantWithdrawRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
}
