package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.PlatformCouponMapper;
import com.ruoyi.platform.domain.PlatformCoupon;
import com.ruoyi.platform.service.IPlatformCouponService;

/**
 * 平台优惠券Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class PlatformCouponServiceImpl implements IPlatformCouponService 
{
    @Autowired
    private PlatformCouponMapper platformCouponMapper;

    /**
     * 查询平台优惠券
     * 
     * @param platformCouponId 平台优惠券主键
     * @return 平台优惠券
     */
    @Override
    public PlatformCoupon selectPlatformCouponByPlatformCouponId(Long platformCouponId)
    {
        return platformCouponMapper.selectPlatformCouponByPlatformCouponId(platformCouponId);
    }

    /**
     * 查询平台优惠券列表
     * 
     * @param platformCoupon 平台优惠券
     * @return 平台优惠券
     */
    @Override
    public List<PlatformCoupon> selectPlatformCouponList(PlatformCoupon platformCoupon)
    {
        return platformCouponMapper.selectPlatformCouponList(platformCoupon);
    }

    /**
     * 新增平台优惠券
     * 
     * @param platformCoupon 平台优惠券
     * @return 结果
     */
    @Override
    public int insertPlatformCoupon(PlatformCoupon platformCoupon)
    {
        platformCoupon.setCreateTime(DateUtils.getNowDate());
        return platformCouponMapper.insertPlatformCoupon(platformCoupon);
    }

    /**
     * 修改平台优惠券
     * 
     * @param platformCoupon 平台优惠券
     * @return 结果
     */
    @Override
    public int updatePlatformCoupon(PlatformCoupon platformCoupon)
    {
        platformCoupon.setUpdateTime(DateUtils.getNowDate());
        return platformCouponMapper.updatePlatformCoupon(platformCoupon);
    }

    /**
     * 批量删除平台优惠券
     * 
     * @param platformCouponIds 需要删除的平台优惠券主键
     * @return 结果
     */
    @Override
    public int deletePlatformCouponByPlatformCouponIds(Long[] platformCouponIds)
    {
        return platformCouponMapper.deletePlatformCouponByPlatformCouponIds(platformCouponIds);
    }

    /**
     * 删除平台优惠券信息
     * 
     * @param platformCouponId 平台优惠券主键
     * @return 结果
     */
    @Override
    public int deletePlatformCouponByPlatformCouponId(Long platformCouponId)
    {
        return platformCouponMapper.deletePlatformCouponByPlatformCouponId(platformCouponId);
    }
}
