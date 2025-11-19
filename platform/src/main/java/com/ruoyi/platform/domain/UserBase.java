package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户基础信息对象 user_base
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class UserBase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户唯一ID（雪花算法） */
    private Long userBaseId;

    /** 登录账号（唯一） */
    @Excel(name = "登录账号", readConverterExp = "唯=一")
    private String username;

    /** 密码（BCrypt加密） */
    @Excel(name = "密码", readConverterExp = "B=Crypt加密")
    private String password;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickname;

    /** 头像URL */
    @Excel(name = "头像URL")
    private String avatar;

    /** 学号（唯一） */
    @Excel(name = "学号", readConverterExp = "唯=一")
    private String studentId;

    /** 所属学院（如计算机学院） */
    @Excel(name = "所属学院", readConverterExp = "如=计算机学院")
    private String college;

    /** 所属专业（如软件工程） */
    @Excel(name = "所属专业", readConverterExp = "如=软件工程")
    private String major;

    /** 年级（如2022级） */
    @Excel(name = "年级", readConverterExp = "如=2022级")
    private String grade;

    /** 性别：1-男 2-女 0-未知 */
    @Excel(name = "性别：1-男 2-女 0-未知")
    private Long gender;

    /** 联系电话（AES加密） */
    @Excel(name = "联系电话", readConverterExp = "A=ES加密")
    private String phone;

    /** 信用分（影响推荐优先级） */
    @Excel(name = "信用分", readConverterExp = "影=响推荐优先级")
    private Long creditScore;

    /** 账号状态：0-禁用 1-正常 */
    @Excel(name = "账号状态：0-禁用 1-正常")
    private Long accountStatus;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastLoginTime;

    /** 最后登录IP */
    @Excel(name = "最后登录IP")
    private String lastLoginIp;

    /** 支付密码（BCrypt加密） */
    @Excel(name = "支付密码", readConverterExp = "BC=rypt加密")
    private String payPassword;
    /** 对应sys_user表的用户ID */
    private Long userId;

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserBaseId(Long userBaseId) 
    {
        this.userBaseId = userBaseId;
    }

    public Long getUserBaseId() 
    {
        return userBaseId;
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

    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }

    public void setCollege(String college) 
    {
        this.college = college;
    }

    public String getCollege() 
    {
        return college;
    }

    public void setMajor(String major) 
    {
        this.major = major;
    }

    public String getMajor() 
    {
        return major;
    }

    public void setGrade(String grade) 
    {
        this.grade = grade;
    }

    public String getGrade() 
    {
        return grade;
    }

    public void setGender(Long gender) 
    {
        this.gender = gender;
    }

    public Long getGender() 
    {
        return gender;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
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

    public void setLastLoginTime(Date lastLoginTime) 
    {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLoginTime() 
    {
        return lastLoginTime;
    }

    public void setLastLoginIp(String lastLoginIp) 
    {
        this.lastLoginIp = lastLoginIp;
    }

    public String getLastLoginIp() 
    {
        return lastLoginIp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userBaseId", getUserBaseId())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("nickname", getNickname())
            .append("avatar", getAvatar())
            .append("studentId", getStudentId())
            .append("college", getCollege())
            .append("major", getMajor())
            .append("grade", getGrade())
            .append("gender", getGender())
            .append("phone", getPhone())
            .append("creditScore", getCreditScore())
            .append("accountStatus", getAccountStatus())
            .append("lastLoginTime", getLastLoginTime())
            .append("lastLoginIp", getLastLoginIp())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
