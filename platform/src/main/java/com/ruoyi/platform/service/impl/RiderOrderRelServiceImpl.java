package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.RiderOrderRelMapper;
import com.ruoyi.platform.domain.RiderOrderRel;
import com.ruoyi.platform.service.IRiderOrderRelService;

/**
 * 骑手接单关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class RiderOrderRelServiceImpl implements IRiderOrderRelService 
{
    @Autowired
    private RiderOrderRelMapper riderOrderRelMapper;

    /**
     * 查询骑手接单关联
     * 
     * @param riderOrderRelId 骑手接单关联主键
     * @return 骑手接单关联
     */
    @Override
    public RiderOrderRel selectRiderOrderRelByRiderOrderRelId(Long riderOrderRelId)
    {
        return riderOrderRelMapper.selectRiderOrderRelByRiderOrderRelId(riderOrderRelId);
    }

    /**
     * 查询骑手接单关联列表
     * 
     * @param riderOrderRel 骑手接单关联
     * @return 骑手接单关联
     */
    @Override
    public List<RiderOrderRel> selectRiderOrderRelList(RiderOrderRel riderOrderRel)
    {
        return riderOrderRelMapper.selectRiderOrderRelList(riderOrderRel);
    }

    /**
     * 新增骑手接单关联
     * 
     * @param riderOrderRel 骑手接单关联
     * @return 结果
     */
    @Override
    public int insertRiderOrderRel(RiderOrderRel riderOrderRel)
    {
        riderOrderRel.setCreateTime(DateUtils.getNowDate());
        return riderOrderRelMapper.insertRiderOrderRel(riderOrderRel);
    }

    /**
     * 修改骑手接单关联
     * 
     * @param riderOrderRel 骑手接单关联
     * @return 结果
     */
    @Override
    public int updateRiderOrderRel(RiderOrderRel riderOrderRel)
    {
        riderOrderRel.setUpdateTime(DateUtils.getNowDate());
        return riderOrderRelMapper.updateRiderOrderRel(riderOrderRel);
    }

    /**
     * 批量删除骑手接单关联
     * 
     * @param riderOrderRelIds 需要删除的骑手接单关联主键
     * @return 结果
     */
    @Override
    public int deleteRiderOrderRelByRiderOrderRelIds(Long[] riderOrderRelIds)
    {
        return riderOrderRelMapper.deleteRiderOrderRelByRiderOrderRelIds(riderOrderRelIds);
    }

    /**
     * 删除骑手接单关联信息
     * 
     * @param riderOrderRelId 骑手接单关联主键
     * @return 结果
     */
    @Override
    public int deleteRiderOrderRelByRiderOrderRelId(Long riderOrderRelId)
    {
        return riderOrderRelMapper.deleteRiderOrderRelByRiderOrderRelId(riderOrderRelId);
    }
}
