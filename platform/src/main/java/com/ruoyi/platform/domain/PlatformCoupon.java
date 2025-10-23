package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 平台优惠券对象 platform_coupon
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class PlatformCoupon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 优惠券ID */
    private Long platformCouponId;

    /** 优惠券编号 */
    @Excel(name = "优惠券编号")
    private String couponNo;

    /** 优惠券名称 */
    @Excel(name = "优惠券名称")
    private String couponName;

    /** 类型：1-满减券 2-折扣券 3-固定金额券 */
    @Excel(name = "类型：1-满减券 2-折扣券 3-固定金额券")
    private Long couponType;

    /** 面值 */
    @Excel(name = "面值")
    private BigDecimal faceValue;

    /** 最低消费金额 */
    @Excel(name = "最低消费金额")
    private BigDecimal minSpend;

    /** 折扣率（如0.85=85折） */
    @Excel(name = "折扣率", readConverterExp = "如=0.85=85折")
    private BigDecimal discount;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 总发行量 */
    @Excel(name = "总发行量")
    private Long totalCount;

    /** 剩余数量 */
    @Excel(name = "剩余数量")
    private Long remainCount;

    /** 状态：0-未发布 1-已发布 2-已过期 */
    @Excel(name = "状态：0-未发布 1-已发布 2-已过期")
    private Long status;

    public void setPlatformCouponId(Long platformCouponId) 
    {
        this.platformCouponId = platformCouponId;
    }

    public Long getPlatformCouponId() 
    {
        return platformCouponId;
    }

    public void setCouponNo(String couponNo) 
    {
        this.couponNo = couponNo;
    }

    public String getCouponNo() 
    {
        return couponNo;
    }

    public void setCouponName(String couponName) 
    {
        this.couponName = couponName;
    }

    public String getCouponName() 
    {
        return couponName;
    }

    public void setCouponType(Long couponType) 
    {
        this.couponType = couponType;
    }

    public Long getCouponType() 
    {
        return couponType;
    }

    public void setFaceValue(BigDecimal faceValue) 
    {
        this.faceValue = faceValue;
    }

    public BigDecimal getFaceValue() 
    {
        return faceValue;
    }

    public void setMinSpend(BigDecimal minSpend) 
    {
        this.minSpend = minSpend;
    }

    public BigDecimal getMinSpend() 
    {
        return minSpend;
    }

    public void setDiscount(BigDecimal discount) 
    {
        this.discount = discount;
    }

    public BigDecimal getDiscount() 
    {
        return discount;
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

    public void setTotalCount(Long totalCount) 
    {
        this.totalCount = totalCount;
    }

    public Long getTotalCount() 
    {
        return totalCount;
    }

    public void setRemainCount(Long remainCount) 
    {
        this.remainCount = remainCount;
    }

    public Long getRemainCount() 
    {
        return remainCount;
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
            .append("platformCouponId", getPlatformCouponId())
            .append("couponNo", getCouponNo())
            .append("couponName", getCouponName())
            .append("couponType", getCouponType())
            .append("faceValue", getFaceValue())
            .append("minSpend", getMinSpend())
            .append("discount", getDiscount())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("totalCount", getTotalCount())
            .append("remainCount", getRemainCount())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
