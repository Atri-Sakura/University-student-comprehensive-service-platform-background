package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品评价对象 goods_evaluation
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class GoodsEvaluation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评价唯一ID */
    private Long goodsEvaluationId;

    /** 商品ID */
    @Excel(name = "商品ID")
    private Long merchantGoodsId;

    /** 商家ID */
    @Excel(name = "商家ID")
    private Long merchantBaseId;

    /** 评价用户ID */
    @Excel(name = "评价用户ID")
    private Long userId;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderId;

    /** 关联订单项ID */
    @Excel(name = "关联订单项ID")
    private Long orderItemId;

    /** 商品评分(1-5分) */
    @Excel(name = "商品评分(1-5分)")
    private Long rating;

    /** 评价内容 */
    @Excel(name = "评价内容")
    private String content;

    /** 是否匿名：0-否 1-是 */
    @Excel(name = "是否匿名：0-否 1-是")
    private Long isAnonymous;

    /** 商家回复 */
    @Excel(name = "商家回复")
    private String merchantReply;

    /** 回复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "回复时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date replyTime;

    /** 有用数（点赞数） */
    @Excel(name = "有用数", readConverterExp = "点=赞数")
    private Long usefulCount;

    public void setGoodsEvaluationId(Long goodsEvaluationId) 
    {
        this.goodsEvaluationId = goodsEvaluationId;
    }

    public Long getGoodsEvaluationId() 
    {
        return goodsEvaluationId;
    }

    public void setMerchantGoodsId(Long merchantGoodsId) 
    {
        this.merchantGoodsId = merchantGoodsId;
    }

    public Long getMerchantGoodsId() 
    {
        return merchantGoodsId;
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

    public void setOrderItemId(Long orderItemId) 
    {
        this.orderItemId = orderItemId;
    }

    public Long getOrderItemId() 
    {
        return orderItemId;
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

    public void setIsAnonymous(Long isAnonymous) 
    {
        this.isAnonymous = isAnonymous;
    }

    public Long getIsAnonymous() 
    {
        return isAnonymous;
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

    public void setUsefulCount(Long usefulCount) 
    {
        this.usefulCount = usefulCount;
    }

    public Long getUsefulCount() 
    {
        return usefulCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("goodsEvaluationId", getGoodsEvaluationId())
            .append("merchantGoodsId", getMerchantGoodsId())
            .append("merchantBaseId", getMerchantBaseId())
            .append("userId", getUserId())
            .append("orderId", getOrderId())
            .append("orderItemId", getOrderItemId())
            .append("rating", getRating())
            .append("content", getContent())
            .append("isAnonymous", getIsAnonymous())
            .append("merchantReply", getMerchantReply())
            .append("createTime", getCreateTime())
            .append("replyTime", getReplyTime())
            .append("usefulCount", getUsefulCount())
            .toString();
    }
}
