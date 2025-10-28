package com.ruoyi.platform.merchant.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * 订单统计VO
 */
@Data
public class OrderStatsVO {
    /**
     * 订单量
     */
    private Integer orderCount;

    /**
     * 营业额
     */
    private BigDecimal totalRevenue;

    /**
     * 实收金额
     */
    private BigDecimal actualIncome;

    /**
     * 单均价
     */
    private BigDecimal avgOrderAmount;

    /**
     * 较昨日订单量变化百分比
     */
    private BigDecimal orderCountChangePercent;

    /**
     * 较昨日营业额变化百分比
     */
    private BigDecimal revenueChangePercent;

    /**
     * 较昨日实收金额变化百分比
     */
    private BigDecimal incomeChangePercent;

    /**
     * 较昨日单均价变化百分比
     */
    private BigDecimal avgAmountChangePercent;

    /**
     * 统计日期
     */
    private LocalDateTime statsDate;


    public OrderStatsVO(Integer orderCount, BigDecimal totalRevenue, BigDecimal actualIncome, BigDecimal avgOrderAmount) {
        this.orderCount = orderCount;
        this.totalRevenue = totalRevenue;
        this.actualIncome = actualIncome;
        this.avgOrderAmount = avgOrderAmount;
    }
    public OrderStatsVO() {
        this.orderCount = 0;
        this.totalRevenue = BigDecimal.ZERO;
        this.actualIncome = BigDecimal.ZERO;
        this.avgOrderAmount = BigDecimal.ZERO;
        this.orderCountChangePercent = BigDecimal.ZERO;
        this.revenueChangePercent = BigDecimal.ZERO;
        this.incomeChangePercent = BigDecimal.ZERO;
        this.avgAmountChangePercent = BigDecimal.ZERO;
    }
}