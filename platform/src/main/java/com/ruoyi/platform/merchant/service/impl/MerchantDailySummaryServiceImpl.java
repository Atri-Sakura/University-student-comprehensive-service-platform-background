package com.ruoyi.platform.merchant.service.impl;


import com.ruoyi.platform.domain.MerchantDailySummary;
import com.ruoyi.platform.merchant.mapper.MerchantDailySummaryMapper;
import com.ruoyi.platform.merchant.service.IMerchantDailySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商家每日收入汇总 Service 业务层实现
 *
 * 封装商家每日收入汇总的增删改查业务逻辑。
 *
 * @author Jinx
 * @date 2025-10-24
 */
@Service
public class MerchantDailySummaryServiceImpl implements IMerchantDailySummaryService {
    @Autowired
    private MerchantDailySummaryMapper merchantDailySummaryMapper;

    /**
     * 查询商家每日收入汇总
     *
     * @param id 汇总记录ID
     * @return 商家每日收入汇总
     */
    @Override
    public MerchantDailySummary selectMerchantDailySummaryById(Long id)
    {
        return merchantDailySummaryMapper.selectMerchantDailySummaryById(id);
    }

    /**
     * 查询商家每日收入汇总列表
     *
     * @param summary 查询条件
     * @return 商家每日收入汇总集合
     */
    @Override
    public List<MerchantDailySummary> selectMerchantDailySummaryList(MerchantDailySummary summary)
    {
        return merchantDailySummaryMapper.selectMerchantDailySummaryList(summary);
    }

    /**
     * 新增商家每日收入汇总
     *
     * @param summary 数据对象
     * @return 结果
     */
    @Override
    public int insertMerchantDailySummary(MerchantDailySummary summary)
    {
        return merchantDailySummaryMapper.insertMerchantDailySummary(summary);
    }

    /**
     * 修改商家每日收入汇总
     *
     * @param summary 数据对象
     * @return 结果
     */
    @Override
    public int updateMerchantDailySummary(MerchantDailySummary summary)
    {
        return merchantDailySummaryMapper.updateMerchantDailySummary(summary);
    }

    /**
     * 批量删除商家每日收入汇总
     *
     * @param ids 要删除的ID数组
     * @return 结果
     */
    @Override
    public int deleteMerchantDailySummaryByIds(Long[] ids)
    {
        return merchantDailySummaryMapper.deleteMerchantDailySummaryByIds(ids);
    }

    /**
     * 删除单个商家每日收入汇总
     *
     * @param id 汇总记录ID
     * @return 结果
     */
    @Override
    public int deleteMerchantDailySummaryById(Long id)
    {
        return merchantDailySummaryMapper.deleteMerchantDailySummaryById(id);
    }
}
