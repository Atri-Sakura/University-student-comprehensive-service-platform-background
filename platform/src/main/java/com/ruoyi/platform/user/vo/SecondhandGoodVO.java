package com.ruoyi.platform.user.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SecondhandGoodVO {
    /** 商品基础信息 */
    private Long goodsId;
    private String goodsName;
    private String category;
    private BigDecimal price;
    private String description;
    private Long status;
    private Long viewCount;
    private Long favoriteCount;
    private Long shareCount;
    private Date createTime;

    /** 商品图片信息 */
    private String imageUrl;
}
