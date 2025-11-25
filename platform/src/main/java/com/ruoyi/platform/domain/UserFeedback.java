package com.ruoyi.platform.domain;

import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户反馈对象 user_feedback
 *
 * @author ruoyi
 * @date 2025-11-25
 */
public class UserFeedback extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 反馈唯一ID */
    private Long feedbackId;

    /** 用户类型：1-学生用户 2-骑手 3-商家 */
    @Excel(name = "用户类型", readConverterExp = "1=学生用户,2=骑手,3=商家")
    private Integer userType;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户昵称（冗余） */
    @Excel(name = "用户昵称")
    private String userNickname;

    /** 反馈标题 */
    @Excel(name = "反馈标题")
    private String feedbackTitle;

    /** 反馈详情 */
    @Excel(name = "反馈详情")
    private String feedbackContent;

    /** 反馈类型：1-功能建议 2-bug反馈 3-投诉建议 4-其他 */
    @Excel(name = "反馈类型", readConverterExp = "1=功能建议,2=bug反馈,3=投诉建议,4=其他")
    private Integer feedbackType;

    /** 联系方式（手机号或邮箱） */
    @Excel(name = "联系方式")
    private String contactInfo;

    /** 反馈图片URL（逗号分隔） */
    private String imgUrls;

    /** 处理状态：0-待处理 1-处理中 2-已解决 3-已关闭 */
    @Excel(name = "处理状态", readConverterExp = "0=待处理,1=处理中,2=已解决,3=已关闭")
    private Integer status;

    /** 管理员回复 */
    @Excel(name = "管理员回复")
    private String adminReply;

    /** 处理人ID */
    private Long handlerId;

    /** 处理人姓名 */
    @Excel(name = "处理人姓名")
    private String handlerName;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    public void setFeedbackId(Long feedbackId)
    {
        this.feedbackId = feedbackId;
    }

    public Long getFeedbackId()
    {
        return feedbackId;
    }

    public void setUserType(Integer userType)
    {
        this.userType = userType;
    }

    public Integer getUserType()
    {
        return userType;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserNickname(String userNickname)
    {
        this.userNickname = userNickname;
    }

    public String getUserNickname()
    {
        return userNickname;
    }

    public void setFeedbackTitle(String feedbackTitle)
    {
        this.feedbackTitle = feedbackTitle;
    }

    public String getFeedbackTitle()
    {
        return feedbackTitle;
    }

    public void setFeedbackContent(String feedbackContent)
    {
        this.feedbackContent = feedbackContent;
    }

    public String getFeedbackContent()
    {
        return feedbackContent;
    }

    public void setFeedbackType(Integer feedbackType)
    {
        this.feedbackType = feedbackType;
    }

    public Integer getFeedbackType()
    {
        return feedbackType;
    }

    public void setContactInfo(String contactInfo)
    {
        this.contactInfo = contactInfo;
    }

    public String getContactInfo()
    {
        return contactInfo;
    }

    public void setImgUrls(String imgUrls)
    {
        this.imgUrls = imgUrls;
    }

    public String getImgUrls()
    {
        return imgUrls;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setAdminReply(String adminReply)
    {
        this.adminReply = adminReply;
    }

    public String getAdminReply()
    {
        return adminReply;
    }

    public void setHandlerId(Long handlerId)
    {
        this.handlerId = handlerId;
    }

    public Long getHandlerId()
    {
        return handlerId;
    }

    public void setHandlerName(String handlerName)
    {
        this.handlerName = handlerName;
    }

    public String getHandlerName()
    {
        return handlerName;
    }

    public void setHandleTime(Date handleTime)
    {
        this.handleTime = handleTime;
    }

    public Date getHandleTime()
    {
        return handleTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("feedbackId", getFeedbackId())
                .append("userType", getUserType())
                .append("userId", getUserId())
                .append("userNickname", getUserNickname())
                .append("feedbackTitle", getFeedbackTitle())
                .append("feedbackContent", getFeedbackContent())
                .append("feedbackType", getFeedbackType())
                .append("contactInfo", getContactInfo())
                .append("imgUrls", getImgUrls())
                .append("status", getStatus())
                .append("adminReply", getAdminReply())
                .append("handlerId", getHandlerId())
                .append("handlerName", getHandlerName())
                .append("createTime", getCreateTime())
                .append("handleTime", getHandleTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}