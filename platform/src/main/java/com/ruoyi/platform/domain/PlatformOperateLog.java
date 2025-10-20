package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 系统操作日志对象 platform_operate_log
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class PlatformOperateLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志唯一ID */
    private Long platformOperateLogId;

    /** 操作管理员ID（关联platform_admin.platform_admin_id） */
    @Excel(name = "操作管理员ID", readConverterExp = "关=联platform_admin.platform_admin_id")
    private Long adminId;

    /** 管理员姓名（冗余） */
    @Excel(name = "管理员姓名", readConverterExp = "冗=余")
    private String adminName;

    /** 操作类型（create/update/delete/audit） */
    @Excel(name = "操作类型", readConverterExp = "c=reate/update/delete/audit")
    private String operType;

    /** 操作模块（merchant/order/user/rider） */
    @Excel(name = "操作模块", readConverterExp = "m=erchant/order/user/rider")
    private String operModule;

    /** 操作内容（如"审核商家ID=123通过"） */
    @Excel(name = "操作内容", readConverterExp = "如=审核商家ID=123通过")
    private String operContent;

    /** 操作IP地址 */
    @Excel(name = "操作IP地址")
    private String ipAddress;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operTime;

    /** 用户代理信息（浏览器/设备） */
    @Excel(name = "用户代理信息", readConverterExp = "浏=览器/设备")
    private String userAgent;

    public void setPlatformOperateLogId(Long platformOperateLogId) 
    {
        this.platformOperateLogId = platformOperateLogId;
    }

    public Long getPlatformOperateLogId() 
    {
        return platformOperateLogId;
    }

    public void setAdminId(Long adminId) 
    {
        this.adminId = adminId;
    }

    public Long getAdminId() 
    {
        return adminId;
    }

    public void setAdminName(String adminName) 
    {
        this.adminName = adminName;
    }

    public String getAdminName() 
    {
        return adminName;
    }

    public void setOperType(String operType) 
    {
        this.operType = operType;
    }

    public String getOperType() 
    {
        return operType;
    }

    public void setOperModule(String operModule) 
    {
        this.operModule = operModule;
    }

    public String getOperModule() 
    {
        return operModule;
    }

    public void setOperContent(String operContent) 
    {
        this.operContent = operContent;
    }

    public String getOperContent() 
    {
        return operContent;
    }

    public void setIpAddress(String ipAddress) 
    {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() 
    {
        return ipAddress;
    }

    public void setOperTime(Date operTime) 
    {
        this.operTime = operTime;
    }

    public Date getOperTime() 
    {
        return operTime;
    }

    public void setUserAgent(String userAgent) 
    {
        this.userAgent = userAgent;
    }

    public String getUserAgent() 
    {
        return userAgent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("platformOperateLogId", getPlatformOperateLogId())
            .append("adminId", getAdminId())
            .append("adminName", getAdminName())
            .append("operType", getOperType())
            .append("operModule", getOperModule())
            .append("operContent", getOperContent())
            .append("ipAddress", getIpAddress())
            .append("operTime", getOperTime())
            .append("userAgent", getUserAgent())
            .toString();
    }
}
