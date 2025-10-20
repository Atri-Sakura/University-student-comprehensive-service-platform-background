package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.PlatformRoleMapping;

/**
 * 角色-账号映射（多角色登录路由核心）Service接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IPlatformRoleMappingService 
{
    /**
     * 查询角色-账号映射（多角色登录路由核心）
     * 
     * @param platformRoleMappingId 角色-账号映射（多角色登录路由核心）主键
     * @return 角色-账号映射（多角色登录路由核心）
     */
    public PlatformRoleMapping selectPlatformRoleMappingByPlatformRoleMappingId(Long platformRoleMappingId);

    /**
     * 查询角色-账号映射（多角色登录路由核心）列表
     * 
     * @param platformRoleMapping 角色-账号映射（多角色登录路由核心）
     * @return 角色-账号映射（多角色登录路由核心）集合
     */
    public List<PlatformRoleMapping> selectPlatformRoleMappingList(PlatformRoleMapping platformRoleMapping);

    /**
     * 新增角色-账号映射（多角色登录路由核心）
     * 
     * @param platformRoleMapping 角色-账号映射（多角色登录路由核心）
     * @return 结果
     */
    public int insertPlatformRoleMapping(PlatformRoleMapping platformRoleMapping);

    /**
     * 修改角色-账号映射（多角色登录路由核心）
     * 
     * @param platformRoleMapping 角色-账号映射（多角色登录路由核心）
     * @return 结果
     */
    public int updatePlatformRoleMapping(PlatformRoleMapping platformRoleMapping);

    /**
     * 批量删除角色-账号映射（多角色登录路由核心）
     * 
     * @param platformRoleMappingIds 需要删除的角色-账号映射（多角色登录路由核心）主键集合
     * @return 结果
     */
    public int deletePlatformRoleMappingByPlatformRoleMappingIds(Long[] platformRoleMappingIds);

    /**
     * 删除角色-账号映射（多角色登录路由核心）信息
     * 
     * @param platformRoleMappingId 角色-账号映射（多角色登录路由核心）主键
     * @return 结果
     */
    public int deletePlatformRoleMappingByPlatformRoleMappingId(Long platformRoleMappingId);
}
