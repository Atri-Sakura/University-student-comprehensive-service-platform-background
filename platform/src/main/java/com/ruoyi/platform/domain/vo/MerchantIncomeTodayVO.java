package com.ruoyi.platform.domain.vo;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 今日收入统计 VO
 *
 * @author Jinx
 * @date 2025-10-25
 */
@Data
public class MerchantIncomeTodayVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 今日总收入 */
    private BigDecimal totalIncome;

    /** 今日订单数 */
    private Integer orderCount;

    /** 今日退款金额 */
    private BigDecimal refundAmount;

    /** 今日净收入（=总收入-退款） */
    private BigDecimal netIncome;

    @Override
    public String toString()
    {
        return "MerchantIncomeTodayVO{" +
                "totalIncome=" + totalIncome +
                ", orderCount=" + orderCount +
                ", refundAmount=" + refundAmount +
                ", netIncome=" + netIncome +
                '}';
    }
}
