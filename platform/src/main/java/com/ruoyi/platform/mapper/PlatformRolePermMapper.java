package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.PlatformRolePerm;

/**
 * 角色权限关联Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface PlatformRolePermMapper 
{
    /**
     * 查询角色权限关联
     * 
     * @param platformRolePermId 角色权限关联主键
     * @return 角色权限关联
     */
    public PlatformRolePerm selectPlatformRolePermByPlatformRolePermId(Long platformRolePermId);

    /**
     * 查询角色权限关联列表
     * 
     * @param platformRolePerm 角色权限关联
     * @return 角色权限关联集合
     */
    public List<PlatformRolePerm> selectPlatformRolePermList(PlatformRolePerm platformRolePerm);

    /**
     * 新增角色权限关联
     * 
     * @param platformRolePerm 角色权限关联
     * @return 结果
     */
    public int insertPlatformRolePerm(PlatformRolePerm platformRolePerm);

    /**
     * 修改角色权限关联
     * 
     * @param platformRolePerm 角色权限关联
     * @return 结果
     */
    public int updatePlatformRolePerm(PlatformRolePerm platformRolePerm);

    /**
     * 删除角色权限关联
     * 
     * @param platformRolePermId 角色权限关联主键
     * @return 结果
     */
    public int deletePlatformRolePermByPlatformRolePermId(Long platformRolePermId);

    /**
     * 批量删除角色权限关联
     * 
     * @param platformRolePermIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePlatformRolePermByPlatformRolePermIds(Long[] platformRolePermIds);
}
