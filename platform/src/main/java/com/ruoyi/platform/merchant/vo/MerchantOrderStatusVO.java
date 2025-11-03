package com.ruoyi.platform.merchant.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 商家订单状态VO
 */
@Data
public class MerchantOrderStatusVO {
    /**
     * 商家名称
     */
    private String merchantName;

    /**
     * 总订单量
     */
    private Integer orderCount;

    /**
     * 总营业额
     */
    private BigDecimal revenue;

    /**
     * 待处理数量
     */
    private Integer pendingCount;

    /**
     * 待配送数量
     */
    private Integer waitingDeliveryCount;

    /**
     * 配送中数量
     */
    private Integer deliveringCount;

    /**
     * 今日订单量
     */
    private Integer todayOrderCount;

    /**
     * 今日营业额
     */
    private BigDecimal todayRevenue;

    /**
     * 较昨日订单量变化百分比
     */
    private BigDecimal orderCountChangePercent;

    /**
     * 较昨日营业额变化百分比
     */
    private BigDecimal revenueChangePercent;
}