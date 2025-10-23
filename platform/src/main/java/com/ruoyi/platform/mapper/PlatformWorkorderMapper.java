package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.PlatformWorkorder;

/**
 * 客服工单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface PlatformWorkorderMapper 
{
    /**
     * 查询客服工单
     * 
     * @param platformWorkorderId 客服工单主键
     * @return 客服工单
     */
    public PlatformWorkorder selectPlatformWorkorderByPlatformWorkorderId(Long platformWorkorderId);

    /**
     * 查询客服工单列表
     * 
     * @param platformWorkorder 客服工单
     * @return 客服工单集合
     */
    public List<PlatformWorkorder> selectPlatformWorkorderList(PlatformWorkorder platformWorkorder);

    /**
     * 新增客服工单
     * 
     * @param platformWorkorder 客服工单
     * @return 结果
     */
    public int insertPlatformWorkorder(PlatformWorkorder platformWorkorder);

    /**
     * 修改客服工单
     * 
     * @param platformWorkorder 客服工单
     * @return 结果
     */
    public int updatePlatformWorkorder(PlatformWorkorder platformWorkorder);

    /**
     * 删除客服工单
     * 
     * @param platformWorkorderId 客服工单主键
     * @return 结果
     */
    public int deletePlatformWorkorderByPlatformWorkorderId(Long platformWorkorderId);

    /**
     * 批量删除客服工单
     * 
     * @param platformWorkorderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePlatformWorkorderByPlatformWorkorderIds(Long[] platformWorkorderIds);
}
