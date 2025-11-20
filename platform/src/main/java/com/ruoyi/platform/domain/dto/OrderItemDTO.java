package com.ruoyi.platform.domain.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单商品明细DTO
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Data
public class OrderItemDTO {

    /** 商品ID */
    private Long goodsId;

    /** 商品名称 */
    private String goodsName;

    /** 商品单价 */
    private BigDecimal goodsPrice;

    /** 购买数量 */
    private Long quantity;

    /** 商品规格 */
    private String goodsSpec;

    /** 商品标签 */
    private String goodsTags;
}