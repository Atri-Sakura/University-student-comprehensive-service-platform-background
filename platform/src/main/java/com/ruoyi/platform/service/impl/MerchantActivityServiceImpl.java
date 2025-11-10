package com.ruoyi.platform.service.impl;

import java.util.Date;
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
 * @date 2025-10-20
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
    public MerchantActivity selectMerchantActivityByMerchantActivityId(Long merchantActivityId) {
        MerchantActivity activity = merchantActivityMapper.selectMerchantActivityByMerchantActivityId(merchantActivityId);
        if (activity != null) {
            // 自动判断并设置状态
            activity.setStatus(getRealStatus(activity.getStartTime(), activity.getEndTime()));
        }
        return activity;
    }

    /**
     * 查询商家活动列表
     * 
     * @param merchantActivity 商家活动
     * @return 商家活动
     */
    @Override
    public List<MerchantActivity> selectMerchantActivityList(MerchantActivity merchantActivity) {
        List<MerchantActivity> list = merchantActivityMapper.selectMerchantActivityList(merchantActivity);
        Date now = new Date();
        for (MerchantActivity act : list) {
            // 自动判断并设置状态
            act.setStatus(getRealStatus(act.getStartTime(), act.getEndTime()));
        }
        return list;
    }

    private Long getRealStatus(Date start, Date end) {
        Date now = new Date();
        if (now.before(start)) {
            return 0L; // 未开始
        }
        if (now.after(end)) {
            return 2L; // 已结束
        }
        return 1L; // 进行中
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
