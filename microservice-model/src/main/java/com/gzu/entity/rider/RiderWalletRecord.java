package com.gzu.entity.rider;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RiderWalletRecord {
    private Long riderWalletRecordId;
    private Long riderWalletId;
    private Long riderBaseId;
    private BigDecimal amount;
    private Integer tradeType;
    private Long relatedId;
    private Integer tradeStatus;
    private LocalDateTime tradeTime;
    private String remark;
}