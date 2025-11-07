package com.ruoyi.platform.user.service.impl;

import com.ruoyi.platform.user.domain.SysUserDevice;
import com.ruoyi.platform.user.mapper.SysUserDeviceMapper;
import com.ruoyi.platform.user.service.ISysUserDeviceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserDeviceServiceImpl implements ISysUserDeviceService {

    private final SysUserDeviceMapper sysUserDeviceMapper;

    public SysUserDeviceServiceImpl(SysUserDeviceMapper sysUserDeviceMapper) {
        this.sysUserDeviceMapper = sysUserDeviceMapper;
    }

    @Override
    public SysUserDevice selectSysUserDeviceById(Long id) {
        return sysUserDeviceMapper.selectSysUserDeviceById(id);
    }

    @Override
    public List<SysUserDevice> selectSysUserDeviceList(SysUserDevice sysUserDevice) {
        return sysUserDeviceMapper.selectSysUserDeviceList(sysUserDevice);
    }

    @Override
    public int insertSysUserDevice(SysUserDevice sysUserDevice) {
        return sysUserDeviceMapper.insertSysUserDevice(sysUserDevice);
    }

    @Override
    public int updateSysUserDevice(SysUserDevice sysUserDevice) {
        return sysUserDeviceMapper.updateSysUserDevice(sysUserDevice);
    }

    @Override
    public int deleteSysUserDeviceById(Long id) {
        return sysUserDeviceMapper.deleteSysUserDeviceById(id);
    }

    @Override
    public int deleteSysUserDeviceByIds(Long[] ids) {
        return sysUserDeviceMapper.deleteSysUserDeviceByIds(ids);
    }
}