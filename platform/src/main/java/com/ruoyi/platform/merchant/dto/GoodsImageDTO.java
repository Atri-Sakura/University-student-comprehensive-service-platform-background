package com.ruoyi.platform.merchant.dto;

import lombok.Data;

/**
 * 商品图片数据传输对象
 */
@Data
public class GoodsImageDTO {

    /** 图片ID */
    private Long merchantGoodsImageId;

    /** 图片URL */
    private String imageUrl;

    /** 图片描述 */
    private String imageDesc;

    /** 排序序号 */
    private Integer sortOrder;

    /** 是否主图：0-否 1-是 */
    private Integer isMain;
}