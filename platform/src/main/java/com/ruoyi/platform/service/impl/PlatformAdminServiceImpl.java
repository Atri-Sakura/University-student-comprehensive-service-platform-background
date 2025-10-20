package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.PlatformAdminMapper;
import com.ruoyi.platform.domain.PlatformAdmin;
import com.ruoyi.platform.service.IPlatformAdminService;

/**
 * 平台管理员Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class PlatformAdminServiceImpl implements IPlatformAdminService 
{
    @Autowired
    private PlatformAdminMapper platformAdminMapper;

    /**
     * 查询平台管理员
     * 
     * @param platformAdminId 平台管理员主键
     * @return 平台管理员
     */
    @Override
    public PlatformAdmin selectPlatformAdminByPlatformAdminId(Long platformAdminId)
    {
        return platformAdminMapper.selectPlatformAdminByPlatformAdminId(platformAdminId);
    }

    /**
     * 查询平台管理员列表
     * 
     * @param platformAdmin 平台管理员
     * @return 平台管理员
     */
    @Override
    public List<PlatformAdmin> selectPlatformAdminList(PlatformAdmin platformAdmin)
    {
        return platformAdminMapper.selectPlatformAdminList(platformAdmin);
    }

    /**
     * 新增平台管理员
     * 
     * @param platformAdmin 平台管理员
     * @return 结果
     */
    @Override
    public int insertPlatformAdmin(PlatformAdmin platformAdmin)
    {
        platformAdmin.setCreateTime(DateUtils.getNowDate());
        return platformAdminMapper.insertPlatformAdmin(platformAdmin);
    }

    /**
     * 修改平台管理员
     * 
     * @param platformAdmin 平台管理员
     * @return 结果
     */
    @Override
    public int updatePlatformAdmin(PlatformAdmin platformAdmin)
    {
        platformAdmin.setUpdateTime(DateUtils.getNowDate());
        return platformAdminMapper.updatePlatformAdmin(platformAdmin);
    }

    /**
     * 批量删除平台管理员
     * 
     * @param platformAdminIds 需要删除的平台管理员主键
     * @return 结果
     */
    @Override
    public int deletePlatformAdminByPlatformAdminIds(Long[] platformAdminIds)
    {
        return platformAdminMapper.deletePlatformAdminByPlatformAdminIds(platformAdminIds);
    }

    /**
     * 删除平台管理员信息
     * 
     * @param platformAdminId 平台管理员主键
     * @return 结果
     */
    @Override
    public int deletePlatformAdminByPlatformAdminId(Long platformAdminId)
    {
        return platformAdminMapper.deletePlatformAdminByPlatformAdminId(platformAdminId);
    }
}
