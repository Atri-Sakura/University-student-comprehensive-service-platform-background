package com.ruoyi.platform.domain.vo;

/**
 * 商家评价统计VO
 */
public class MerchantEvaluationStatisticsVO {

    /** 总评价数 */
    private Long totalCount;

    /** 平均评分 */
    private Double avgRating;

    /** 5星数量 */
    private Long fiveStarCount;

    /** 4星数量 */
    private Long fourStarCount;

    /** 3星数量 */
    private Long threeStarCount;

    /** 2星数量 */
    private Long twoStarCount;

    /** 1星数量 */
    private Long oneStarCount;

    /** 待回复数量 */
    private Long pendingReplyCount;

    /** 有图评价数量 */
    private Long withImageCount;

    // Getter and Setter
    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Long getFiveStarCount() {
        return fiveStarCount;
    }

    public void setFiveStarCount(Long fiveStarCount) {
        this.fiveStarCount = fiveStarCount;
    }

    public Long getFourStarCount() {
        return fourStarCount;
    }

    public void setFourStarCount(Long fourStarCount) {
        this.fourStarCount = fourStarCount;
    }

    public Long getThreeStarCount() {
        return threeStarCount;
    }

    public void setThreeStarCount(Long threeStarCount) {
        this.threeStarCount = threeStarCount;
    }

    public Long getTwoStarCount() {
        return twoStarCount;
    }

    public void setTwoStarCount(Long twoStarCount) {
        this.twoStarCount = twoStarCount;
    }

    public Long getOneStarCount() {
        return oneStarCount;
    }

    public void setOneStarCount(Long oneStarCount) {
        this.oneStarCount = oneStarCount;
    }

    public Long getPendingReplyCount() {
        return pendingReplyCount;
    }

    public void setPendingReplyCount(Long pendingReplyCount) {
        this.pendingReplyCount = pendingReplyCount;
    }

    public Long getWithImageCount() {
        return withImageCount;
    }

    public void setWithImageCount(Long withImageCount) {
        this.withImageCount = withImageCount;
    }
}