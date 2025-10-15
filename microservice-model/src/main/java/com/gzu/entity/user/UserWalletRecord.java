package com.gzu.entity.user;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserWalletRecord {
    private Long userWalletRecordId;
    private Long userWalletId;
    private Long userBaseId;
    private BigDecimal amount;
    private Integer tradeType;
    private Long relatedId;
    private Integer tradeStatus;
    private LocalDateTime tradeTime;
    private String remark;
}