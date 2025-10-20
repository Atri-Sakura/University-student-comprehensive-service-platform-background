package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.PlatformRole;

/**
 * 角色Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface PlatformRoleMapper 
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
     * 删除角色
     * 
     * @param platformRoleId 角色主键
     * @return 结果
     */
    public int deletePlatformRoleByPlatformRoleId(Long platformRoleId);

    /**
     * 批量删除角色
     * 
     * @param platformRoleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePlatformRoleByPlatformRoleIds(Long[] platformRoleIds);
}
