package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.PlatformPermission;

/**
 * 权限Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IPlatformPermissionService 
{
    /**
     * 查询权限
     * 
     * @param platformPermissionId 权限主键
     * @return 权限
     */
    public PlatformPermission selectPlatformPermissionByPlatformPermissionId(Long platformPermissionId);

    /**
     * 查询权限列表
     * 
     * @param platformPermission 权限
     * @return 权限集合
     */
    public List<PlatformPermission> selectPlatformPermissionList(PlatformPermission platformPermission);

    /**
     * 新增权限
     * 
     * @param platformPermission 权限
     * @return 结果
     */
    public int insertPlatformPermission(PlatformPermission platformPermission);

    /**
     * 修改权限
     * 
     * @param platformPermission 权限
     * @return 结果
     */
    public int updatePlatformPermission(PlatformPermission platformPermission);

    /**
     * 批量删除权限
     * 
     * @param platformPermissionIds 需要删除的权限主键集合
     * @return 结果
     */
    public int deletePlatformPermissionByPlatformPermissionIds(Long[] platformPermissionIds);

    /**
     * 删除权限信息
     * 
     * @param platformPermissionId 权限主键
     * @return 结果
     */
    public int deletePlatformPermissionByPlatformPermissionId(Long platformPermissionId);
}
