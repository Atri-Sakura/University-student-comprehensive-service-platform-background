package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.PlatformAdmin;

/**
 * 平台管理员Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IPlatformAdminService 
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
     * 批量删除平台管理员
     * 
     * @param platformAdminIds 需要删除的平台管理员主键集合
     * @return 结果
     */
    public int deletePlatformAdminByPlatformAdminIds(Long[] platformAdminIds);

    /**
     * 删除平台管理员信息
     * 
     * @param platformAdminId 平台管理员主键
     * @return 结果
     */
    public int deletePlatformAdminByPlatformAdminId(Long platformAdminId);
}
