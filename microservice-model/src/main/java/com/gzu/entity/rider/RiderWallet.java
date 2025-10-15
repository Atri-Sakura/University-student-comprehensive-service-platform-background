package com.gzu.entity.rider;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RiderWallet {
    private Long riderWalletId;
    private Long riderBaseId;
    private BigDecimal balance;
    private BigDecimal freezeAmount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}