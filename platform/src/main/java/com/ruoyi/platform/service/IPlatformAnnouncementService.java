package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.PlatformAnnouncement;

/**
 * 系统公告Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IPlatformAnnouncementService 
{
    /**
     * 查询系统公告
     * 
     * @param platformAnnouncementId 系统公告主键
     * @return 系统公告
     */
    public PlatformAnnouncement selectPlatformAnnouncementByPlatformAnnouncementId(Long platformAnnouncementId);

    /**
     * 查询系统公告列表
     * 
     * @param platformAnnouncement 系统公告
     * @return 系统公告集合
     */
    public List<PlatformAnnouncement> selectPlatformAnnouncementList(PlatformAnnouncement platformAnnouncement);

    /**
     * 新增系统公告
     * 
     * @param platformAnnouncement 系统公告
     * @return 结果
     */
    public int insertPlatformAnnouncement(PlatformAnnouncement platformAnnouncement);

    /**
     * 修改系统公告
     * 
     * @param platformAnnouncement 系统公告
     * @return 结果
     */
    public int updatePlatformAnnouncement(PlatformAnnouncement platformAnnouncement);

    /**
     * 批量删除系统公告
     * 
     * @param platformAnnouncementIds 需要删除的系统公告主键集合
     * @return 结果
     */
    public int deletePlatformAnnouncementByPlatformAnnouncementIds(Long[] platformAnnouncementIds);

    /**
     * 删除系统公告信息
     * 
     * @param platformAnnouncementId 系统公告主键
     * @return 结果
     */
    public int deletePlatformAnnouncementByPlatformAnnouncementId(Long platformAnnouncementId);
}
