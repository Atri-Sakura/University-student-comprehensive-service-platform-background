package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消息附件（存储图片/语音等附件的元信息）对象 chat_attachment
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class ChatAttachment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 附件唯一ID（雪花算法生成） */
    private Long attachmentId;

    /** 关联消息ID（一条消息可对应多个附件，如多图发送） */
    @Excel(name = "关联消息ID", readConverterExp = "一=条消息可对应多个附件，如多图发送")
    private Long messageId;

    /** 附件类型：1-图片 2-语音 */
    @Excel(name = "附件类型：1-图片 2-语音")
    private Long attachmentType;

    /** 附件存储URL（MinIO的访问地址，如http://minio:9000/chat-attach/202409/xxx.png） */
    @Excel(name = "附件存储URL", readConverterExp = "M=inIO的访问地址，如http://minio:9000/chat-attach/202409/xxx.png")
    private String attachmentUrl;

    /** 原始文件名（如"IMG_2024.png"） */
    @Excel(name = "原始文件名", readConverterExp = "如=IMG_2024.png")
    private String fileName;

    /** 文件大小（字节，用于前端显示"2.5MB"） */
    @Excel(name = "文件大小", readConverterExp = "字=节，用于前端显示2.5MB")
    private Long fileSize;

    /** 文件后缀（如"png""mp3"，便于筛选文件类型） */
    @Excel(name = "文件后缀", readConverterExp = "如=png,mp3，便于筛选文件类型")
    private String fileExt;

    /** 过期时间（null表示永久有效；如临时图片设为24小时后过期） */
    @Excel(name = "过期时间", readConverterExp = "n=ull表示永久有效；如临时图片设为24小时后过期")
    private Date expireTime;

    /** 是否有效：0-无效（已删除/过期） 1-有效 */
    @Excel(name = "是否有效：0-无效", readConverterExp = "已=删除/过期")
    private Long isValid;

    public void setAttachmentId(Long attachmentId) 
    {
        this.attachmentId = attachmentId;
    }

    public Long getAttachmentId() 
    {
        return attachmentId;
    }

    public void setMessageId(Long messageId) 
    {
        this.messageId = messageId;
    }

    public Long getMessageId() 
    {
        return messageId;
    }

    public void setAttachmentType(Long attachmentType) 
    {
        this.attachmentType = attachmentType;
    }

    public Long getAttachmentType() 
    {
        return attachmentType;
    }

    public void setAttachmentUrl(String attachmentUrl) 
    {
        this.attachmentUrl = attachmentUrl;
    }

    public String getAttachmentUrl() 
    {
        return attachmentUrl;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }

    public void setFileExt(String fileExt) 
    {
        this.fileExt = fileExt;
    }

    public String getFileExt() 
    {
        return fileExt;
    }

    public void setExpireTime(Date expireTime) 
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() 
    {
        return expireTime;
    }

    public void setIsValid(Long isValid) 
    {
        this.isValid = isValid;
    }

    public Long getIsValid() 
    {
        return isValid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("attachmentId", getAttachmentId())
            .append("messageId", getMessageId())
            .append("attachmentType", getAttachmentType())
            .append("attachmentUrl", getAttachmentUrl())
            .append("fileName", getFileName())
            .append("fileSize", getFileSize())
            .append("fileExt", getFileExt())
            .append("expireTime", getExpireTime())
            .append("isValid", getIsValid())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
