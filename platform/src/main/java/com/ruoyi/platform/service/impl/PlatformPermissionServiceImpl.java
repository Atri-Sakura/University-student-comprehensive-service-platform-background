package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.PlatformPermissionMapper;
import com.ruoyi.platform.domain.PlatformPermission;
import com.ruoyi.platform.service.IPlatformPermissionService;

/**
 * 权限Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class PlatformPermissionServiceImpl implements IPlatformPermissionService 
{
    @Autowired
    private PlatformPermissionMapper platformPermissionMapper;

    /**
     * 查询权限
     * 
     * @param platformPermissionId 权限主键
     * @return 权限
     */
    @Override
    public PlatformPermission selectPlatformPermissionByPlatformPermissionId(Long platformPermissionId)
    {
        return platformPermissionMapper.selectPlatformPermissionByPlatformPermissionId(platformPermissionId);
    }

    /**
     * 查询权限列表
     * 
     * @param platformPermission 权限
     * @return 权限
     */
    @Override
    public List<PlatformPermission> selectPlatformPermissionList(PlatformPermission platformPermission)
    {
        return platformPermissionMapper.selectPlatformPermissionList(platformPermission);
    }

    /**
     * 新增权限
     * 
     * @param platformPermission 权限
     * @return 结果
     */
    @Override
    public int insertPlatformPermission(PlatformPermission platformPermission)
    {
        platformPermission.setCreateTime(DateUtils.getNowDate());
        return platformPermissionMapper.insertPlatformPermission(platformPermission);
    }

    /**
     * 修改权限
     * 
     * @param platformPermission 权限
     * @return 结果
     */
    @Override
    public int updatePlatformPermission(PlatformPermission platformPermission)
    {
        platformPermission.setUpdateTime(DateUtils.getNowDate());
        return platformPermissionMapper.updatePlatformPermission(platformPermission);
    }

    /**
     * 批量删除权限
     * 
     * @param platformPermissionIds 需要删除的权限主键
     * @return 结果
     */
    @Override
    public int deletePlatformPermissionByPlatformPermissionIds(Long[] platformPermissionIds)
    {
        return platformPermissionMapper.deletePlatformPermissionByPlatformPermissionIds(platformPermissionIds);
    }

    /**
     * 删除权限信息
     * 
     * @param platformPermissionId 权限主键
     * @return 结果
     */
    @Override
    public int deletePlatformPermissionByPlatformPermissionId(Long platformPermissionId)
    {
        return platformPermissionMapper.deletePlatformPermissionByPlatformPermissionId(platformPermissionId);
    }
}
