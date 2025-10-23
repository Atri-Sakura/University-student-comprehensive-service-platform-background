package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.RiderBase;

/**
 * 骑手基础信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface RiderBaseMapper 
{
    /**
     * 查询骑手基础信息
     * 
     * @param riderBaseId 骑手基础信息主键
     * @return 骑手基础信息
     */
    public RiderBase selectRiderBaseByRiderBaseId(Long riderBaseId);

    /**
     * 查询骑手基础信息列表
     * 
     * @param riderBase 骑手基础信息
     * @return 骑手基础信息集合
     */
    public List<RiderBase> selectRiderBaseList(RiderBase riderBase);

    /**
     * 新增骑手基础信息
     * 
     * @param riderBase 骑手基础信息
     * @return 结果
     */
    public int insertRiderBase(RiderBase riderBase);

    int updateRiderBaseBasicInfo(RiderBase riderBase);
    /**
     * 修改骑手基础信息
     * 
     * @param riderBase 骑手基础信息
     * @return 结果
     */
    public int updateRiderBase(RiderBase riderBase);

    /**
     * 删除骑手基础信息
     * 
     * @param riderBaseId 骑手基础信息主键
     * @return 结果
     */
    public int deleteRiderBaseByRiderBaseId(Long riderBaseId);

    /**
     * 批量删除骑手基础信息
     * 
     * @param riderBaseIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRiderBaseByRiderBaseIds(Long[] riderBaseIds);

    /**
     * 修改骑手工作状态
     *
     */
    int updateRiderWorkStatus(RiderBase riderBase);

    /**
     * 更新骑手授权信息
     */
    int updateRiderAuthInfo(RiderBase rider);

}
