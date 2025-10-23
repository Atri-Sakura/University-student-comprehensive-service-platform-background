package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商家活动对象 merchant_activity
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class MerchantActivity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动唯一ID */
    private Long merchantActivityId;

    /** 所属商家ID */
    @Excel(name = "所属商家ID")
    private Long merchantBaseId;

    /** 活动名称 */
    @Excel(name = "活动名称")
    private String activityName;

    /** 活动类型 */
    @Excel(name = "活动类型")
    private String activityType;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 活动内容 */
    @Excel(name = "活动内容")
    private String content;

    /** 状态：0-未开始 1-进行中 2-已结束 */
    @Excel(name = "状态：0-未开始 1-进行中 2-已结束")
    private Long status;

    public void setMerchantActivityId(Long merchantActivityId) 
    {
        this.merchantActivityId = merchantActivityId;
    }

    public Long getMerchantActivityId() 
    {
        return merchantActivityId;
    }

    public void setMerchantBaseId(Long merchantBaseId) 
    {
        this.merchantBaseId = merchantBaseId;
    }

    public Long getMerchantBaseId() 
    {
        return merchantBaseId;
    }

    public void setActivityName(String activityName) 
    {
        this.activityName = activityName;
    }

    public String getActivityName() 
    {
        return activityName;
    }

    public void setActivityType(String activityType) 
    {
        this.activityType = activityType;
    }

    public String getActivityType() 
    {
        return activityType;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("merchantActivityId", getMerchantActivityId())
            .append("merchantBaseId", getMerchantBaseId())
            .append("activityName", getActivityName())
            .append("activityType", getActivityType())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("content", getContent())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
