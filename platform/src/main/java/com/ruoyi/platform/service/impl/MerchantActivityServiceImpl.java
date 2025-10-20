package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.MerchantActivityMapper;
import com.ruoyi.platform.domain.MerchantActivity;
import com.ruoyi.platform.service.IMerchantActivityService;

/**
 * 商家活动Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class MerchantActivityServiceImpl implements IMerchantActivityService 
{
    @Autowired
    private MerchantActivityMapper merchantActivityMapper;

    /**
     * 查询商家活动
     * 
     * @param merchantActivityId 商家活动主键
     * @return 商家活动
     */
    @Override
    public MerchantActivity selectMerchantActivityByMerchantActivityId(Long merchantActivityId)
    {
        return merchantActivityMapper.selectMerchantActivityByMerchantActivityId(merchantActivityId);
    }

    /**
     * 查询商家活动列表
     * 
     * @param merchantActivity 商家活动
     * @return 商家活动
     */
    @Override
    public List<MerchantActivity> selectMerchantActivityList(MerchantActivity merchantActivity)
    {
        return merchantActivityMapper.selectMerchantActivityList(merchantActivity);
    }

    /**
     * 新增商家活动
     * 
     * @param merchantActivity 商家活动
     * @return 结果
     */
    @Override
    public int insertMerchantActivity(MerchantActivity merchantActivity)
    {
        merchantActivity.setCreateTime(DateUtils.getNowDate());
        return merchantActivityMapper.insertMerchantActivity(merchantActivity);
    }

    /**
     * 修改商家活动
     * 
     * @param merchantActivity 商家活动
     * @return 结果
     */
    @Override
    public int updateMerchantActivity(MerchantActivity merchantActivity)
    {
        merchantActivity.setUpdateTime(DateUtils.getNowDate());
        return merchantActivityMapper.updateMerchantActivity(merchantActivity);
    }

    /**
     * 批量删除商家活动
     * 
     * @param merchantActivityIds 需要删除的商家活动主键
     * @return 结果
     */
    @Override
    public int deleteMerchantActivityByMerchantActivityIds(Long[] merchantActivityIds)
    {
        return merchantActivityMapper.deleteMerchantActivityByMerchantActivityIds(merchantActivityIds);
    }

    /**
     * 删除商家活动信息
     * 
     * @param merchantActivityId 商家活动主键
     * @return 结果
     */
    @Override
    public int deleteMerchantActivityByMerchantActivityId(Long merchantActivityId)
    {
        return merchantActivityMapper.deleteMerchantActivityByMerchantActivityId(merchantActivityId);
    }
}
