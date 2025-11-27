package com.ruoyi.platform. domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache. commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder. ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 骑手评价对象 rider_evaluation
 *
 * @author ruoyi
 * @date 2025-11-26
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

    /** 用户昵称 (冗余字段，从user_base表查询) */
    @Excel(name = "用户昵称")
    private String userNickname;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderId;

    /** 订单编号 (冗余字段，从order_main表查询) */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 评分(1-5分) */
    @Excel(name = "评分")
    private Long rating;

    /** 评价内容 */
    @Excel(name = "评价内容")
    private String content;

    /** 评价时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "评价时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // Getter and Setter
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

    public void setUserNickname(String userNickname)
    {
        this.userNickname = userNickname;
    }

    public String getUserNickname()
    {
        return userNickname;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderNo(String orderNo)
    {
        this. orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setRating(Long rating)
    {
        this.rating = rating;
    }

    public Long getRating()
    {
        return rating;
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
    public Date getCreateTime()
    {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                . append("riderEvaluationId", getRiderEvaluationId())
                .append("riderBaseId", getRiderBaseId())
                .append("userId", getUserId())
                .append("userNickname", getUserNickname())
                .append("orderId", getOrderId())
                .append("orderNo", getOrderNo())
                .append("rating", getRating())
                .append("content", getContent())
                .append("createTime", getCreateTime())
                .toString();
    }
}