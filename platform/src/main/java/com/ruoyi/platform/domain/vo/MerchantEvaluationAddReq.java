package com.ruoyi.platform.domain.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商家评价添加请求对象
 * 用于接收前端提交的评价数据
 */
public class MerchantEvaluationAddReq {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低1分")
    @Max(value = 5, message = "评分最高5分")
    private Long rating;

    // 餐饮类专用，非必填
    @Min(value = 1, message = "口味评分最低1分")
    @Max(value = 5, message = "口味评分最高5分")
    private Long tasteScore;

    @Min(value = 1, message = "包装评分最低1分")
    @Max(value = 5, message = "包装评分最高5分")
    private Long packageScore;

    @NotBlank(message = "评价内容不能为空")
    private String content;

    // 图片URL（已废弃，改用文件上传）
    @Deprecated
    private String imgUrls;

    // 新增：图片文件列表（支持多图上传，最多9张）
    private List<MultipartFile> images;

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public List<MultipartFile> getImages() {
        return images;
    }

    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}