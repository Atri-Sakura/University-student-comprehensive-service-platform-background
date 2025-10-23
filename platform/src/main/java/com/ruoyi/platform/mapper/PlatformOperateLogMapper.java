package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.PlatformOperateLog;

/**
 * 系统操作日志Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface PlatformOperateLogMapper 
{
    /**
     * 查询系统操作日志
     * 
     * @param platformOperateLogId 系统操作日志主键
     * @return 系统操作日志
     */
    public PlatformOperateLog selectPlatformOperateLogByPlatformOperateLogId(Long platformOperateLogId);

    /**
     * 查询系统操作日志列表
     * 
     * @param platformOperateLog 系统操作日志
     * @return 系统操作日志集合
     */
    public List<PlatformOperateLog> selectPlatformOperateLogList(PlatformOperateLog platformOperateLog);

    /**
     * 新增系统操作日志
     * 
     * @param platformOperateLog 系统操作日志
     * @return 结果
     */
    public int insertPlatformOperateLog(PlatformOperateLog platformOperateLog);

    /**
     * 修改系统操作日志
     * 
     * @param platformOperateLog 系统操作日志
     * @return 结果
     */
    public int updatePlatformOperateLog(PlatformOperateLog platformOperateLog);

    /**
     * 删除系统操作日志
     * 
     * @param platformOperateLogId 系统操作日志主键
     * @return 结果
     */
    public int deletePlatformOperateLogByPlatformOperateLogId(Long platformOperateLogId);

    /**
     * 批量删除系统操作日志
     * 
     * @param platformOperateLogIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePlatformOperateLogByPlatformOperateLogIds(Long[] platformOperateLogIds);
}
