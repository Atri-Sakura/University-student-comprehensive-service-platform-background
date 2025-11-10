package com.ruoyi.common.core.domain.model;

public class IdentityInfo {
    private Integer role; // 0-平台管理员 1-用户 2-骑手 3-商家
    private Long id;      // 对应的唯一ID

    public IdentityInfo(Integer role, Long id) {
        this.role = role;
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}