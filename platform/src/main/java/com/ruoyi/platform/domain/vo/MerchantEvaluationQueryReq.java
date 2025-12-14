package com.ruoyi.platform.domain.vo;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商家评价查询请求
 */
public class MerchantEvaluationQueryReq extends BaseEntity {

    /** 评分筛选（1-5分，null表示全部） */
    private Long rating;

    /** 是否只看有内容的评价：0-全部 1-只看有内容 */
    private Integer hasContent;

    /** 是否只看有图片的评价：0-全部 1-只看有图片 */
    private Integer hasImages;

    /** 回复状态：0-未回复 1-已回复 null-全部 */
    private Integer replyStatus;

    /** 排序方式：1-最新评价 2-评分最高 3-评分最低 */
    private Integer sortType;

    // Getter and Setter
    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public Integer getHasContent() {
        return hasContent;
    }

    public void setHasContent(Integer hasContent) {
        this.hasContent = hasContent;
    }

    public Integer getHasImages() {
        return hasImages;
    }

    public void setHasImages(Integer hasImages) {
        this.hasImages = hasImages;
    }

    public Integer getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(Integer replyStatus) {
        this.replyStatus = replyStatus;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }
}