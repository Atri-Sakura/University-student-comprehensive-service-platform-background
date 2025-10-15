package com.gzu.entity.rider;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RiderOrderRel {
    private Long riderOrderRelId;
    private Long riderBaseId;
    private Long orderId;
    private Integer orderType;
    private LocalDateTime receiveTime;
    private LocalDateTime pickUpTime;
    private LocalDateTime deliverTime;
    private Integer deliveryStatus;
    private String abnormalReason;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
