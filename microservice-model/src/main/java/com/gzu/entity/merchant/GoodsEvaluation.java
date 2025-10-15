package com.gzu.entity.merchant;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GoodsEvaluation {
    private Long goodsEvaluationId;
    private Long merchantGoodsId;
    private Long merchantBaseId;
    private Long userId;
    private Long orderId;
    private Long orderItemId;
    private Integer rating;
    private String content;
    private Integer isAnonymous;
    private String merchantReply;
    private LocalDateTime createTime;
    private LocalDateTime replyTime;
    private Integer usefulCount;
}