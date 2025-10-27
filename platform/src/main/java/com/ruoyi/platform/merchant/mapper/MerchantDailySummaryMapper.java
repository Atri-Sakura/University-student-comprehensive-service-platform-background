package com.ruoyi.platform.merchant.mapper;

import com.ruoyi.platform.domain.MerchantDailySummary;
import io.lettuce.core.dynamic.annotation.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商家每日收入汇总 Mapper 接口
 *
 * @author Jinx
 * @date 2025-10-24
 */
public interface MerchantDailySummaryMapper {

    /**
     * 查询商家今日收入
     *
     * @param merchantBaseId 商家ID
     * @param summaryDate    统计日期
     * @return 当日收入
     */
    BigDecimal selectTodayIncome(@Param("merchantBaseId") Long merchantBaseId,
                                 @Param("summaryDate") String summaryDate);
    /**
     * 根据ID查询商家每日收入汇总
     *
     * @param id 商家每日收入汇总ID
     * @return 商家每日收入汇总
     */
    MerchantDailySummary selectMerchantDailySummaryById(Long id);

    /**
     * 查询商家每日收入汇总列表
     *
     * @param summary 商家每日收入汇总
     * @return 商家每日收入汇总集合
     */
    List<MerchantDailySummary> selectMerchantDailySummaryList(MerchantDailySummary summary);

    /**
     * 新增商家每日收入汇总
     *
     * @param summary 商家每日收入汇总
     * @return 结果
     */
    int insertMerchantDailySummary(MerchantDailySummary summary);

    /**
     * 修改商家每日收入汇总
     *
     * @param summary 商家每日收入汇总
     * @return 结果
     */
    int updateMerchantDailySummary(MerchantDailySummary summary);

    /**
     * 删除商家每日收入汇总
     *
     * @param id 商家每日收入汇总ID
     * @return 结果
     */
    int deleteMerchantDailySummaryById(Long id);

    /**
     * 批量删除商家每日收入汇总
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteMerchantDailySummaryByIds(Long[] ids);
}
