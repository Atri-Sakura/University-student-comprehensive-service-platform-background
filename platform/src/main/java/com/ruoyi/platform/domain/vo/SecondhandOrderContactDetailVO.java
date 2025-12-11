package com.ruoyi.platform.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class SecondhandOrderContactDetailVO {

    // 订单基础信息
    private String orderNo;
    private Integer orderStatus;         // order_main.order_status
    private Integer payStatus;           // order_main.pay_status
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    // 商品信息
    private Long goodsId;
    private String goodsName;
    private String category;
    private BigDecimal goodsPrice;
    private String description;
    private String mainImageUrl;         // 主图 URL


    // 对方（联系人）信息
    private Long counterpartUserBaseId;
    private String counterpartUsername;
    private String counterpartAvatar;
    private String counterpartPhone;
}
