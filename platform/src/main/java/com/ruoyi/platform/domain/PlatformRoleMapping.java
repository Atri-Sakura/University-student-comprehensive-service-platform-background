package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 角色-账号映射（多角色登录路由核心）对象 platform_role_mapping
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class PlatformRoleMapping extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long platformRoleMappingId;

    /** 登录账号（各角色的username字段） */
    @Excel(name = "登录账号", readConverterExp = "各=角色的username字段")
    private String username;

    /** 角色类型：1-学生用户 2-骑手 3-商家 4-平台管理员 */
    @Excel(name = "角色类型：1-学生用户 2-骑手 3-商家 4-平台管理员")
    private Long roleType;

    /** 目标数据库：user_db/rider_db/merchant_db/platform_db */
    @Excel(name = "目标数据库：user_db/rider_db/merchant_db/platform_db")
    private String targetDb;

    /** 目标表：user_base/rider_base/merchant_base/platform_admin */
    @Excel(name = "目标表：user_base/rider_base/merchant_base/platform_admin")
    private String targetTable;

    /** 账号全局状态：0-禁用 1-正常 */
    @Excel(name = "账号全局状态：0-禁用 1-正常")
    private Long accountStatus;

    public void setPlatformRoleMappingId(Long platformRoleMappingId) 
    {
        this.platformRoleMappingId = platformRoleMappingId;
    }

    public Long getPlatformRoleMappingId() 
    {
        return platformRoleMappingId;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setRoleType(Long roleType) 
    {
        this.roleType = roleType;
    }

    public Long getRoleType() 
    {
        return roleType;
    }

    public void setTargetDb(String targetDb) 
    {
        this.targetDb = targetDb;
    }

    public String getTargetDb() 
    {
        return targetDb;
    }

    public void setTargetTable(String targetTable) 
    {
        this.targetTable = targetTable;
    }

    public String getTargetTable() 
    {
        return targetTable;
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
            .append("platformRoleMappingId", getPlatformRoleMappingId())
            .append("username", getUsername())
            .append("roleType", getRoleType())
            .append("targetDb", getTargetDb())
            .append("targetTable", getTargetTable())
            .append("accountStatus", getAccountStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
