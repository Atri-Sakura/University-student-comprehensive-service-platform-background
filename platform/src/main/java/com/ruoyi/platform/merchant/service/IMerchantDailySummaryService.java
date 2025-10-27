package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.domain.MerchantDailySummary;

import java.util.List;

/**
 * 商家每日收入汇总 Service 接口
 *
 * 定义商家每日收入汇总的业务逻辑操作接口，包括查询、插入、修改与删除。
 *
 * @author Jinx
 * @date 2025-10-24
 */
public interface IMerchantDailySummaryService {

    /**
     * 查询商家每日收入汇总
     *
     * @param id 汇总记录ID
     * @return 商家每日收入汇总
     */
    MerchantDailySummary selectMerchantDailySummaryById(Long id);

    /**
     * 查询商家每日收入汇总列表
     *
     * @param summary 查询条件
     * @return 商家每日收入汇总集合
     */
    List<MerchantDailySummary> selectMerchantDailySummaryList(MerchantDailySummary summary);

    /**
     * 新增商家每日收入汇总
     *
     * @param summary 商家每日收入汇总对象
     * @return 结果
     */
    int insertMerchantDailySummary(MerchantDailySummary summary);

    /**
     * 修改商家每日收入汇总
     *
     * @param summary 商家每日收入汇总对象
     * @return 结果
     */
    int updateMerchantDailySummary(MerchantDailySummary summary);

    /**
     * 批量删除商家每日收入汇总
     *
     * @param ids 要删除的汇总记录ID集合
     * @return 结果
     */
    int deleteMerchantDailySummaryByIds(Long[] ids);

    /**
     * 删除单个商家每日收入汇总
     *
     * @param id 汇总记录ID
     * @return 结果
     */
    int deleteMerchantDailySummaryById(Long id);
}
