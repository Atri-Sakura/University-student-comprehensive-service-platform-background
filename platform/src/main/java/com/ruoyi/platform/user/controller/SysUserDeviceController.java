package com.ruoyi.platform.user.controller;

import com.ruoyi.platform.user.domain.SysUserDevice;
import com.ruoyi.platform.user.service.ISysUserDeviceService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户登录设备信息控制器
 */
@RestController
@RequestMapping("/user/device")
public class SysUserDeviceController {

    private final ISysUserDeviceService sysUserDeviceService;

    @Autowired
    public SysUserDeviceController(ISysUserDeviceService sysUserDeviceService) {
        this.sysUserDeviceService = sysUserDeviceService;
    }

    /**
     * 查询单个设备信息
     */
    @GetMapping("/{id}")
    public AjaxResult getDevice(@PathVariable Long id) {
        SysUserDevice device = sysUserDeviceService.selectSysUserDeviceById(id);
        return AjaxResult.success(device);
    }

    /**
     * 查询设备信息列表
     */
    @GetMapping("/list")
    public AjaxResult list(SysUserDevice sysUserDevice) {
        List<SysUserDevice> list = sysUserDeviceService.selectSysUserDeviceList(sysUserDevice);
        return AjaxResult.success(list);
    }

    /**
     * 新增设备信息
     */
    @PostMapping
    public AjaxResult add(@RequestBody SysUserDevice sysUserDevice, HttpServletRequest request) {
        String loginIp = request.getRemoteAddr();
        sysUserDevice.setLoginIp(loginIp);
        sysUserDevice.setLoginTime(LocalDateTime.now());
        int rows = sysUserDeviceService.insertSysUserDevice(sysUserDevice);
        return rows > 0 ? AjaxResult.success("新增成功") : AjaxResult.error("新增失败");
    }

    /**
     * 编辑设备信息
     */
    @PutMapping
    public AjaxResult edit(@RequestBody SysUserDevice sysUserDevice) {
        int rows = sysUserDeviceService.updateSysUserDevice(sysUserDevice);
        return rows > 0 ? AjaxResult.success("修改成功") : AjaxResult.error("修改失败");
    }

    /**
     * 删除单个设备信息
     */
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        int rows = sysUserDeviceService.deleteSysUserDeviceById(id);
        return rows > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    /**
     * 批量删除设备信息
     */
    @DeleteMapping("/batch")
    public AjaxResult batchRemove(@RequestBody Long[] ids) {
        int rows = sysUserDeviceService.deleteSysUserDeviceByIds(ids);
        return rows > 0 ? AjaxResult.success("批量删除成功") : AjaxResult.error("批量删除失败");
    }
}