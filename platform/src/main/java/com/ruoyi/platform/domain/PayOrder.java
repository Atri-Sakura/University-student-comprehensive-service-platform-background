package com.ruoyi.platform.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayOrder {

    private Long payOrderId;

    private String outTradeNo;// 交易订单号
    private Integer channel; // 1 支付宝 2 微信支付 3 银联支付

    private Integer ownerType;
    private Long ownerId;

    private Integer bizType;
    private Long bizId; // 可为 null，用户充值先不用也行

    private BigDecimal totalAmount;

    private Integer status;           // 0/2/3...
    private String channelTradeNo;    // 支付宝 trade_no

    private Integer notifyStatus;     // 0 未处理 1 已处理
    private Integer notifyTimes;      // 回调次数

    private Date createTime;
    private Date updateTime;
}
