package com.gzu.entity.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserBankCard {
    private Long id;
    private Long userBaseId;
    private String bankName;
    private Integer bankCardType;
    private String cardNumber;
    private String cardTailNumber;
    private String holderName;
    private String idNumber;
    private String reservePhone;
    private Integer bindStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}