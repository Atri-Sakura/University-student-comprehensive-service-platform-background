package com.gzu.entity.order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderPayRecord {
    private Long orderPayRecordId;
    private Long orderMainId;
    private String payNo;
    private BigDecimal payAmount;
    private Integer payType;
    private Integer payStatus;
    private Date payTime;
    private String callbackData;
}