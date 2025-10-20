package com.ruoyi.platform.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 平台管理员对象 platform_admin
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class PlatformAdmin extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 管理员唯一ID（雪花算法） */
    private Long platformAdminId;

    /** 登录账号（唯一） */
    @Excel(name = "登录账号", readConverterExp = "唯=一")
    private String username;

    /** 密码（BCrypt加密存储） */
    @Excel(name = "密码", readConverterExp = "B=Crypt加密存储")
    private String password;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 关联角色ID（关联platform_role.platform_role_id） */
    @Excel(name = "关联角色ID", readConverterExp = "关=联platform_role.platform_role_id")
    private Long platformRoleId;

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

    public void setPlatformAdminId(Long platformAdminId) 
    {
        this.platformAdminId = platformAdminId;
    }

    public Long getPlatformAdminId() 
    {
        return platformAdminId;
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

    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setPlatformRoleId(Long platformRoleId) 
    {
        this.platformRoleId = platformRoleId;
    }

    public Long getPlatformRoleId() 
    {
        return platformRoleId;
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
            .append("platformAdminId", getPlatformAdminId())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("realName", getRealName())
            .append("phone", getPhone())
            .append("platformRoleId", getPlatformRoleId())
            .append("accountStatus", getAccountStatus())
            .append("lastLoginTime", getLastLoginTime())
            .append("lastLoginIp", getLastLoginIp())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
