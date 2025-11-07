package com.ruoyi.platform.user.domain;

import java.time.LocalDateTime;

public class SysUserDevice {
    private Long id;
    private Long userId;
    private String deviceModel;
    private String deviceBrand;
    private String deviceSystem;
    private String devicePlatform;
    private String deviceVersion;
    private String loginIp;
    private LocalDateTime loginTime;
    private String remark;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }
    public String getDeviceBrand() { return deviceBrand; }
    public void setDeviceBrand(String deviceBrand) { this.deviceBrand = deviceBrand; }
    public String getDeviceSystem() { return deviceSystem; }
    public void setDeviceSystem(String deviceSystem) { this.deviceSystem = deviceSystem; }
    public String getDevicePlatform() { return devicePlatform; }
    public void setDevicePlatform(String devicePlatform) { this.devicePlatform = devicePlatform; }
    public String getDeviceVersion() { return deviceVersion; }
    public void setDeviceVersion(String deviceVersion) { this.deviceVersion = deviceVersion; }
    public String getLoginIp() { return loginIp; }
    public void setLoginIp(String loginIp) { this.loginIp = loginIp; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}