package com.gzu.entity.user;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SecondhandGoods {
    private Long secondhandGoodsId;
    private Long userBaseId;
    private String goodsName;
    private String category;
    private BigDecimal price;
    private String description;
    private Integer status;
    private Integer viewCount;
    private Integer favoriteCount;
    private Integer shareCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime soldTime;
}