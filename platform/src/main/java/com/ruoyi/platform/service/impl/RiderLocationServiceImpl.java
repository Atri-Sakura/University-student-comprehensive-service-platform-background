package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.RiderLocationMapper;
import com.ruoyi.platform.domain.RiderLocation;
import com.ruoyi.platform.service.IRiderLocationService;

/**
 * 骑手位置Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class RiderLocationServiceImpl implements IRiderLocationService 
{
    @Autowired
    private RiderLocationMapper riderLocationMapper;

    /**
     * 查询骑手位置
     * 
     * @param riderLocationId 骑手位置主键
     * @return 骑手位置
     */
    @Override
    public RiderLocation selectRiderLocationByRiderLocationId(Long riderLocationId)
    {
        return riderLocationMapper.selectRiderLocationByRiderLocationId(riderLocationId);
    }

    /**
     * 查询骑手位置列表
     * 
     * @param riderLocation 骑手位置
     * @return 骑手位置
     */
    @Override
    public List<RiderLocation> selectRiderLocationList(RiderLocation riderLocation)
    {
        return riderLocationMapper.selectRiderLocationList(riderLocation);
    }

    /**
     * 新增骑手位置
     * 
     * @param riderLocation 骑手位置
     * @return 结果
     */
    @Override
    public int insertRiderLocation(RiderLocation riderLocation)
    {
        return riderLocationMapper.insertRiderLocation(riderLocation);
    }

    /**
     * 修改骑手位置
     * 
     * @param riderLocation 骑手位置
     * @return 结果
     */
    @Override
    public int updateRiderLocation(RiderLocation riderLocation)
    {
        riderLocation.setUpdateTime(DateUtils.getNowDate());
        return riderLocationMapper.updateRiderLocation(riderLocation);
    }

    /**
     * 批量删除骑手位置
     * 
     * @param riderLocationIds 需要删除的骑手位置主键
     * @return 结果
     */
    @Override
    public int deleteRiderLocationByRiderLocationIds(Long[] riderLocationIds)
    {
        return riderLocationMapper.deleteRiderLocationByRiderLocationIds(riderLocationIds);
    }

    /**
     * 删除骑手位置信息
     * 
     * @param riderLocationId 骑手位置主键
     * @return 结果
     */
    @Override
    public int deleteRiderLocationByRiderLocationId(Long riderLocationId)
    {
        return riderLocationMapper.deleteRiderLocationByRiderLocationId(riderLocationId);
    }
}
