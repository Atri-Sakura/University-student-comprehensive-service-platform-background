package com.ruoyi.platform.domain.dto;

import lombok.Data;

@Data
public class SecondhandGoodsSearchDTO {

    /** 搜索关键词 */
    private String keyword;

    /** 分类 */
    private String category;

    /** 排序字段 */
    private String sortBy = "create_time";

    /** 排序方式 */
    private String sortOrder = "desc";
}
