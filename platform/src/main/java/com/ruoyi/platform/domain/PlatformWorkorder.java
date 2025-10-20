package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客服工单对象 platform_workorder
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class PlatformWorkorder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工单唯一ID */
    private Long platformWorkorderId;

    /** 关联订单ID（来自order_db） */
    @Excel(name = "关联订单ID", readConverterExp = "来=自order_db")
    private Long orderId;

    /** 提交用户ID（来自user/rider/merchant_db） */
    @Excel(name = "提交用户ID", readConverterExp = "来=自user/rider/merchant_db")
    private Long userId;

    /** 用户类型：1-学生 2-骑手 3-商家 */
    @Excel(name = "用户类型：1-学生 2-骑手 3-商家")
    private Long userType;

    /** 用户昵称（冗余） */
    @Excel(name = "用户昵称", readConverterExp = "冗=余")
    private String userNickname;

    /** 工单内容（问题描述） */
    @Excel(name = "工单内容", readConverterExp = "问=题描述")
    private String content;

    /** 问题图片URL（逗号分隔） */
    @Excel(name = "问题图片URL", readConverterExp = "逗=号分隔")
    private String imgUrls;

    /** 工单类型：1-订单投诉 2-服务差评 3-系统故障 4-其他 */
    @Excel(name = "工单类型：1-订单投诉 2-服务差评 3-系统故障 4-其他")
    private Long workorderType;

    /** 处理人ID（关联platform_admin.platform_admin_id） */
    @Excel(name = "处理人ID", readConverterExp = "关=联platform_admin.platform_admin_id")
    private Long handlerId;

    /** 处理人姓名（冗余） */
    @Excel(name = "处理人姓名", readConverterExp = "冗=余")
    private String handlerName;

    /** 处理状态：0-待处理 1-处理中 2-已解决 3-已关闭 */
    @Excel(name = "处理状态：0-待处理 1-处理中 2-已解决 3-已关闭")
    private Long handleStatus;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String handleResult;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date handleTime;

    /** 关闭时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "关闭时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date closeTime;

    public void setPlatformWorkorderId(Long platformWorkorderId) 
    {
        this.platformWorkorderId = platformWorkorderId;
    }

    public Long getPlatformWorkorderId() 
    {
        return platformWorkorderId;
    }

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserType(Long userType) 
    {
        this.userType = userType;
    }

    public Long getUserType() 
    {
        return userType;
    }

    public void setUserNickname(String userNickname) 
    {
        this.userNickname = userNickname;
    }

    public String getUserNickname() 
    {
        return userNickname;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setImgUrls(String imgUrls) 
    {
        this.imgUrls = imgUrls;
    }

    public String getImgUrls() 
    {
        return imgUrls;
    }

    public void setWorkorderType(Long workorderType) 
    {
        this.workorderType = workorderType;
    }

    public Long getWorkorderType() 
    {
        return workorderType;
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

    public void setHandleStatus(Long handleStatus) 
    {
        this.handleStatus = handleStatus;
    }

    public Long getHandleStatus() 
    {
        return handleStatus;
    }

    public void setHandleResult(String handleResult) 
    {
        this.handleResult = handleResult;
    }

    public String getHandleResult() 
    {
        return handleResult;
    }

    public void setHandleTime(Date handleTime) 
    {
        this.handleTime = handleTime;
    }

    public Date getHandleTime() 
    {
        return handleTime;
    }

    public void setCloseTime(Date closeTime) 
    {
        this.closeTime = closeTime;
    }

    public Date getCloseTime() 
    {
        return closeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("platformWorkorderId", getPlatformWorkorderId())
            .append("orderId", getOrderId())
            .append("userId", getUserId())
            .append("userType", getUserType())
            .append("userNickname", getUserNickname())
            .append("content", getContent())
            .append("imgUrls", getImgUrls())
            .append("workorderType", getWorkorderType())
            .append("handlerId", getHandlerId())
            .append("handlerName", getHandlerName())
            .append("handleStatus", getHandleStatus())
            .append("handleResult", getHandleResult())
            .append("createTime", getCreateTime())
            .append("handleTime", getHandleTime())
            .append("closeTime", getCloseTime())
            .toString();
    }
}
