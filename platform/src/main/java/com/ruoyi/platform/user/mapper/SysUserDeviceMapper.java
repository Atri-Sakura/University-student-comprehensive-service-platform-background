package com.ruoyi.platform.user.mapper;

import com.ruoyi.platform.user.domain.SysUserDevice;
import java.util.List;

public interface SysUserDeviceMapper {
    SysUserDevice selectSysUserDeviceById(Long id);

    List<SysUserDevice> selectSysUserDeviceList(SysUserDevice sysUserDevice);

    int insertSysUserDevice(SysUserDevice sysUserDevice);

    int updateSysUserDevice(SysUserDevice sysUserDevice);

    int deleteSysUserDeviceById(Long id);

    int deleteSysUserDeviceByIds(Long[] ids);

    // 查询分类设备
    List<SysUserDevice> selectSysUserDeviceByCategory(Long userId, String category); // category: all/today/week/month

    // 查看当前登录设备
    SysUserDevice selectCurrentDevice(Long userId, String loginIp); // 当前IP和用户

    // 设备下线
    int offlineDevice(Long id);

    // 统计登录次数
    int totalLoginCount(Long userId);
    int monthLoginCount(Long userId);
}