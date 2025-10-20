package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.PlatformRolePermMapper;
import com.ruoyi.platform.domain.PlatformRolePerm;
import com.ruoyi.platform.service.IPlatformRolePermService;

/**
 * 角色权限关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class PlatformRolePermServiceImpl implements IPlatformRolePermService 
{
    @Autowired
    private PlatformRolePermMapper platformRolePermMapper;

    /**
     * 查询角色权限关联
     * 
     * @param platformRolePermId 角色权限关联主键
     * @return 角色权限关联
     */
    @Override
    public PlatformRolePerm selectPlatformRolePermByPlatformRolePermId(Long platformRolePermId)
    {
        return platformRolePermMapper.selectPlatformRolePermByPlatformRolePermId(platformRolePermId);
    }

    /**
     * 查询角色权限关联列表
     * 
     * @param platformRolePerm 角色权限关联
     * @return 角色权限关联
     */
    @Override
    public List<PlatformRolePerm> selectPlatformRolePermList(PlatformRolePerm platformRolePerm)
    {
        return platformRolePermMapper.selectPlatformRolePermList(platformRolePerm);
    }

    /**
     * 新增角色权限关联
     * 
     * @param platformRolePerm 角色权限关联
     * @return 结果
     */
    @Override
    public int insertPlatformRolePerm(PlatformRolePerm platformRolePerm)
    {
        platformRolePerm.setCreateTime(DateUtils.getNowDate());
        return platformRolePermMapper.insertPlatformRolePerm(platformRolePerm);
    }

    /**
     * 修改角色权限关联
     * 
     * @param platformRolePerm 角色权限关联
     * @return 结果
     */
    @Override
    public int updatePlatformRolePerm(PlatformRolePerm platformRolePerm)
    {
        return platformRolePermMapper.updatePlatformRolePerm(platformRolePerm);
    }

    /**
     * 批量删除角色权限关联
     * 
     * @param platformRolePermIds 需要删除的角色权限关联主键
     * @return 结果
     */
    @Override
    public int deletePlatformRolePermByPlatformRolePermIds(Long[] platformRolePermIds)
    {
        return platformRolePermMapper.deletePlatformRolePermByPlatformRolePermIds(platformRolePermIds);
    }

    /**
     * 删除角色权限关联信息
     * 
     * @param platformRolePermId 角色权限关联主键
     * @return 结果
     */
    @Override
    public int deletePlatformRolePermByPlatformRolePermId(Long platformRolePermId)
    {
        return platformRolePermMapper.deletePlatformRolePermByPlatformRolePermId(platformRolePermId);
    }
}
