package com.ruoyi.platform.domain.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 用户反馈提交DTO
 */
@Data
public class UserFeedbackDTO {

    /**
     * 反馈标题
     */
    @NotBlank(message = "反馈标题不能为空")
    private String feedbackTitle;

    /**
     * 反馈详情
     */
    @NotBlank(message = "反馈详情不能为空")
    private String feedbackContent;

    /**
     * 反馈类型：1-功能建议 2-bug反馈 3-投诉建议 4-其他
     */
    @NotNull(message = "反馈类型不能为空")
    private Integer feedbackType;

    /**
     * 联系方式（选填）
     */
    private String contactInfo;

    /**
     * 反馈图片URL（逗号分隔，选填）
     */
    private String imgUrls;
}