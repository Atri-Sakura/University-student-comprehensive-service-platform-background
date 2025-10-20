package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 聊天消息（存储单条消息的核心信息）对象 chat_message
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class ChatMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 消息唯一ID（雪花算法生成，全局唯一） */
    private Long messageId;

    /** 所属会话ID（关联chat_session.session_id，聚合同一会话的消息） */
    @Excel(name = "所属会话ID", readConverterExp = "关=联chat_session.session_id，聚合同一会话的消息")
    private Long sessionId;

    /** 发送方类型：1-用户 2-骑手 3-商家 4-系统 */
    @Excel(name = "发送方类型：1-用户 2-骑手 3-商家 4-系统")
    private Long fromType;

    /** 发送方ID（系统消息from_id固定为0） */
    @Excel(name = "发送方ID", readConverterExp = "系=统消息from_id固定为0")
    private Long fromId;

    /** 接收方类型：1-用户 2-骑手 3-商家 */
    @Excel(name = "接收方类型：1-用户 2-骑手 3-商家")
    private Long toType;

    /** 接收方ID */
    @Excel(name = "接收方ID")
    private Long toId;

    /** 消息类型：1-文本 2-图片 3-语音 4-系统通知 */
    @Excel(name = "消息类型：1-文本 2-图片 3-语音 4-系统通知")
    private Long msgType;

    /** 消息内容：文本消息存内容；图片/语音存MinIO的URL；系统通知存模板内容 */
    @Excel(name = "消息内容：文本消息存内容；图片/语音存MinIO的URL；系统通知存模板内容")
    private String msgContent;

    /** 消息状态：0-发送中 1-已送达 2-已读 3-已撤回 4-发送失败 */
    @Excel(name = "消息状态：0-发送中 1-已送达 2-已读 3-已撤回 4-发送失败")
    private Long msgStatus;

    /** 消息发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "消息发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /** 消息送达时间（仅用于需确认送达的场景） */
    @Excel(name = "消息送达时间", readConverterExp = "仅=用于需确认送达的场景")
    private Date deliverTime;

    /** 消息已读时间（接收方点击后更新） */
    @Excel(name = "消息已读时间", readConverterExp = "接=收方点击后更新")
    private Date readTime;

    /** 是否删除（软删除）：0-未删除 1-已删除（仅对删除方隐藏） */
    @Excel(name = "是否删除", readConverterExp = "软=删除")
    private Long isDeleted;

    public void setMessageId(Long messageId) 
    {
        this.messageId = messageId;
    }

    public Long getMessageId() 
    {
        return messageId;
    }

    public void setSessionId(Long sessionId) 
    {
        this.sessionId = sessionId;
    }

    public Long getSessionId() 
    {
        return sessionId;
    }

    public void setFromType(Long fromType) 
    {
        this.fromType = fromType;
    }

    public Long getFromType() 
    {
        return fromType;
    }

    public void setFromId(Long fromId) 
    {
        this.fromId = fromId;
    }

    public Long getFromId() 
    {
        return fromId;
    }

    public void setToType(Long toType) 
    {
        this.toType = toType;
    }

    public Long getToType() 
    {
        return toType;
    }

    public void setToId(Long toId) 
    {
        this.toId = toId;
    }

    public Long getToId() 
    {
        return toId;
    }

    public void setMsgType(Long msgType) 
    {
        this.msgType = msgType;
    }

    public Long getMsgType() 
    {
        return msgType;
    }

    public void setMsgContent(String msgContent) 
    {
        this.msgContent = msgContent;
    }

    public String getMsgContent() 
    {
        return msgContent;
    }

    public void setMsgStatus(Long msgStatus) 
    {
        this.msgStatus = msgStatus;
    }

    public Long getMsgStatus() 
    {
        return msgStatus;
    }

    public void setSendTime(Date sendTime) 
    {
        this.sendTime = sendTime;
    }

    public Date getSendTime() 
    {
        return sendTime;
    }

    public void setDeliverTime(Date deliverTime) 
    {
        this.deliverTime = deliverTime;
    }

    public Date getDeliverTime() 
    {
        return deliverTime;
    }

    public void setReadTime(Date readTime) 
    {
        this.readTime = readTime;
    }

    public Date getReadTime() 
    {
        return readTime;
    }

    public void setIsDeleted(Long isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public Long getIsDeleted() 
    {
        return isDeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("messageId", getMessageId())
            .append("sessionId", getSessionId())
            .append("fromType", getFromType())
            .append("fromId", getFromId())
            .append("toType", getToType())
            .append("toId", getToId())
            .append("msgType", getMsgType())
            .append("msgContent", getMsgContent())
            .append("msgStatus", getMsgStatus())
            .append("sendTime", getSendTime())
            .append("deliverTime", getDeliverTime())
            .append("readTime", getReadTime())
            .append("isDeleted", getIsDeleted())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
