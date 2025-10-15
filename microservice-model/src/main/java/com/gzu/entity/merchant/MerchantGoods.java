package com.gzu.entity.merchant;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MerchantGoods {
    private Long merchantGoodsId;
    private Long merchantBaseId;
    private String goodsName;
    private String category;
    private String subCategory;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Long salesCount;
    private String description;
    private String tagCodes;
    private Integer status;
    private BigDecimal avgRating;
    private Integer ratingCount;
    private BigDecimal fiveStarRate;
    private BigDecimal fourStarRate;
    private BigDecimal threeStarRate;
    private BigDecimal twoStarRate;
    private BigDecimal oneStarRate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}