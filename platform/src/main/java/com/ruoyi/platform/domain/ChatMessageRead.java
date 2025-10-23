package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）对象 chat_message_read
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class ChatMessageRead extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 已读记录唯一ID（雪花算法生成） */
    private Long readId;

    /** 关联消息ID（关联chat_message.message_id） */
    @Excel(name = "关联消息ID", readConverterExp = "关=联chat_message.message_id")
    private Long messageId;

    /** 已读用户类型：1-用户 2-骑手 3-商家 */
    @Excel(name = "已读用户类型：1-用户 2-骑手 3-商家")
    private Long readerType;

    /** 已读用户ID（谁已读这条消息） */
    @Excel(name = "已读用户ID", readConverterExp = "谁=已读这条消息")
    private Long readerId;

    /** 已读状态：0-未读 1-已读 */
    @Excel(name = "已读状态：0-未读 1-已读")
    private Long readStatus;

    /** 已读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "已读时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date readTime;

    public void setReadId(Long readId) 
    {
        this.readId = readId;
    }

    public Long getReadId() 
    {
        return readId;
    }

    public void setMessageId(Long messageId) 
    {
        this.messageId = messageId;
    }

    public Long getMessageId() 
    {
        return messageId;
    }

    public void setReaderType(Long readerType) 
    {
        this.readerType = readerType;
    }

    public Long getReaderType() 
    {
        return readerType;
    }

    public void setReaderId(Long readerId) 
    {
        this.readerId = readerId;
    }

    public Long getReaderId() 
    {
        return readerId;
    }

    public void setReadStatus(Long readStatus) 
    {
        this.readStatus = readStatus;
    }

    public Long getReadStatus() 
    {
        return readStatus;
    }

    public void setReadTime(Date readTime) 
    {
        this.readTime = readTime;
    }

    public Date getReadTime() 
    {
        return readTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("readId", getReadId())
            .append("messageId", getMessageId())
            .append("readerType", getReaderType())
            .append("readerId", getReaderId())
            .append("readStatus", getReadStatus())
            .append("readTime", getReadTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
