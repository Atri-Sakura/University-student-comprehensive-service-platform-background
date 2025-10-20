package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 聊天会话（管理双方的聊天窗口关系）对象 chat_session
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class ChatSession extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会话唯一ID（雪花算法生成，全局唯一） */
    private Long sessionId;

    /** 发送方类型：1-用户 2-骑手 3-商家 */
    @Excel(name = "发送方类型：1-用户 2-骑手 3-商家")
    private Long fromType;

    /** 发送方ID（关联user_db.user_id/ridder_db.ridder_id/merchant_db.merchant_base_id） */
    @Excel(name = "发送方ID", readConverterExp = "关=联user_db.user_id/ridder_db.ridder_id/merchant_db.merchant_base_id")
    private Long fromId;

    /** 接收方类型：1-用户 2-骑手 3-商家 */
    @Excel(name = "接收方类型：1-用户 2-骑手 3-商家")
    private Long toType;

    /** 接收方ID（关联对应业务库的主键） */
    @Excel(name = "接收方ID", readConverterExp = "关=联对应业务库的主键")
    private Long toId;

    /** 最后一条消息的ID（关联chat_message.message_id） */
    @Excel(name = "最后一条消息的ID", readConverterExp = "关=联chat_message.message_id")
    private Long lastMsgId;

    /** 最后一条消息内容（冗余，用于会话列表快速展示） */
    @Excel(name = "最后一条消息内容", readConverterExp = "冗=余，用于会话列表快速展示")
    private String lastMsgContent;

    /** 最后一条消息类型：1-文本 2-图片 3-语音 4-系统通知 */
    @Excel(name = "最后一条消息类型：1-文本 2-图片 3-语音 4-系统通知")
    private Long lastMsgType;

    /** 最后一条消息发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后一条消息发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastMsgTime;

    /** 未读消息数（接收方视角，如用户A的会话中未读数量） */
    @Excel(name = "未读消息数", readConverterExp = "接=收方视角，如用户A的会话中未读数量")
    private Long unreadCount;

    /** 会话状态：0-已删除 1-正常 2-已屏蔽 */
    @Excel(name = "会话状态：0-已删除 1-正常 2-已屏蔽")
    private Long sessionStatus;

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

    public void setLastMsgId(Long lastMsgId) 
    {
        this.lastMsgId = lastMsgId;
    }

    public Long getLastMsgId() 
    {
        return lastMsgId;
    }

    public void setLastMsgContent(String lastMsgContent) 
    {
        this.lastMsgContent = lastMsgContent;
    }

    public String getLastMsgContent() 
    {
        return lastMsgContent;
    }

    public void setLastMsgType(Long lastMsgType) 
    {
        this.lastMsgType = lastMsgType;
    }

    public Long getLastMsgType() 
    {
        return lastMsgType;
    }

    public void setLastMsgTime(Date lastMsgTime) 
    {
        this.lastMsgTime = lastMsgTime;
    }

    public Date getLastMsgTime() 
    {
        return lastMsgTime;
    }

    public void setUnreadCount(Long unreadCount) 
    {
        this.unreadCount = unreadCount;
    }

    public Long getUnreadCount() 
    {
        return unreadCount;
    }

    public void setSessionStatus(Long sessionStatus) 
    {
        this.sessionStatus = sessionStatus;
    }

    public Long getSessionStatus() 
    {
        return sessionStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sessionId", getSessionId())
            .append("fromType", getFromType())
            .append("fromId", getFromId())
            .append("toType", getToType())
            .append("toId", getToId())
            .append("lastMsgId", getLastMsgId())
            .append("lastMsgContent", getLastMsgContent())
            .append("lastMsgType", getLastMsgType())
            .append("lastMsgTime", getLastMsgTime())
            .append("unreadCount", getUnreadCount())
            .append("sessionStatus", getSessionStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
