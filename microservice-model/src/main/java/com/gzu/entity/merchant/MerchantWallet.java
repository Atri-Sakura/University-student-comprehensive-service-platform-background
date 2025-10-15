package com.gzu.entity.merchant;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MerchantWallet {
    private Long merchantWalletId;
    private Long merchantBaseId;
    private BigDecimal balance;
    private BigDecimal freezeAmount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}