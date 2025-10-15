package com.gzu.entity.user;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserWallet {
    private Long userWalletId;
    private Long userBaseId;
    private BigDecimal balance;
    private BigDecimal freezeAmount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}