package com.ruoyi.platform.domain.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商家评价修改请求对象
 * 用于接收前端提交的修改评价数据
 */
public class MerchantEvaluationUpdateReq {

    @NotNull(message = "评价ID不能为空")
    private Long merchantEvaluationId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低1分")
    @Max(value = 5, message = "评分最高5分")
    private Long rating;

    @Min(value = 1, message = "口味评分最低1分")
    @Max(value = 5, message = "口味评分最高5分")
    private Long tasteScore;

    @Min(value = 1, message = "包装评分最低1分")
    @Max(value = 5, message = "包装评分最高5分")
    private Long packageScore;

    private String content;

    // 保留的原有图片URL（逗号分隔）
    private String keepImgUrls;

    // 新增的图片文件列表
    private List<MultipartFile> newImages;

    // Getters and Setters
    public Long getMerchantEvaluationId() {
        return merchantEvaluationId;
    }

    public void setMerchantEvaluationId(Long merchantEvaluationId) {
        this.merchantEvaluationId = merchantEvaluationId;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Long getTasteScore() {
        return tasteScore;
    }

    public void setTasteScore(Long tasteScore) {
        this.tasteScore = tasteScore;
    }

    public Long getPackageScore() {
        return packageScore;
    }

    public void setPackageScore(Long packageScore) {
        this.packageScore = packageScore;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeepImgUrls() {
        return keepImgUrls;
    }

    public void setKeepImgUrls(String keepImgUrls) {
        this.keepImgUrls = keepImgUrls;
    }

    public List<MultipartFile> getNewImages() {
        return newImages;
    }

    public void setNewImages(List<MultipartFile> newImages) {
        this.newImages = newImages;
    }
}