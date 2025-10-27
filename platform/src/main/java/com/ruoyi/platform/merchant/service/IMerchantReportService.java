package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.domain.MerchantReport;
import java.util.List;

/**
 * 商家报表Service接口
 *
 * @author ruoyi
 * @date 2025-10-24
 */
public interface IMerchantReportService
{
    /**
     * 查询商家报表
     *
     * @param reportId 商家报表主键
     * @return 商家报表
     */
    public MerchantReport selectMerchantReportByReportId(Long reportId);

    /**
     * 查询商家报表列表
     *
     * @param merchantReport 商家报表
     * @return 商家报表集合
     */
    public List<MerchantReport> selectMerchantReportList(MerchantReport merchantReport);

    /**
     * 新增商家报表
     *
     * @param merchantReport 商家报表
     * @return 结果
     */
    public int insertMerchantReport(MerchantReport merchantReport);

    /**
     * 修改商家报表
     *
     * @param merchantReport 商家报表
     * @return 结果
     */
    public int updateMerchantReport(MerchantReport merchantReport);

    /**
     * 批量删除商家报表
     *
     * @param reportIds 需要删除的商家报表主键集合
     * @return 结果
     */
    public int deleteMerchantReportByReportIds(Long[] reportIds);

    /**
     * 删除商家报表信息
     *
     * @param reportId 商家报表主键
     * @return 结果
     */
    public int deleteMerchantReportByReportId(Long reportId);
}