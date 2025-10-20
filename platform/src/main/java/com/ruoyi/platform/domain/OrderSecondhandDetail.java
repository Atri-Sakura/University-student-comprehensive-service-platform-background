package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 二手交易订单明细（不含地址信息）对象 order_secondhand_detail
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class OrderSecondhandDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细唯一ID */
    private Long orderSecondhandDetailId;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderMainId;

    /** 二手商品ID */
    @Excel(name = "二手商品ID")
    private Long goodsId;

    /** 商品名称(冗余) */
    @Excel(name = "商品名称(冗余)")
    private String goodsName;

    /** 卖家ID（关联user_db.user_base.user_base_id） */
    @Excel(name = "卖家ID", readConverterExp = "关=联user_db.user_base.user_base_id")
    private Long sellerId;

    /** 交易方式：1-线上 2-线下 */
    @Excel(name = "交易方式：1-线上 2-线下")
    private Long sellWay;

    /** 卖家昵称(冗余) */
    @Excel(name = "卖家昵称(冗余)")
    private String sellerNickname;

    /** 担保金金额 */
    @Excel(name = "担保金金额")
    private BigDecimal depositAmount;

    /** 买家确认收货时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "买家确认收货时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date confirmTime;

    /** 评价状态：0-未评价 1-已评价 */
    @Excel(name = "评价状态：0-未评价 1-已评价")
    private Long evaluateStatus;

    public void setOrderSecondhandDetailId(Long orderSecondhandDetailId) 
    {
        this.orderSecondhandDetailId = orderSecondhandDetailId;
    }

    public Long getOrderSecondhandDetailId() 
    {
        return orderSecondhandDetailId;
    }

    public void setOrderMainId(Long orderMainId) 
    {
        this.orderMainId = orderMainId;
    }

    public Long getOrderMainId() 
    {
        return orderMainId;
    }

    public void setGoodsId(Long goodsId) 
    {
        this.goodsId = goodsId;
    }

    public Long getGoodsId() 
    {
        return goodsId;
    }

    public void setGoodsName(String goodsName) 
    {
        this.goodsName = goodsName;
    }

    public String getGoodsName() 
    {
        return goodsName;
    }

    public void setSellerId(Long sellerId) 
    {
        this.sellerId = sellerId;
    }

    public Long getSellerId() 
    {
        return sellerId;
    }

    public void setSellWay(Long sellWay) 
    {
        this.sellWay = sellWay;
    }

    public Long getSellWay() 
    {
        return sellWay;
    }

    public void setSellerNickname(String sellerNickname) 
    {
        this.sellerNickname = sellerNickname;
    }

    public String getSellerNickname() 
    {
        return sellerNickname;
    }

    public void setDepositAmount(BigDecimal depositAmount) 
    {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getDepositAmount() 
    {
        return depositAmount;
    }

    public void setConfirmTime(Date confirmTime) 
    {
        this.confirmTime = confirmTime;
    }

    public Date getConfirmTime() 
    {
        return confirmTime;
    }

    public void setEvaluateStatus(Long evaluateStatus) 
    {
        this.evaluateStatus = evaluateStatus;
    }

    public Long getEvaluateStatus() 
    {
        return evaluateStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderSecondhandDetailId", getOrderSecondhandDetailId())
            .append("orderMainId", getOrderMainId())
            .append("goodsId", getGoodsId())
            .append("goodsName", getGoodsName())
            .append("sellerId", getSellerId())
            .append("sellWay", getSellWay())
            .append("sellerNickname", getSellerNickname())
            .append("depositAmount", getDepositAmount())
            .append("confirmTime", getConfirmTime())
            .append("evaluateStatus", getEvaluateStatus())
            .toString();
    }
}
