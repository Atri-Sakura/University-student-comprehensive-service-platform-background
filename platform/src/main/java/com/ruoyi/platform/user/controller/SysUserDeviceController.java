package com.ruoyi.platform.user.controller;

import com.ruoyi.platform.user.domain.SysUserDevice;
import com.ruoyi.platform.user.service.ISysUserDeviceService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ruoyi.framework.web.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private TokenService tokenService;

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

    /**
     * 分类查询设备
     */
    @GetMapping("/category")
    public AjaxResult deviceCategory(
            @RequestParam Long userId,
            @RequestParam String category // all,today,week,month
    ) {
        List<SysUserDevice> list = sysUserDeviceService.selectSysUserDeviceByCategory(userId, category);
        return AjaxResult.success(list);
    }
    /**
     * 查看当前登录设备
     */
    @GetMapping("/current")
    public AjaxResult currentDevice(@RequestParam Long userId, HttpServletRequest request) {
        String loginIp = request.getRemoteAddr();
        SysUserDevice device = sysUserDeviceService.selectCurrentDevice(userId, loginIp);
        return AjaxResult.success(device);
    }
    /**
     * 设备下线
     */
    @PostMapping("/offline/{id}")
    public AjaxResult offline(@PathVariable Long id) {
        // 1. 查询设备
        SysUserDevice device = sysUserDeviceService.selectSysUserDeviceById(id);
        if (device == null) {
            return AjaxResult.error("设备不存在");
        }

        // 2. 下线数据库online字段
        int rows = sysUserDeviceService.offlineDevice(id);

        // 3. 删除redis中的token，让设备立即失效
        String token = device.getToken();
        if (token != null && !token.isEmpty()) {
            tokenService.delLoginUser(token);
        }
        return rows > 0 ? AjaxResult.success("下线成功") : AjaxResult.error("下线失败，数据库更新异常");
    }
    /**
     * 统计
     */
    @GetMapping("/count")
    public AjaxResult count(@RequestParam Long userId) {
        int total = sysUserDeviceService.totalLoginCount(userId);
        int month = sysUserDeviceService.monthLoginCount(userId);
        return AjaxResult.success(Map.of("total", total, "month", month));
    }
}