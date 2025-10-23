package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.RiderBaseMapper;
import com.ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.service.IRiderBaseService;

/**
 * 骑手基础信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class RiderBaseServiceImpl implements IRiderBaseService 
{
    @Autowired
    private RiderBaseMapper riderBaseMapper;

    /**
     * 查询骑手基础信息
     * 
     * @param riderBaseId 骑手基础信息主键
     * @return 骑手基础信息
     */
    @Override
    public RiderBase selectRiderBaseByRiderBaseId(Long riderBaseId)
    {
        return riderBaseMapper.selectRiderBaseByRiderBaseId(riderBaseId);
    }

    /**
     * 查询骑手基础信息列表
     * 
     * @param riderBase 骑手基础信息
     * @return 骑手基础信息
     */
    @Override
    public List<RiderBase> selectRiderBaseList(RiderBase riderBase)
    {
        return riderBaseMapper.selectRiderBaseList(riderBase);
    }

    /**
     * 新增骑手基础信息
     * 
     * @param riderBase 骑手基础信息
     * @return 结果
     */
    @Override
    public int insertRiderBase(RiderBase riderBase)
    {
        riderBase.setCreateTime(DateUtils.getNowDate());
        return riderBaseMapper.insertRiderBase(riderBase);
    }

    /**
     * 修改骑手基础信息
     * 
     * @param riderBase 骑手基础信息
     * @return 结果
     */
    @Override
    public int updateRiderBase(RiderBase riderBase)
    {
        riderBase.setUpdateTime(DateUtils.getNowDate());
        return riderBaseMapper.updateRiderBase(riderBase);
    }

    @Override
    public int updateRiderBaseBasicInfo(RiderBase riderBase) {
        return riderBaseMapper.updateRiderBaseBasicInfo(riderBase);
    }


    /**
     * 批量删除骑手基础信息
     * 
     * @param riderBaseIds 需要删除的骑手基础信息主键
     * @return 结果
     */
    @Override
    public int deleteRiderBaseByRiderBaseIds(Long[] riderBaseIds)
    {
        return riderBaseMapper.deleteRiderBaseByRiderBaseIds(riderBaseIds);
    }

    /**
     * 删除骑手基础信息信息
     * 
     * @param riderBaseId 骑手基础信息主键
     * @return 结果
     */
    @Override
    public int deleteRiderBaseByRiderBaseId(Long riderBaseId)
    {
        return riderBaseMapper.deleteRiderBaseByRiderBaseId(riderBaseId);
    }

    /**
     * 修改骑手工作状态
     *
     */
    @Override
    public int updateRiderWorkStatus(RiderBase riderBase) {
        RiderBase current = riderBaseMapper.selectRiderBaseByRiderBaseId(riderBase.getRiderBaseId());
        if (current != null && current.getWorkStatus().equals(riderBase.getWorkStatus())) {
            // 状态一致，不更新
            return 0;
        }
        return riderBaseMapper.updateRiderWorkStatus(riderBase);
    }

    /**
     * 更新骑手授权信息
     */
    @Override
    public int updateRiderAuthInfo(RiderBase rider) {
        return riderBaseMapper.updateRiderAuthInfo(rider);
    }

}
