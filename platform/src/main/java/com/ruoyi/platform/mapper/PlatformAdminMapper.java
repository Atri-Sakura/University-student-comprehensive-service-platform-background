package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.PlatformAdmin;

/**
 * 平台管理员Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface PlatformAdminMapper 
{
    /**
     * 查询平台管理员
     * 
     * @param platformAdminId 平台管理员主键
     * @return 平台管理员
     */
    public PlatformAdmin selectPlatformAdminByPlatformAdminId(Long platformAdminId);

    /**
     * 查询平台管理员列表
     * 
     * @param platformAdmin 平台管理员
     * @return 平台管理员集合
     */
    public List<PlatformAdmin> selectPlatformAdminList(PlatformAdmin platformAdmin);

    /**
     * 新增平台管理员
     * 
     * @param platformAdmin 平台管理员
     * @return 结果
     */
    public int insertPlatformAdmin(PlatformAdmin platformAdmin);

    /**
     * 修改平台管理员
     * 
     * @param platformAdmin 平台管理员
     * @return 结果
     */
    public int updatePlatformAdmin(PlatformAdmin platformAdmin);

    /**
     * 删除平台管理员
     * 
     * @param platformAdminId 平台管理员主键
     * @return 结果
     */
    public int deletePlatformAdminByPlatformAdminId(Long platformAdminId);

    /**
     * 批量删除平台管理员
     * 
     * @param platformAdminIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePlatformAdminByPlatformAdminIds(Long[] platformAdminIds);
}
