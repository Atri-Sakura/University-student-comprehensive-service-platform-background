package com.ruoyi. platform.domain. dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * 骑手评价DTO
 *
 * @author ruoyi
 * @date 2025-11-26
 */
public class RiderEvaluationDTO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 关联订单ID */
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    /** 评分(1-5分) */
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1分")
    @Max(value = 5, message = "评分最高为5分")
    private Integer rating;

    /** 评价内容 */
    @Size(max = 500, message = "评价内容不能超过500个字符")
    private String content;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this. orderId = orderId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}