package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.PlatformRoleMapper;
import com.ruoyi.platform.domain.PlatformRole;
import com.ruoyi.platform.service.IPlatformRoleService;

/**
 * 角色Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class PlatformRoleServiceImpl implements IPlatformRoleService 
{
    @Autowired
    private PlatformRoleMapper platformRoleMapper;

    /**
     * 查询角色
     * 
     * @param platformRoleId 角色主键
     * @return 角色
     */
    @Override
    public PlatformRole selectPlatformRoleByPlatformRoleId(Long platformRoleId)
    {
        return platformRoleMapper.selectPlatformRoleByPlatformRoleId(platformRoleId);
    }

    /**
     * 查询角色列表
     * 
     * @param platformRole 角色
     * @return 角色
     */
    @Override
    public List<PlatformRole> selectPlatformRoleList(PlatformRole platformRole)
    {
        return platformRoleMapper.selectPlatformRoleList(platformRole);
    }

    /**
     * 新增角色
     * 
     * @param platformRole 角色
     * @return 结果
     */
    @Override
    public int insertPlatformRole(PlatformRole platformRole)
    {
        platformRole.setCreateTime(DateUtils.getNowDate());
        return platformRoleMapper.insertPlatformRole(platformRole);
    }

    /**
     * 修改角色
     * 
     * @param platformRole 角色
     * @return 结果
     */
    @Override
    public int updatePlatformRole(PlatformRole platformRole)
    {
        platformRole.setUpdateTime(DateUtils.getNowDate());
        return platformRoleMapper.updatePlatformRole(platformRole);
    }

    /**
     * 批量删除角色
     * 
     * @param platformRoleIds 需要删除的角色主键
     * @return 结果
     */
    @Override
    public int deletePlatformRoleByPlatformRoleIds(Long[] platformRoleIds)
    {
        return platformRoleMapper.deletePlatformRoleByPlatformRoleIds(platformRoleIds);
    }

    /**
     * 删除角色信息
     * 
     * @param platformRoleId 角色主键
     * @return 结果
     */
    @Override
    public int deletePlatformRoleByPlatformRoleId(Long platformRoleId)
    {
        return platformRoleMapper.deletePlatformRoleByPlatformRoleId(platformRoleId);
    }
}
