package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户信用分流水对象 user_credit_score_record
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class UserCreditScoreRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userBaseId;

    /** 分数变动（正负） */
    @Excel(name = "分数变动", readConverterExp = "正=负")
    private Long changeScore;

    /** 变动说明 */
    @Excel(name = "变动说明")
    private String descriptions;

    /** 变动时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "变动时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date changeTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
    }

    public void setChangeScore(Long changeScore) 
    {
        this.changeScore = changeScore;
    }

    public Long getChangeScore() 
    {
        return changeScore;
    }

    public void setDescriptions(String descriptions)
    {
        this.descriptions = descriptions;
    }

    public String getDescriptions()
    {
        return descriptions;
    }

    public void setChangeTime(Date changeTime) 
    {
        this.changeTime = changeTime;
    }

    public Date getChangeTime() 
    {
        return changeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userBaseId", getUserBaseId())
            .append("changeScore", getChangeScore())
            .append("descriptions", getDescriptions())
            .append("changeTime", getChangeTime())
            .toString();
    }
}
