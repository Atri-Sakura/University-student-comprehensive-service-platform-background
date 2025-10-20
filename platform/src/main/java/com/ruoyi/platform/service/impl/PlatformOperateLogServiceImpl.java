package com.ruoyi.platform.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.PlatformOperateLogMapper;
import com.ruoyi.platform.domain.PlatformOperateLog;
import com.ruoyi.platform.service.IPlatformOperateLogService;

/**
 * 系统操作日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class PlatformOperateLogServiceImpl implements IPlatformOperateLogService 
{
    @Autowired
    private PlatformOperateLogMapper platformOperateLogMapper;

    /**
     * 查询系统操作日志
     * 
     * @param platformOperateLogId 系统操作日志主键
     * @return 系统操作日志
     */
    @Override
    public PlatformOperateLog selectPlatformOperateLogByPlatformOperateLogId(Long platformOperateLogId)
    {
        return platformOperateLogMapper.selectPlatformOperateLogByPlatformOperateLogId(platformOperateLogId);
    }

    /**
     * 查询系统操作日志列表
     * 
     * @param platformOperateLog 系统操作日志
     * @return 系统操作日志
     */
    @Override
    public List<PlatformOperateLog> selectPlatformOperateLogList(PlatformOperateLog platformOperateLog)
    {
        return platformOperateLogMapper.selectPlatformOperateLogList(platformOperateLog);
    }

    /**
     * 新增系统操作日志
     * 
     * @param platformOperateLog 系统操作日志
     * @return 结果
     */
    @Override
    public int insertPlatformOperateLog(PlatformOperateLog platformOperateLog)
    {
        return platformOperateLogMapper.insertPlatformOperateLog(platformOperateLog);
    }

    /**
     * 修改系统操作日志
     * 
     * @param platformOperateLog 系统操作日志
     * @return 结果
     */
    @Override
    public int updatePlatformOperateLog(PlatformOperateLog platformOperateLog)
    {
        return platformOperateLogMapper.updatePlatformOperateLog(platformOperateLog);
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param platformOperateLogIds 需要删除的系统操作日志主键
     * @return 结果
     */
    @Override
    public int deletePlatformOperateLogByPlatformOperateLogIds(Long[] platformOperateLogIds)
    {
        return platformOperateLogMapper.deletePlatformOperateLogByPlatformOperateLogIds(platformOperateLogIds);
    }

    /**
     * 删除系统操作日志信息
     * 
     * @param platformOperateLogId 系统操作日志主键
     * @return 结果
     */
    @Override
    public int deletePlatformOperateLogByPlatformOperateLogId(Long platformOperateLogId)
    {
        return platformOperateLogMapper.deletePlatformOperateLogByPlatformOperateLogId(platformOperateLogId);
    }
}
