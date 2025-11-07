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
}