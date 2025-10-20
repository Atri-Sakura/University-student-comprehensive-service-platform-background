package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 骑手基础信息对象 rider_base
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class RiderBase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 骑手唯一ID */
    private Long riderBaseId;

    /** 登录账号 */
    @Excel(name = "登录账号")
    private String username;

    /** 密码(BCrypt加密) */
    @Excel(name = "密码(BCrypt加密)")
    private String password;

    /** 骑手昵称 */
    @Excel(name = "骑手昵称")
    private String nickname;

    /** 头像URL */
    @Excel(name = "头像URL")
    private String avatar;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 身份证号(AES加密) */
    @Excel(name = "身份证号(AES加密)")
    private String idCard;

    /** 身份证正面照URL */
    @Excel(name = "身份证正面照URL")
    private String idCardFront;

    /** 身份证反面照URL */
    @Excel(name = "身份证反面照URL")
    private String idCardBack;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 审核状态：0-待审核 1-通过 2-拒绝 */
    @Excel(name = "审核状态：0-待审核 1-通过 2-拒绝")
    private Long auditStatus;

    /** 工作状态：0-下线 1-上线 2-忙碌 */
    @Excel(name = "工作状态：0-下线 1-上线 2-忙碌")
    private Long workStatus;

    /** 服务信用分 */
    @Excel(name = "服务信用分")
    private Long creditScore;

    /** 账号状态：0-禁用 1-正常 */
    @Excel(name = "账号状态：0-禁用 1-正常")
    private Long accountStatus;

    public void setRiderBaseId(Long riderBaseId) 
    {
        this.riderBaseId = riderBaseId;
    }

    public Long getRiderBaseId() 
    {
        return riderBaseId;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setNickname(String nickname) 
    {
        this.nickname = nickname;
    }

    public String getNickname() 
    {
        return nickname;
    }

    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }

    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }

    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }

    public void setIdCardFront(String idCardFront) 
    {
        this.idCardFront = idCardFront;
    }

    public String getIdCardFront() 
    {
        return idCardFront;
    }

    public void setIdCardBack(String idCardBack) 
    {
        this.idCardBack = idCardBack;
    }

    public String getIdCardBack() 
    {
        return idCardBack;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setAuditStatus(Long auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public Long getAuditStatus() 
    {
        return auditStatus;
    }

    public void setWorkStatus(Long workStatus) 
    {
        this.workStatus = workStatus;
    }

    public Long getWorkStatus() 
    {
        return workStatus;
    }

    public void setCreditScore(Long creditScore) 
    {
        this.creditScore = creditScore;
    }

    public Long getCreditScore() 
    {
        return creditScore;
    }

    public void setAccountStatus(Long accountStatus) 
    {
        this.accountStatus = accountStatus;
    }

    public Long getAccountStatus() 
    {
        return accountStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("riderBaseId", getRiderBaseId())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("nickname", getNickname())
            .append("avatar", getAvatar())
            .append("realName", getRealName())
            .append("idCard", getIdCard())
            .append("idCardFront", getIdCardFront())
            .append("idCardBack", getIdCardBack())
            .append("phone", getPhone())
            .append("auditStatus", getAuditStatus())
            .append("workStatus", getWorkStatus())
            .append("creditScore", getCreditScore())
            .append("accountStatus", getAccountStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
