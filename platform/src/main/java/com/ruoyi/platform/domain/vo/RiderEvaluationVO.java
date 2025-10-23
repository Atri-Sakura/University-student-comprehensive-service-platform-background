package com.ruoyi.platform.domain.vo;

import lombok.Data;

import java.util.Date;


/**
 * 骑手评价展示VO
 */
@Data
public class RiderEvaluationVO {
    private String userName;      // 评价用户（可以从 userId 查，也可暂留）
    private Long rating;          // 综合评分
    private Long speedScore;      // 速度评分
    private Long attitudeScore;   // 态度评分
    private String content;       // 评价内容
    private Date createTime;      // 评价时间

    // 构造 & getter/setter
    public RiderEvaluationVO() {}

    public RiderEvaluationVO(String userName, Long rating, Long speedScore,
                             Long attitudeScore, String content, Date createTime) {
        this.userName = userName;
        this.rating = rating;
        this.speedScore = speedScore;
        this.attitudeScore = attitudeScore;
        this.content = content;
        this.createTime = createTime;
    }
}
