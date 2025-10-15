package com.gzu.entity.order;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderMain {
    private Long orderMainId;
    private String orderNo;
    private Long userId;
    private String userNickname;
    private Integer orderType;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private BigDecimal discountAmount;
    private Integer payStatus;
    private Date payTime;
    private Integer payType;
    private Integer orderStatus;
    private String cancelReason;
    private String cancelOperator;

    // 取货地址字段
    private Long pickAddressId;
    private String pickAddress;
    private String pickContact;
    private String pickPhone;
    private BigDecimal pickLongitude;
    private BigDecimal pickLatitude;

    // 送货地址字段
    private Long deliverAddressId;
    private String deliverAddress;
    private String deliverContact;
    private String deliverPhone;
    private BigDecimal deliverLongitude;
    private BigDecimal deliverLatitude;

    private Date createTime;
    private Date updateTime;
    private Date completeTime;
}