package com.ruoyi.platform.domain.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预支付订单DTO
 *
 * @author Moli2580
 * @date 2025-11-18
 */
@Data
public class PrePayOrderDTO {

    /** 预订单号 */
    private String preOrderNo;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 实付金额 */
    private BigDecimal payAmount;

    /** 商品金额 */
    private BigDecimal goodsAmount;

    /** 配送费 */
    private BigDecimal deliveryFee;

    /** 优惠金额 */
    private BigDecimal discountAmount;

    /** 过期时间（15分钟后） */
    private Date expireTime;

    /** 创建时间 */
    private Date createTime;
}