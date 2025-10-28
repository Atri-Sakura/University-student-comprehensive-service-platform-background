package com.ruoyi.platform.merchant.service;


import com.github.pagehelper.PageInfo;
import com.ruoyi.platform.domain.MerchantWithdrawRecord;
import com.ruoyi.platform.domain.dto.WithdrawApplyDTO;
import com.ruoyi.platform.domain.vo.MerchantWithdrawOverviewVO;
import com.ruoyi.platform.domain.vo.MerchantWithdrawRecordVO;
import com.ruoyi.platform.domain.vo.WithdrawApplyResultVO;

import java.util.List;

/**
 * 商家提现记录 Service 接口
 *
 * 定义商家提现记录的业务逻辑操作接口，包括查询、插入、修改与删除等。
 *
 * @author Jinx
 * @date 2025-10-24
 */
public interface IMerchantWithdrawRecordService {
    /**
     * 获取提现页概览信息
     *
     * @param merchantBaseId 商家ID
     * @return 概览信息
     */
    MerchantWithdrawOverviewVO getWithdrawOverview(Long merchantBaseId);
    /**
     * 查询商家提现记录
     *
     * @param withdrawId 提现记录ID
     * @return 商家提现记录
     */
    MerchantWithdrawRecord selectMerchantWithdrawRecordById(Long withdrawId);

    /**
     * 查询商家提现记录列表
     *
     * @param record 查询条件
     * @return 商家提现记录集合
     */
    List<MerchantWithdrawRecord> selectMerchantWithdrawRecordList(MerchantWithdrawRecord record);

    /**
     * 新增商家提现记录
     *
     * @param record 提现记录对象
     * @return 结果
     */
    int insertMerchantWithdrawRecord(MerchantWithdrawRecord record);

    /**
     * 修改商家提现记录
     *
     * @param record 提现记录对象
     * @return 结果
     */
    int updateMerchantWithdrawRecord(MerchantWithdrawRecord record);

    /**
     * 批量删除商家提现记录
     *
     * @param withdrawIds 要删除的提现记录ID集合
     * @return 结果
     */
    int deleteMerchantWithdrawRecordByIds(Long[] withdrawIds);

    /**
     * 删除单个商家提现记录
     *
     * @param withdrawId 提现记录ID
     * @return 结果
     */
    int deleteMerchantWithdrawRecordById(Long withdrawId);

    /**
     * 提现申请
     *
     * @param merchantBaseId 商家ID
     * @param withdrawApplyDTO 提现申请参数
     * @return 提现申请结果
     */
    WithdrawApplyResultVO applyWithdraw(Long merchantBaseId, WithdrawApplyDTO withdrawApplyDTO);

    /**
     * 根据提现记录ID查询提现记录
     *
     * @param withdrawId 提现记录ID
     * @return 提现记录
     */
    MerchantWithdrawRecord selectWithdrawRecordById(Long withdrawId);

    /**
     * 获取商家提现记录列表
     *
     * @param merchantBaseId 商家ID
     * @param status 提现状态
     * @return 提现中总金额
     */
    List<MerchantWithdrawRecordVO> selectWithdrawRecordVOList(Long merchantBaseId, String status);

}
