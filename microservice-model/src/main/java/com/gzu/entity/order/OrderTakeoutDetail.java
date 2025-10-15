package com.gzu.entity.order;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderTakeoutDetail {
    private Long orderTakeoutDetailId;
    private Long orderMainId;
    private Long merchantId;
    private String merchantName;
    private Long goodsId;
    private String goodsName;
    private BigDecimal goodsPrice;
    private Integer quantity;
    private BigDecimal subtotal;
    private String goodsSpec;
    private String goodsTags;
}