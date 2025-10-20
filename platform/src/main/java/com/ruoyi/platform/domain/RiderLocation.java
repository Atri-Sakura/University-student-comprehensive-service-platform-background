package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 骑手位置对象 rider_location
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class RiderLocation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 位置记录唯一ID */
    private Long riderLocationId;

    /** 所属骑手ID */
    @Excel(name = "所属骑手ID")
    private Long riderBaseId;

    /** 当前经度 */
    @Excel(name = "当前经度")
    private BigDecimal longitude;

    /** 当前纬度 */
    @Excel(name = "当前纬度")
    private BigDecimal latitude;

    public void setRiderLocationId(Long riderLocationId) 
    {
        this.riderLocationId = riderLocationId;
    }

    public Long getRiderLocationId() 
    {
        return riderLocationId;
    }

    public void setRiderBaseId(Long riderBaseId) 
    {
        this.riderBaseId = riderBaseId;
    }

    public Long getRiderBaseId() 
    {
        return riderBaseId;
    }

    public void setLongitude(BigDecimal longitude) 
    {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude() 
    {
        return longitude;
    }

    public void setLatitude(BigDecimal latitude) 
    {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude() 
    {
        return latitude;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("riderLocationId", getRiderLocationId())
            .append("riderBaseId", getRiderBaseId())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
