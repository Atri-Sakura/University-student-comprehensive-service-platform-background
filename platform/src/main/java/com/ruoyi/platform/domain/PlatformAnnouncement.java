package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 系统公告对象 platform_announcement
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class PlatformAnnouncement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告唯一ID */
    private Long platformAnnouncementId;

    /** 公告标题 */
    @Excel(name = "公告标题")
    private String title;

    /** 公告内容 */
    @Excel(name = "公告内容")
    private String content;

    /** 发布人ID（关联platform_admin.platform_admin_id） */
    @Excel(name = "发布人ID", readConverterExp = "关=联platform_admin.platform_admin_id")
    private Long publisherId;

    /** 发布人姓名（冗余） */
    @Excel(name = "发布人姓名", readConverterExp = "冗=余")
    private String publisherName;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishTime;

    /** 状态：0-草稿 1-已发布 2-已下架 */
    @Excel(name = "状态：0-草稿 1-已发布 2-已下架")
    private Long status;

    /** 阅读量 */
    @Excel(name = "阅读量")
    private Long readCount;

    /** 是否置顶：0-否 1-是 */
    @Excel(name = "是否置顶：0-否 1-是")
    private Long isTop;

    public void setPlatformAnnouncementId(Long platformAnnouncementId) 
    {
        this.platformAnnouncementId = platformAnnouncementId;
    }

    public Long getPlatformAnnouncementId() 
    {
        return platformAnnouncementId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setPublisherId(Long publisherId) 
    {
        this.publisherId = publisherId;
    }

    public Long getPublisherId() 
    {
        return publisherId;
    }

    public void setPublisherName(String publisherName) 
    {
        this.publisherName = publisherName;
    }

    public String getPublisherName() 
    {
        return publisherName;
    }

    public void setPublishTime(Date publishTime) 
    {
        this.publishTime = publishTime;
    }

    public Date getPublishTime() 
    {
        return publishTime;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setReadCount(Long readCount) 
    {
        this.readCount = readCount;
    }

    public Long getReadCount() 
    {
        return readCount;
    }

    public void setIsTop(Long isTop) 
    {
        this.isTop = isTop;
    }

    public Long getIsTop() 
    {
        return isTop;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("platformAnnouncementId", getPlatformAnnouncementId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("publisherId", getPublisherId())
            .append("publisherName", getPublisherName())
            .append("publishTime", getPublishTime())
            .append("status", getStatus())
            .append("readCount", getReadCount())
            .append("isTop", getIsTop())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
