package com.gzu.entity.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SecondhandGoodsImage {
    private Long secondhandGoodsImageId;
    private Long secondhandGoodsId;
    private String imageUrl;
    private Integer isMain;
    private Integer sortOrder;
    private LocalDateTime createTime;
}