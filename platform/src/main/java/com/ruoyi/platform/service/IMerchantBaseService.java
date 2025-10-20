package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.MerchantBase;

/**
 * 商家基础信息Service接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IMerchantBaseService 
{
    /**
     * 查询商家基础信息
     * 
     * @param merchantBaseId 商家基础信息主键
     * @return 商家基础信息
     */
    public MerchantBase selectMerchantBaseByMerchantBaseId(Long merchantBaseId);

    /**
     * 查询商家基础信息列表
     * 
     * @param merchantBase 商家基础信息
     * @return 商家基础信息集合
     */
    public List<MerchantBase> selectMerchantBaseList(MerchantBase merchantBase);

    /**
     * 新增商家基础信息
     * 
     * @param merchantBase 商家基础信息
     * @return 结果
     */
    public int insertMerchantBase(MerchantBase merchantBase);

    /**
     * 修改商家基础信息
     * 
     * @param merchantBase 商家基础信息
     * @return 结果
     */
    public int updateMerchantBase(MerchantBase merchantBase);

    /**
     * 批量删除商家基础信息
     * 
     * @param merchantBaseIds 需要删除的商家基础信息主键集合
     * @return 结果
     */
    public int deleteMerchantBaseByMerchantBaseIds(Long[] merchantBaseIds);

    /**
     * 删除商家基础信息信息
     * 
     * @param merchantBaseId 商家基础信息主键
     * @return 结果
     */
    public int deleteMerchantBaseByMerchantBaseId(Long merchantBaseId);
}
