package com.ruoyi.platform.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RiderRechargeRequest {

    private BigDecimal amount;
    private Integer payChannel;   // 1=支付宝
}
