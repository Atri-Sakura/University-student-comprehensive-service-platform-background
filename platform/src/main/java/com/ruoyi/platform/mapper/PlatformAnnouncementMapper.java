package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.PlatformAnnouncement;

/**
 * 系统公告Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface PlatformAnnouncementMapper 
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
     * 删除系统公告
     * 
     * @param platformAnnouncementId 系统公告主键
     * @return 结果
     */
    public int deletePlatformAnnouncementByPlatformAnnouncementId(Long platformAnnouncementId);

    /**
     * 批量删除系统公告
     * 
     * @param platformAnnouncementIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePlatformAnnouncementByPlatformAnnouncementIds(Long[] platformAnnouncementIds);
}
