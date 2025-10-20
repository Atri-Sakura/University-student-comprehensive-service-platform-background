package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 跑腿订单明细（不含地址信息）对象 order_errand_detail
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class OrderErrandDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细唯一ID */
    private Long orderErrandDetailId;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderMainId;

    /** 跑腿类型：1-帮我送 2-帮我买 */
    @Excel(name = "跑腿类型：1-帮我送 2-帮我买")
    private Long errandType;

    /** 物品/商品描述（如“生日蛋糕/6寸”“ textbooks/高等数学”） */
    @Excel(name = "物品/商品描述", readConverterExp = "如=“生日蛋糕/6寸”“,t=extbooks/高等数学”")
    private String goodsDesc;

    /** 期望送达时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "期望送达时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectedTime;

    /** 骑手垫付金额（帮我买场景专用） */
    @Excel(name = "骑手垫付金额", readConverterExp = "帮=我买场景专用")
    private BigDecimal advanceAmount;

    /** 小费金额 */
    @Excel(name = "小费金额")
    private BigDecimal tipAmount;

    /** 代付凭证 */
    @Excel(name = "代付凭证")
    private String buyPhotoUrl;

    public void setOrderErrandDetailId(Long orderErrandDetailId) 
    {
        this.orderErrandDetailId = orderErrandDetailId;
    }

    public Long getOrderErrandDetailId() 
    {
        return orderErrandDetailId;
    }

    public void setOrderMainId(Long orderMainId) 
    {
        this.orderMainId = orderMainId;
    }

    public Long getOrderMainId() 
    {
        return orderMainId;
    }

    public void setErrandType(Long errandType) 
    {
        this.errandType = errandType;
    }

    public Long getErrandType() 
    {
        return errandType;
    }

    public void setGoodsDesc(String goodsDesc) 
    {
        this.goodsDesc = goodsDesc;
    }

    public String getGoodsDesc() 
    {
        return goodsDesc;
    }

    public void setExpectedTime(Date expectedTime) 
    {
        this.expectedTime = expectedTime;
    }

    public Date getExpectedTime() 
    {
        return expectedTime;
    }

    public void setAdvanceAmount(BigDecimal advanceAmount) 
    {
        this.advanceAmount = advanceAmount;
    }

    public BigDecimal getAdvanceAmount() 
    {
        return advanceAmount;
    }

    public void setTipAmount(BigDecimal tipAmount) 
    {
        this.tipAmount = tipAmount;
    }

    public BigDecimal getTipAmount() 
    {
        return tipAmount;
    }

    public void setBuyPhotoUrl(String buyPhotoUrl) 
    {
        this.buyPhotoUrl = buyPhotoUrl;
    }

    public String getBuyPhotoUrl() 
    {
        return buyPhotoUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderErrandDetailId", getOrderErrandDetailId())
            .append("orderMainId", getOrderMainId())
            .append("errandType", getErrandType())
            .append("goodsDesc", getGoodsDesc())
            .append("expectedTime", getExpectedTime())
            .append("advanceAmount", getAdvanceAmount())
            .append("tipAmount", getTipAmount())
            .append("buyPhotoUrl", getBuyPhotoUrl())
            .toString();
    }
}
