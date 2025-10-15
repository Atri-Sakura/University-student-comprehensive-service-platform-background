package com.gzu.entity.order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderSecondhandDetail {
    private Long orderSecondhandDetailId;
    private Long orderMainId;
    private Long goodsId;
    private String goodsName;
    private Long sellerId;
    private Integer sellWay;
    private String sellerNickname;
    private BigDecimal depositAmount;
    private Date confirmTime;
    private Integer evaluateStatus;
}