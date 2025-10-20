package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.PlatformWorkorderMapper;
import com.ruoyi.platform.domain.PlatformWorkorder;
import com.ruoyi.platform.service.IPlatformWorkorderService;

/**
 * 客服工单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class PlatformWorkorderServiceImpl implements IPlatformWorkorderService 
{
    @Autowired
    private PlatformWorkorderMapper platformWorkorderMapper;

    /**
     * 查询客服工单
     * 
     * @param platformWorkorderId 客服工单主键
     * @return 客服工单
     */
    @Override
    public PlatformWorkorder selectPlatformWorkorderByPlatformWorkorderId(Long platformWorkorderId)
    {
        return platformWorkorderMapper.selectPlatformWorkorderByPlatformWorkorderId(platformWorkorderId);
    }

    /**
     * 查询客服工单列表
     * 
     * @param platformWorkorder 客服工单
     * @return 客服工单
     */
    @Override
    public List<PlatformWorkorder> selectPlatformWorkorderList(PlatformWorkorder platformWorkorder)
    {
        return platformWorkorderMapper.selectPlatformWorkorderList(platformWorkorder);
    }

    /**
     * 新增客服工单
     * 
     * @param platformWorkorder 客服工单
     * @return 结果
     */
    @Override
    public int insertPlatformWorkorder(PlatformWorkorder platformWorkorder)
    {
        platformWorkorder.setCreateTime(DateUtils.getNowDate());
        return platformWorkorderMapper.insertPlatformWorkorder(platformWorkorder);
    }

    /**
     * 修改客服工单
     * 
     * @param platformWorkorder 客服工单
     * @return 结果
     */
    @Override
    public int updatePlatformWorkorder(PlatformWorkorder platformWorkorder)
    {
        return platformWorkorderMapper.updatePlatformWorkorder(platformWorkorder);
    }

    /**
     * 批量删除客服工单
     * 
     * @param platformWorkorderIds 需要删除的客服工单主键
     * @return 结果
     */
    @Override
    public int deletePlatformWorkorderByPlatformWorkorderIds(Long[] platformWorkorderIds)
    {
        return platformWorkorderMapper.deletePlatformWorkorderByPlatformWorkorderIds(platformWorkorderIds);
    }

    /**
     * 删除客服工单信息
     * 
     * @param platformWorkorderId 客服工单主键
     * @return 结果
     */
    @Override
    public int deletePlatformWorkorderByPlatformWorkorderId(Long platformWorkorderId)
    {
        return platformWorkorderMapper.deletePlatformWorkorderByPlatformWorkorderId(platformWorkorderId);
    }
}
