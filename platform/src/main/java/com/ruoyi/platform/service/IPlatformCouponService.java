package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.PlatformCoupon;

/**
 * 平台优惠券Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IPlatformCouponService 
{
    /**
     * 查询平台优惠券
     * 
     * @param platformCouponId 平台优惠券主键
     * @return 平台优惠券
     */
    public PlatformCoupon selectPlatformCouponByPlatformCouponId(Long platformCouponId);

    /**
     * 查询平台优惠券列表
     * 
     * @param platformCoupon 平台优惠券
     * @return 平台优惠券集合
     */
    public List<PlatformCoupon> selectPlatformCouponList(PlatformCoupon platformCoupon);

    /**
     * 新增平台优惠券
     * 
     * @param platformCoupon 平台优惠券
     * @return 结果
     */
    public int insertPlatformCoupon(PlatformCoupon platformCoupon);

    /**
     * 修改平台优惠券
     * 
     * @param platformCoupon 平台优惠券
     * @return 结果
     */
    public int updatePlatformCoupon(PlatformCoupon platformCoupon);

    /**
     * 批量删除平台优惠券
     * 
     * @param platformCouponIds 需要删除的平台优惠券主键集合
     * @return 结果
     */
    public int deletePlatformCouponByPlatformCouponIds(Long[] platformCouponIds);

    /**
     * 删除平台优惠券信息
     * 
     * @param platformCouponId 平台优惠券主键
     * @return 结果
     */
    public int deletePlatformCouponByPlatformCouponId(Long platformCouponId);
}
