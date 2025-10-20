package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商家评价对象 merchant_evaluation
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class MerchantEvaluation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评价唯一ID */
    private Long merchantEvaluationId;

    /** 所属商家ID */
    @Excel(name = "所属商家ID")
    private Long merchantBaseId;

    /** 评价用户ID */
    @Excel(name = "评价用户ID")
    private Long userId;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderId;

    /** 评分(1-5分) */
    @Excel(name = "评分(1-5分)")
    private Long rating;

    /** 口味评分(1-5分，仅餐饮类) */
    @Excel(name = "口味评分(1-5分，仅餐饮类)")
    private Long tasteScore;

    /** 包装评分(1-5分) */
    @Excel(name = "包装评分(1-5分)")
    private Long packageScore;

    /** 评价内容 */
    @Excel(name = "评价内容")
    private String content;

    /** 评价图片URL(逗号分隔) */
    @Excel(name = "评价图片URL(逗号分隔)")
    private String imgUrls;

    /** 商家回复 */
    @Excel(name = "商家回复")
    private String merchantReply;

    /** 回复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "回复时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date replyTime;

    public void setMerchantEvaluationId(Long merchantEvaluationId) 
    {
        this.merchantEvaluationId = merchantEvaluationId;
    }

    public Long getMerchantEvaluationId() 
    {
        return merchantEvaluationId;
    }

    public void setMerchantBaseId(Long merchantBaseId) 
    {
        this.merchantBaseId = merchantBaseId;
    }

    public Long getMerchantBaseId() 
    {
        return merchantBaseId;
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

    public void setTasteScore(Long tasteScore) 
    {
        this.tasteScore = tasteScore;
    }

    public Long getTasteScore() 
    {
        return tasteScore;
    }

    public void setPackageScore(Long packageScore) 
    {
        this.packageScore = packageScore;
    }

    public Long getPackageScore() 
    {
        return packageScore;
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

    public void setMerchantReply(String merchantReply) 
    {
        this.merchantReply = merchantReply;
    }

    public String getMerchantReply() 
    {
        return merchantReply;
    }

    public void setReplyTime(Date replyTime) 
    {
        this.replyTime = replyTime;
    }

    public Date getReplyTime() 
    {
        return replyTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("merchantEvaluationId", getMerchantEvaluationId())
            .append("merchantBaseId", getMerchantBaseId())
            .append("userId", getUserId())
            .append("orderId", getOrderId())
            .append("rating", getRating())
            .append("tasteScore", getTasteScore())
            .append("packageScore", getPackageScore())
            .append("content", getContent())
            .append("imgUrls", getImgUrls())
            .append("merchantReply", getMerchantReply())
            .append("createTime", getCreateTime())
            .append("replyTime", getReplyTime())
            .toString();
    }
}
