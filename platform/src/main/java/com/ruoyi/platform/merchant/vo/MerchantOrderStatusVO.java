package com.ruoyi.platform.merchant.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
/**
 * 商家订单统计DTO
 */
public class MerchantOrderStatusVO {

    /**
     * 商家名称
     */
    private String merchantName;

    /**
     * 订单量
     */
    private Integer orderCount;

    /**
     * 营业额
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

    // 默认构造方法
    public MerchantOrderStatusVO() {
    }

    @Override
    public String toString() {
        return "MerchantOrderStatsDTO{" +
                "merchantName='" + merchantName + '\'' +
                ", orderCount=" + orderCount +
                ", revenue=" + revenue +
                ", pendingCount=" + pendingCount +
                ", waitingDeliveryCount=" + waitingDeliveryCount +
                ", deliveringCount=" + deliveringCount +
                '}';
    }
}