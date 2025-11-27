package com.ruoyi.platform.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 骑手评价统计VO
 *
 * @author ruoyi
 * @date 2025-11-26
 */
public class RiderEvaluationStatisticsVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 综合评分 */
    private BigDecimal avgRating;

    /** 总评价数 */
    private Long totalCount;

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

    /** 5星占比 */
    private BigDecimal fiveStarRate;

    /** 4星占比 */
    private BigDecimal fourStarRate;

    /** 3星占比 */
    private BigDecimal threeStarRate;

    /** 2星占比 */
    private BigDecimal twoStarRate;

    /** 1星占比 */
    private BigDecimal oneStarRate;

    // Getter and Setter
    public BigDecimal getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(BigDecimal avgRating) {
        this.avgRating = avgRating;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getFiveStarCount() {
        return fiveStarCount;
    }

    public void setFiveStarCount(Long fiveStarCount) {
        this. fiveStarCount = fiveStarCount;
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

    public BigDecimal getFiveStarRate() {
        return fiveStarRate;
    }

    public void setFiveStarRate(BigDecimal fiveStarRate) {
        this.fiveStarRate = fiveStarRate;
    }

    public BigDecimal getFourStarRate() {
        return fourStarRate;
    }

    public void setFourStarRate(BigDecimal fourStarRate) {
        this.fourStarRate = fourStarRate;
    }

    public BigDecimal getThreeStarRate() {
        return threeStarRate;
    }

    public void setThreeStarRate(BigDecimal threeStarRate) {
        this.threeStarRate = threeStarRate;
    }

    public BigDecimal getTwoStarRate() {
        return twoStarRate;
    }

    public void setTwoStarRate(BigDecimal twoStarRate) {
        this.twoStarRate = twoStarRate;
    }

    public BigDecimal getOneStarRate() {
        return oneStarRate;
    }

    public void setOneStarRate(BigDecimal oneStarRate) {
        this.oneStarRate = oneStarRate;
    }
}