package com.ruoyi.platform.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 权限对象 platform_permission
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public class PlatformPermission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 权限唯一ID */
    private Long platformPermissionId;

    /** 权限名称（如订单管理/商家审核） */
    @Excel(name = "权限名称", readConverterExp = "如=订单管理/商家审核")
    private String permName;

    /** 权限标识（如order:manage/merchant:audit） */
    @Excel(name = "权限标识", readConverterExp = "如=order:manage/merchant:audit")
    private String permKey;

    /** 权限类型：1-菜单 2-按钮 */
    @Excel(name = "权限类型：1-菜单 2-按钮")
    private Long permType;

    /** 父权限ID（用于构建权限树，关联platform_permission.platform_permission_id） */
    @Excel(name = "父权限ID", readConverterExp = "用=于构建权限树，关联platform_permission.platform_permission_id")
    private Long parentPermId;

    /** 菜单路径（仅perm_type=1时有值） */
    @Excel(name = "菜单路径", readConverterExp = "仅=perm_type=1时有值")
    private String menuPath;

    /** 排序序号（值越小越靠前） */
    @Excel(name = "排序序号", readConverterExp = "值=越小越靠前")
    private Long sort;

    public void setPlatformPermissionId(Long platformPermissionId) 
    {
        this.platformPermissionId = platformPermissionId;
    }

    public Long getPlatformPermissionId() 
    {
        return platformPermissionId;
    }

    public void setPermName(String permName) 
    {
        this.permName = permName;
    }

    public String getPermName() 
    {
        return permName;
    }

    public void setPermKey(String permKey) 
    {
        this.permKey = permKey;
    }

    public String getPermKey() 
    {
        return permKey;
    }

    public void setPermType(Long permType) 
    {
        this.permType = permType;
    }

    public Long getPermType() 
    {
        return permType;
    }

    public void setParentPermId(Long parentPermId) 
    {
        this.parentPermId = parentPermId;
    }

    public Long getParentPermId() 
    {
        return parentPermId;
    }

    public void setMenuPath(String menuPath) 
    {
        this.menuPath = menuPath;
    }

    public String getMenuPath() 
    {
        return menuPath;
    }

    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("platformPermissionId", getPlatformPermissionId())
            .append("permName", getPermName())
            .append("permKey", getPermKey())
            .append("permType", getPermType())
            .append("parentPermId", getParentPermId())
            .append("menuPath", getMenuPath())
            .append("sort", getSort())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
