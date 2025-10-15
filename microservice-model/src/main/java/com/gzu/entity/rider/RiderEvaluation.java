package com.gzu.entity.rider;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RiderEvaluation {
    private Long riderEvaluationId;
    private Long riderBaseId;
    private Long userId;
    private Long orderId;
    private Integer rating;
    private Integer speedScore;
    private Integer attitudeScore;
    private String content;
    private LocalDateTime createTime;
}