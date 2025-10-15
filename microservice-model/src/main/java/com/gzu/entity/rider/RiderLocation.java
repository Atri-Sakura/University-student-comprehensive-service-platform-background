package com.gzu.entity.rider;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RiderLocation {
    private Long riderLocationId;
    private Long riderBaseId;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private LocalDateTime updateTime;
}