package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.RiderLocation;

/**
 * 骑手位置Service接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IRiderLocationService 
{
    /**
     * 查询骑手位置
     * 
     * @param riderLocationId 骑手位置主键
     * @return 骑手位置
     */
    public RiderLocation selectRiderLocationByRiderLocationId(Long riderLocationId);

    /**
     * 查询骑手位置列表
     * 
     * @param riderLocation 骑手位置
     * @return 骑手位置集合
     */
    public List<RiderLocation> selectRiderLocationList(RiderLocation riderLocation);

    /**
     * 新增骑手位置
     * 
     * @param riderLocation 骑手位置
     * @return 结果
     */
    public int insertRiderLocation(RiderLocation riderLocation);

    /**
     * 修改骑手位置
     * 
     * @param riderLocation 骑手位置
     * @return 结果
     */
    public int updateRiderLocation(RiderLocation riderLocation);

    /**
     * 批量删除骑手位置
     * 
     * @param riderLocationIds 需要删除的骑手位置主键集合
     * @return 结果
     */
    public int deleteRiderLocationByRiderLocationIds(Long[] riderLocationIds);

    /**
     * 删除骑手位置信息
     * 
     * @param riderLocationId 骑手位置主键
     * @return 结果
     */
    public int deleteRiderLocationByRiderLocationId(Long riderLocationId);
}
