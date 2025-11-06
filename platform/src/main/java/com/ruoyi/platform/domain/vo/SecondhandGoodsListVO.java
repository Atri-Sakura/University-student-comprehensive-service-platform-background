package com.ruoyi.platform.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class SecondhandGoodsListVO {
    /** 商品ID */
    private Long goodsId;

    /** 商品名称 */
    private String goodsName;

    /** 商品价格 */
    private BigDecimal price;

    /** 主图URL */
    private String mainImageUrl;

    /** 分类 */
    private String category;

    /** 发布时间 */
    private Date createTime;
}
