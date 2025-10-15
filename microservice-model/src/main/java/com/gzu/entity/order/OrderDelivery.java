package com.gzu.entity.order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDelivery {
    private Long orderDeliveryId;
    private Long orderMainId;
    private Long riderId;
    private String riderNickname;
    private BigDecimal deliveryFee;
    private BigDecimal actualPickLongitude;
    private BigDecimal actualPickLatitude;
    private BigDecimal actualDeliverLongitude;
    private BigDecimal actualDeliverLatitude;
    private Date assignTime;
    private Date receiveTime;
    private Date pickTime;
    private Date deliverTime;
    private Integer deliveryStatus;
}