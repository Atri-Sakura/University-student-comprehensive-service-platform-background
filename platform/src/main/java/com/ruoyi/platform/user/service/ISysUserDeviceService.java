package com.ruoyi.platform.user.service;

import com.ruoyi.platform.user.domain.SysUserDevice;
import java.util.List;

public interface ISysUserDeviceService {
    SysUserDevice selectSysUserDeviceById(Long id);

    List<SysUserDevice> selectSysUserDeviceList(SysUserDevice sysUserDevice);

    int insertSysUserDevice(SysUserDevice sysUserDevice);

    int updateSysUserDevice(SysUserDevice sysUserDevice);

    int deleteSysUserDeviceById(Long id);

    int deleteSysUserDeviceByIds(Long[] ids);

    // 分类
    List<SysUserDevice> selectSysUserDeviceByCategory(Long userId, String category);
    // 当前登录设备
    SysUserDevice selectCurrentDevice(Long userId, String loginIp);
    // 下线
    int offlineDevice(Long id);
    // 统计
    int totalLoginCount(Long userId);
    int monthLoginCount(Long userId);
}