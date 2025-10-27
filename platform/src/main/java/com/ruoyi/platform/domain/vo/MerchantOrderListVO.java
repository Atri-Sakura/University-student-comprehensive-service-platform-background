package com.ruoyi.platform.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家订单列表项 VO
 */
@Data
public class MerchantOrderListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderNo;
    private Date orderTime;
    private String customerName;
    private BigDecimal orderAmount;
    private BigDecimal discount;
    private BigDecimal actualIncome;
    private BigDecimal platformFee;
    private BigDecimal deliveryFee;
    private String status;

    @Override
    public String toString()
    {
        return "MerchantOrderListVO{" +
                "orderNo='" + orderNo + '\'' +
                ", orderTime=" + orderTime +
                ", customerName='" + customerName + '\'' +
                ", orderAmount=" + orderAmount +
                ", discount=" + discount +
                ", actualIncome=" + actualIncome +
                ", platformFee=" + platformFee +
                ", deliveryFee=" + deliveryFee +
                ", status='" + status + '\'' +
                '}';
    }

}
