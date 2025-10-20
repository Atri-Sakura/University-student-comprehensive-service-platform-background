package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.PlatformRoleMappingMapper;
import com.ruoyi.platform.domain.PlatformRoleMapping;
import com.ruoyi.platform.service.IPlatformRoleMappingService;

/**
 * 角色-账号映射（多角色登录路由核心）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class PlatformRoleMappingServiceImpl implements IPlatformRoleMappingService 
{
    @Autowired
    private PlatformRoleMappingMapper platformRoleMappingMapper;

    /**
     * 查询角色-账号映射（多角色登录路由核心）
     * 
     * @param platformRoleMappingId 角色-账号映射（多角色登录路由核心）主键
     * @return 角色-账号映射（多角色登录路由核心）
     */
    @Override
    public PlatformRoleMapping selectPlatformRoleMappingByPlatformRoleMappingId(Long platformRoleMappingId)
    {
        return platformRoleMappingMapper.selectPlatformRoleMappingByPlatformRoleMappingId(platformRoleMappingId);
    }

    /**
     * 查询角色-账号映射（多角色登录路由核心）列表
     * 
     * @param platformRoleMapping 角色-账号映射（多角色登录路由核心）
     * @return 角色-账号映射（多角色登录路由核心）
     */
    @Override
    public List<PlatformRoleMapping> selectPlatformRoleMappingList(PlatformRoleMapping platformRoleMapping)
    {
        return platformRoleMappingMapper.selectPlatformRoleMappingList(platformRoleMapping);
    }

    /**
     * 新增角色-账号映射（多角色登录路由核心）
     * 
     * @param platformRoleMapping 角色-账号映射（多角色登录路由核心）
     * @return 结果
     */
    @Override
    public int insertPlatformRoleMapping(PlatformRoleMapping platformRoleMapping)
    {
        platformRoleMapping.setCreateTime(DateUtils.getNowDate());
        return platformRoleMappingMapper.insertPlatformRoleMapping(platformRoleMapping);
    }

    /**
     * 修改角色-账号映射（多角色登录路由核心）
     * 
     * @param platformRoleMapping 角色-账号映射（多角色登录路由核心）
     * @return 结果
     */
    @Override
    public int updatePlatformRoleMapping(PlatformRoleMapping platformRoleMapping)
    {
        platformRoleMapping.setUpdateTime(DateUtils.getNowDate());
        return platformRoleMappingMapper.updatePlatformRoleMapping(platformRoleMapping);
    }

    /**
     * 批量删除角色-账号映射（多角色登录路由核心）
     * 
     * @param platformRoleMappingIds 需要删除的角色-账号映射（多角色登录路由核心）主键
     * @return 结果
     */
    @Override
    public int deletePlatformRoleMappingByPlatformRoleMappingIds(Long[] platformRoleMappingIds)
    {
        return platformRoleMappingMapper.deletePlatformRoleMappingByPlatformRoleMappingIds(platformRoleMappingIds);
    }

    /**
     * 删除角色-账号映射（多角色登录路由核心）信息
     * 
     * @param platformRoleMappingId 角色-账号映射（多角色登录路由核心）主键
     * @return 结果
     */
    @Override
    public int deletePlatformRoleMappingByPlatformRoleMappingId(Long platformRoleMappingId)
    {
        return platformRoleMappingMapper.deletePlatformRoleMappingByPlatformRoleMappingId(platformRoleMappingId);
    }
}
