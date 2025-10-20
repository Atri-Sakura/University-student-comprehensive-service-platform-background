package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外卖订单明细（不含地址信息）对象 order_takeout_detail
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class OrderTakeoutDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细唯一ID */
    private Long orderTakeoutDetailId;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderMainId;

    /** 商家ID（关联merchant_db.merchant_base.merchant_base_id） */
    @Excel(name = "商家ID", readConverterExp = "关=联merchant_db.merchant_base.merchant_base_id")
    private Long merchantId;

    /** 商家名称(冗余) */
    @Excel(name = "商家名称(冗余)")
    private String merchantName;

    /** 商品ID（关联merchant_db.merchant_goods.merchant_goods_id） */
    @Excel(name = "商品ID", readConverterExp = "关=联merchant_db.merchant_goods.merchant_goods_id")
    private Long goodsId;

    /** 商品名称(冗余) */
    @Excel(name = "商品名称(冗余)")
    private String goodsName;

    /** 商品单价 */
    @Excel(name = "商品单价")
    private BigDecimal goodsPrice;

    /** 购买数量 */
    @Excel(name = "购买数量")
    private Long quantity;

    /** 小计金额 */
    @Excel(name = "小计金额")
    private BigDecimal subtotal;

    /** 商品规格（如“中杯/少糖”） */
    @Excel(name = "商品规格", readConverterExp = "如=“中杯/少糖”")
    private String goodsSpec;

    /** 商品标签(冗余，如“甜口/冰饮”，用于推荐) */
    @Excel(name = "商品标签(冗余，如“甜口/冰饮”，用于推荐)")
    private String goodsTags;

    public void setOrderTakeoutDetailId(Long orderTakeoutDetailId) 
    {
        this.orderTakeoutDetailId = orderTakeoutDetailId;
    }

    public Long getOrderTakeoutDetailId() 
    {
        return orderTakeoutDetailId;
    }

    public void setOrderMainId(Long orderMainId) 
    {
        this.orderMainId = orderMainId;
    }

    public Long getOrderMainId() 
    {
        return orderMainId;
    }

    public void setMerchantId(Long merchantId) 
    {
        this.merchantId = merchantId;
    }

    public Long getMerchantId() 
    {
        return merchantId;
    }

    public void setMerchantName(String merchantName) 
    {
        this.merchantName = merchantName;
    }

    public String getMerchantName() 
    {
        return merchantName;
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

    public void setGoodsPrice(BigDecimal goodsPrice) 
    {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getGoodsPrice() 
    {
        return goodsPrice;
    }

    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }

    public void setSubtotal(BigDecimal subtotal) 
    {
        this.subtotal = subtotal;
    }

    public BigDecimal getSubtotal() 
    {
        return subtotal;
    }

    public void setGoodsSpec(String goodsSpec) 
    {
        this.goodsSpec = goodsSpec;
    }

    public String getGoodsSpec() 
    {
        return goodsSpec;
    }

    public void setGoodsTags(String goodsTags) 
    {
        this.goodsTags = goodsTags;
    }

    public String getGoodsTags() 
    {
        return goodsTags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderTakeoutDetailId", getOrderTakeoutDetailId())
            .append("orderMainId", getOrderMainId())
            .append("merchantId", getMerchantId())
            .append("merchantName", getMerchantName())
            .append("goodsId", getGoodsId())
            .append("goodsName", getGoodsName())
            .append("goodsPrice", getGoodsPrice())
            .append("quantity", getQuantity())
            .append("subtotal", getSubtotal())
            .append("goodsSpec", getGoodsSpec())
            .append("goodsTags", getGoodsTags())
            .toString();
    }
}
