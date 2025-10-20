package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 角色权限关联对象 platform_role_perm
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public class PlatformRolePerm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联唯一ID */
    private Long platformRolePermId;

    /** 角色ID（关联platform_role.platform_role_id） */
    @Excel(name = "角色ID", readConverterExp = "关=联platform_role.platform_role_id")
    private Long platformRoleId;

    /** 权限ID（关联platform_permission.platform_permission_id） */
    @Excel(name = "权限ID", readConverterExp = "关=联platform_permission.platform_permission_id")
    private Long platformPermissionId;

    public void setPlatformRolePermId(Long platformRolePermId) 
    {
        this.platformRolePermId = platformRolePermId;
    }

    public Long getPlatformRolePermId() 
    {
        return platformRolePermId;
    }

    public void setPlatformRoleId(Long platformRoleId) 
    {
        this.platformRoleId = platformRoleId;
    }

    public Long getPlatformRoleId() 
    {
        return platformRoleId;
    }

    public void setPlatformPermissionId(Long platformPermissionId) 
    {
        this.platformPermissionId = platformPermissionId;
    }

    public Long getPlatformPermissionId() 
    {
        return platformPermissionId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("platformRolePermId", getPlatformRolePermId())
            .append("platformRoleId", getPlatformRoleId())
            .append("platformPermissionId", getPlatformPermissionId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
