package com.ruoyi.platform.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 商家报表对象 merchant_report
 *
 * @author ruoyi
 * @date 2025-10-24
 */
public class MerchantReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报表ID */
    private Long reportId;

    /** 商家基础ID */
    private Long merchantBaseId;

    /** 报表内容 */
    private String context;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    private Integer isImportant;

    public void setReportId(Long reportId)
    {
        this.reportId = reportId;
    }

    public Long getReportId()
    {
        return reportId;
    }

    public void setMerchantBaseId(Long merchantBaseId)
    {
        this.merchantBaseId = merchantBaseId;
    }

    public Long getMerchantBaseId()
    {
        return merchantBaseId;
    }

    public void setContext(String context)
    {
        this.context = context;
    }

    public String getContext()
    {
        return context;
    }

    public void setExpireTime(java.util.Date expireTime)
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime()
    {
        return expireTime;
    }

    public Date getCreatTime()
    {
        return createTime;
    }

    public void setCreatTime(Date creatTime)
    {
        this.createTime = creatTime;
    }

    public void setIsImportant(Integer isImportant){
    this.isImportant = isImportant;
    }

    public Integer getIsImportant(){
        return isImportant;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("reportId", getReportId())
                .append("merchantBaseId", getMerchantBaseId())
                .append("context", getContext())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("expireTime", getExpireTime())
                .toString();
    }
}