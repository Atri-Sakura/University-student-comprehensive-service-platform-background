package com.ruoyi.common.core.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 用户注册对象
 *
 * @author ruoyi
 */
public class RegisterBody extends LoginBody
{
    /** 用户类型 (可选,用于区分商家/普通用户等) */
    private String userType;

    /** 用户昵称 (可选) */
    private String nickName;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 验证码 */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /** 唯一标识 */
    @NotBlank(message = "验证码标识不能为空")
    private String uuid;

    /** 用户类型：1-用户 2-骑手 3-商家 */
    private Integer userTypeInt;

    // ========== 用户端专属字段 ==========
    /** 用户昵称 */
    private String nickname;

    /** 学号 */
    private String studentId;

    /** 所属学院 */
    private String college;

    /** 所属专业 */
    private String major;

    /** 年级 */
    private String grade;

    // ========== 骑手端专属字段 ==========
    /** 骑手昵称 */
    private String riderNickname;

    /** 真实姓名 */
    private String realName;

    /** 身份证号 */
    private String idCard;

    // ========== 商家端专属字段 ==========
    /** 商家名称 */
    private String merchantName;

    /** 经营范围 */
    private String businessScope;

    // Getter and Setter
    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public Integer getUserTypeInt()
    {
        return userTypeInt;
    }

    public void setUserTypeInt(Integer userTypeInt)
    {
        this.userTypeInt = userTypeInt;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getStudentId()
    {
        return studentId;
    }

    public void setStudentId(String studentId)
    {
        this.studentId = studentId;
    }

    public String getCollege()
    {
        return college;
    }

    public void setCollege(String college)
    {
        this.college = college;
    }

    public String getMajor()
    {
        return major;
    }

    public void setMajor(String major)
    {
        this.major = major;
    }

    public String getGrade()
    {
        return grade;
    }

    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    public String getRiderNickname()
    {
        return riderNickname;
    }

    public void setRiderNickname(String riderNickname)
    {
        this.riderNickname = riderNickname;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getMerchantName()
    {
        return merchantName;
    }

    public void setMerchantName(String merchantName)
    {
        this.merchantName = merchantName;
    }

    public String getBusinessScope()
    {
        return businessScope;
    }

    public void setBusinessScope(String businessScope)
    {
        this.businessScope = businessScope;
    }

    /**
     * 获取手机号 (从父类 LoginBody 继承)
     * 为了兼容性,添加 getUsername 方法,返回手机号
     */
    public String getUsername()
    {
        return getPhonenumber();
    }

    public void setUsername(String username)
    {
        setPhonenumber(username);
    }
}