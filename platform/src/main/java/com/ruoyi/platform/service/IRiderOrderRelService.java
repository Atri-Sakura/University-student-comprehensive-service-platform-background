package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.RiderOrderRel;

/**
 * 骑手接单关联Service接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IRiderOrderRelService 
{
    /**
     * 查询骑手接单关联
     * 
     * @param riderOrderRelId 骑手接单关联主键
     * @return 骑手接单关联
     */
    public RiderOrderRel selectRiderOrderRelByRiderOrderRelId(Long riderOrderRelId);

    /**
     * 查询骑手接单关联列表
     * 
     * @param riderOrderRel 骑手接单关联
     * @return 骑手接单关联集合
     */
    public List<RiderOrderRel> selectRiderOrderRelList(RiderOrderRel riderOrderRel);

    /**
     * 新增骑手接单关联
     * 
     * @param riderOrderRel 骑手接单关联
     * @return 结果
     */
    public int insertRiderOrderRel(RiderOrderRel riderOrderRel);

    /**
     * 修改骑手接单关联
     * 
     * @param riderOrderRel 骑手接单关联
     * @return 结果
     */
    public int updateRiderOrderRel(RiderOrderRel riderOrderRel);

    /**
     * 批量删除骑手接单关联
     * 
     * @param riderOrderRelIds 需要删除的骑手接单关联主键集合
     * @return 结果
     */
    public int deleteRiderOrderRelByRiderOrderRelIds(Long[] riderOrderRelIds);

    /**
     * 删除骑手接单关联信息
     * 
     * @param riderOrderRelId 骑手接单关联主键
     * @return 结果
     */
    public int deleteRiderOrderRelByRiderOrderRelId(Long riderOrderRelId);
}
