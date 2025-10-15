package com.gzu.entity.merchant;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MerchantGoodsImage {
    private Long merchantGoodsImageId;
    private Long merchantGoodsId;
    private String imageUrl;
    private String imageDesc;
    private Integer sortOrder;
    private Integer isMain;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
