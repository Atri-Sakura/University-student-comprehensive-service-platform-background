package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.PlatformRole;

/**
 * 角色Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IPlatformRoleService 
{
    /**
     * 查询角色
     * 
     * @param platformRoleId 角色主键
     * @return 角色
     */
    public PlatformRole selectPlatformRoleByPlatformRoleId(Long platformRoleId);

    /**
     * 查询角色列表
     * 
     * @param platformRole 角色
     * @return 角色集合
     */
    public List<PlatformRole> selectPlatformRoleList(PlatformRole platformRole);

    /**
     * 新增角色
     * 
     * @param platformRole 角色
     * @return 结果
     */
    public int insertPlatformRole(PlatformRole platformRole);

    /**
     * 修改角色
     * 
     * @param platformRole 角色
     * @return 结果
     */
    public int updatePlatformRole(PlatformRole platformRole);

    /**
     * 批量删除角色
     * 
     * @param platformRoleIds 需要删除的角色主键集合
     * @return 结果
     */
    public int deletePlatformRoleByPlatformRoleIds(Long[] platformRoleIds);

    /**
     * 删除角色信息
     * 
     * @param platformRoleId 角色主键
     * @return 结果
     */
    public int deletePlatformRoleByPlatformRoleId(Long platformRoleId);
}
