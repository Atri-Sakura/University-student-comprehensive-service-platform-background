package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 角色对象 platform_role
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class PlatformRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 角色唯一ID */
    private Long platformRoleId;

    /** 角色名称（如超级管理员/运营专员） */
    @Excel(name = "角色名称", readConverterExp = "如=超级管理员/运营专员")
    private String roleName;

    /** 角色编码（唯一标识，如ADMIN/OPERATOR） */
    @Excel(name = "角色编码", readConverterExp = "唯=一标识，如ADMIN/OPERATOR")
    private String roleCode;

    /** 角色描述 */
    @Excel(name = "角色描述")
    private String roleDesc;

    public void setPlatformRoleId(Long platformRoleId) 
    {
        this.platformRoleId = platformRoleId;
    }

    public Long getPlatformRoleId() 
    {
        return platformRoleId;
    }

    public void setRoleName(String roleName) 
    {
        this.roleName = roleName;
    }

    public String getRoleName() 
    {
        return roleName;
    }

    public void setRoleCode(String roleCode) 
    {
        this.roleCode = roleCode;
    }

    public String getRoleCode() 
    {
        return roleCode;
    }

    public void setRoleDesc(String roleDesc) 
    {
        this.roleDesc = roleDesc;
    }

    public String getRoleDesc() 
    {
        return roleDesc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("platformRoleId", getPlatformRoleId())
            .append("roleName", getRoleName())
            .append("roleCode", getRoleCode())
            .append("roleDesc", getRoleDesc())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
