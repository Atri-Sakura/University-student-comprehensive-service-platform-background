package com.ruoyi.platform.merchant.service.impl;

import java.util.List;

import com.ruoyi.platform.merchant.mapper.MerchantReportMapper;
import com.ruoyi.platform.merchant.service.IMerchantReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.platform.domain.MerchantReport;


/**
 * 商家报表Service实现类
 *
 * @author ruoyi
 * @date 2025-10-24
 */
@Service
public class MerchantReportServiceImpl implements IMerchantReportService
{
    @Autowired
    private MerchantReportMapper merchantReportMapper;

    /**
     * 查询商家报表
     *
     * @param reportId 商家报表主键
     * @return 商家报表
     */
    @Override
    public MerchantReport selectMerchantReportByReportId(Long reportId)
    {
        return merchantReportMapper.selectMerchantReportByReportId(reportId);
    }

    /**
     * 查询商家报表列表
     *
     * @param merchantReport 商家报表
     * @return 商家报表集合
     */
    @Override
    public List<MerchantReport> selectMerchantReportList(MerchantReport merchantReport)
    {
        return merchantReportMapper.selectMerchantReportList(merchantReport);
    }

    /**
     * 新增商家报表
     *
     * @param merchantReport 商家报表
     * @return 结果
     */
    @Override
    public int insertMerchantReport(MerchantReport merchantReport)
    {
        return merchantReportMapper.insertMerchantReport(merchantReport);
    }

    /**
     * 修改商家报表
     *
     * @param merchantReport 商家报表
     * @return 结果
     */
    @Override
    public int updateMerchantReport(MerchantReport merchantReport)
    {
        return merchantReportMapper.updateMerchantReport(merchantReport);
    }

    /**
     * 批量删除商家报表
     *
     * @param reportIds 需要删除的商家报表主键集合
     * @return 结果
     */
    @Override
    public int deleteMerchantReportByReportIds(Long[] reportIds)
    {
        return merchantReportMapper.deleteMerchantReportByReportIds(reportIds);
    }

    /**
     * 删除商家报表信息
     *
     * @param reportId 商家报表主键
     * @return 结果
     */
    @Override
    public int deleteMerchantReportByReportId(Long reportId)
    {
        return merchantReportMapper.deleteMerchantReportByReportId(reportId);
    }
}