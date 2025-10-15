package com.gzu.entity.merchant;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GoodsEvaluationImage {
    private Long goodsEvaluationImageId;
    private Long goodsEvaluationId;
    private String imageUrl;
    private Integer sortOrder;
    private LocalDateTime createTime;
}