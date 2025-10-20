package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.MerchantActivity;

/**
 * 商家活动Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IMerchantActivityService 
{
    /**
     * 查询商家活动
     * 
     * @param merchantActivityId 商家活动主键
     * @return 商家活动
     */
    public MerchantActivity selectMerchantActivityByMerchantActivityId(Long merchantActivityId);

    /**
     * 查询商家活动列表
     * 
     * @param merchantActivity 商家活动
     * @return 商家活动集合
     */
    public List<MerchantActivity> selectMerchantActivityList(MerchantActivity merchantActivity);

    /**
     * 新增商家活动
     * 
     * @param merchantActivity 商家活动
     * @return 结果
     */
    public int insertMerchantActivity(MerchantActivity merchantActivity);

    /**
     * 修改商家活动
     * 
     * @param merchantActivity 商家活动
     * @return 结果
     */
    public int updateMerchantActivity(MerchantActivity merchantActivity);

    /**
     * 批量删除商家活动
     * 
     * @param merchantActivityIds 需要删除的商家活动主键集合
     * @return 结果
     */
    public int deleteMerchantActivityByMerchantActivityIds(Long[] merchantActivityIds);

    /**
     * 删除商家活动信息
     * 
     * @param merchantActivityId 商家活动主键
     * @return 结果
     */
    public int deleteMerchantActivityByMerchantActivityId(Long merchantActivityId);
}
