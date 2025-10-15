package com.gzu.entity.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserCreditScoreRecord {
    private Long id;
    private Long userBaseId;
    private Integer changeScore;
    private String desc;
    private LocalDateTime changeTime;
}
