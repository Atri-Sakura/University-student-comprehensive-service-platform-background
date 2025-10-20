package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 骑手评价对象 rider_evaluation
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class RiderEvaluation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评价唯一ID */
    private Long riderEvaluationId;

    /** 骑手ID */
    @Excel(name = "骑手ID")
    private Long riderBaseId;

    /** 评价用户ID */
    @Excel(name = "评价用户ID")
    private Long userId;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderId;

    /** 评分(1-5分) */
    @Excel(name = "评分(1-5分)")
    private Long rating;

    /** 速度评分(1-5分) */
    @Excel(name = "速度评分(1-5分)")
    private Long speedScore;

    /** 态度评分(1-5分) */
    @Excel(name = "态度评分(1-5分)")
    private Long attitudeScore;

    /** 评价内容 */
    @Excel(name = "评价内容")
    private String content;

    public void setRiderEvaluationId(Long riderEvaluationId) 
    {
        this.riderEvaluationId = riderEvaluationId;
    }

    public Long getRiderEvaluationId() 
    {
        return riderEvaluationId;
    }

    public void setRiderBaseId(Long riderBaseId) 
    {
        this.riderBaseId = riderBaseId;
    }

    public Long getRiderBaseId() 
    {
        return riderBaseId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setRating(Long rating) 
    {
        this.rating = rating;
    }

    public Long getRating() 
    {
        return rating;
    }

    public void setSpeedScore(Long speedScore) 
    {
        this.speedScore = speedScore;
    }

    public Long getSpeedScore() 
    {
        return speedScore;
    }

    public void setAttitudeScore(Long attitudeScore) 
    {
        this.attitudeScore = attitudeScore;
    }

    public Long getAttitudeScore() 
    {
        return attitudeScore;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("riderEvaluationId", getRiderEvaluationId())
            .append("riderBaseId", getRiderBaseId())
            .append("userId", getUserId())
            .append("orderId", getOrderId())
            .append("rating", getRating())
            .append("speedScore", getSpeedScore())
            .append("attitudeScore", getAttitudeScore())
            .append("content", getContent())
            .append("createTime", getCreateTime())
            .toString();
    }
}
