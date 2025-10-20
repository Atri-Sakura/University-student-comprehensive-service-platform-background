package com.ruoyi.common.core.domain.model;

/**
 * 用户注册对象
 *
 * @author ruoyi
 */
public class RegisterBody extends LoginBody
{
    /**
     * 用户类型 (可选,用于区分商家/普通用户等)
     */
    private String userType;

    /**
     * 用户昵称 (可选)
     */
    private String nickName;

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