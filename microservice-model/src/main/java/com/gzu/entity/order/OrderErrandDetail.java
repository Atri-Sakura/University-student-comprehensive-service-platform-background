package com.gzu.entity.order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderErrandDetail {
    private Long orderErrandDetailId;
    private Long orderMainId;
    private Integer errandType;
    private String goodsDesc;
    private Date expectedTime;
    private BigDecimal advanceAmount;
    private BigDecimal tipAmount;
    private String buyPhotoUrl;
}