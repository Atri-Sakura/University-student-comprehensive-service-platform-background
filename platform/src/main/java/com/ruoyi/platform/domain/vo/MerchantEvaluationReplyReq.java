package com.ruoyi.platform.domain.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 商家回复评价请求
 */
public class MerchantEvaluationReplyReq {

    /** 评价ID */
    @NotNull(message = "评价ID不能为空")
    private Long merchantEvaluationId;

    /** 回复内容 */
    @NotBlank(message = "回复内容不能为空")
    @Size(min = 1, max = 500, message = "回复内容长度为1-500个字符")
    private String merchantReply;

    // Getter and Setter
    public Long getMerchantEvaluationId() {
        return merchantEvaluationId;
    }

    public void setMerchantEvaluationId(Long merchantEvaluationId) {
        this.merchantEvaluationId = merchantEvaluationId;
    }

    public String getMerchantReply() {
        return merchantReply;
    }

    public void setMerchantReply(String merchantReply) {
        this.merchantReply = merchantReply;
    }
}