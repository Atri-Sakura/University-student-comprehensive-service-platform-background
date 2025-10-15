package com.gzu.entity.merchant;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MerchantEvaluation {
    private Long merchantEvaluationId;
    private Long merchantBaseId;
    private Long userId;
    private Long orderId;
    private Integer rating;
    private Integer tasteScore;
    private Integer packageScore;
    private String content;
    private String imgUrls;
    private String merchantReply;
    private LocalDateTime createTime;
    private LocalDateTime replyTime;
}