package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.PlatformAnnouncementMapper;
import com.ruoyi.platform.domain.PlatformAnnouncement;
import com.ruoyi.platform.service.IPlatformAnnouncementService;

/**
 * 系统公告Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class PlatformAnnouncementServiceImpl implements IPlatformAnnouncementService 
{
    @Autowired
    private PlatformAnnouncementMapper platformAnnouncementMapper;

    /**
     * 查询系统公告
     * 
     * @param platformAnnouncementId 系统公告主键
     * @return 系统公告
     */
    @Override
    public PlatformAnnouncement selectPlatformAnnouncementByPlatformAnnouncementId(Long platformAnnouncementId)
    {
        return platformAnnouncementMapper.selectPlatformAnnouncementByPlatformAnnouncementId(platformAnnouncementId);
    }

    /**
     * 查询系统公告列表
     * 
     * @param platformAnnouncement 系统公告
     * @return 系统公告
     */
    @Override
    public List<PlatformAnnouncement> selectPlatformAnnouncementList(PlatformAnnouncement platformAnnouncement)
    {
        return platformAnnouncementMapper.selectPlatformAnnouncementList(platformAnnouncement);
    }

    /**
     * 新增系统公告
     * 
     * @param platformAnnouncement 系统公告
     * @return 结果
     */
    @Override
    public int insertPlatformAnnouncement(PlatformAnnouncement platformAnnouncement)
    {
        platformAnnouncement.setCreateTime(DateUtils.getNowDate());
        return platformAnnouncementMapper.insertPlatformAnnouncement(platformAnnouncement);
    }

    /**
     * 修改系统公告
     * 
     * @param platformAnnouncement 系统公告
     * @return 结果
     */
    @Override
    public int updatePlatformAnnouncement(PlatformAnnouncement platformAnnouncement)
    {
        platformAnnouncement.setUpdateTime(DateUtils.getNowDate());
        return platformAnnouncementMapper.updatePlatformAnnouncement(platformAnnouncement);
    }

    /**
     * 批量删除系统公告
     * 
     * @param platformAnnouncementIds 需要删除的系统公告主键
     * @return 结果
     */
    @Override
    public int deletePlatformAnnouncementByPlatformAnnouncementIds(Long[] platformAnnouncementIds)
    {
        return platformAnnouncementMapper.deletePlatformAnnouncementByPlatformAnnouncementIds(platformAnnouncementIds);
    }

    /**
     * 删除系统公告信息
     * 
     * @param platformAnnouncementId 系统公告主键
     * @return 结果
     */
    @Override
    public int deletePlatformAnnouncementByPlatformAnnouncementId(Long platformAnnouncementId)
    {
        return platformAnnouncementMapper.deletePlatformAnnouncementByPlatformAnnouncementId(platformAnnouncementId);
    }
}
