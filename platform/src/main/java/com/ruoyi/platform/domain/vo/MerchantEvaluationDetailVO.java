package com.ruoyi.platform.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

/**
 * 商家评价详情VO（商家视角）
 */
public class MerchantEvaluationDetailVO {

    /** 评价ID */
    private Long merchantEvaluationId;

    /** 订单ID */
    private Long orderId;

    /** 用户昵称（脱敏） */
    private String userNickname;

    /** 用户头像 */
    private String userAvatar;

    /** 综合评分 */
    private Long rating;

    /** 口味评分 */
    private Long tasteScore;

    /** 包装评分 */
    private Long packageScore;

    /** 评价内容 */
    private String content;

    /** 评价图片列表 */
    private List<String> imageList;

    /** 商家回复 */
    private String merchantReply;

    /** 评价时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm: ss")
    private Date createTime;

    /** 回复时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;

    /** 是否已回复 */
    private Boolean hasReply;

    // Getter and Setter
    public Long getMerchantEvaluationId() {
        return merchantEvaluationId;
    }

    public void setMerchantEvaluationId(Long merchantEvaluationId) {
        this.merchantEvaluationId = merchantEvaluationId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
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

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getMerchantReply() {
        return merchantReply;
    }

    public void setMerchantReply(String merchantReply) {
        this.merchantReply = merchantReply;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Boolean getHasReply() {
        return hasReply;
    }

    public void setHasReply(Boolean hasReply) {
        this.hasReply = hasReply;
    }
}