package com.ruoyi.platform.rider.controller;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common. core.domain.AjaxResult;
import com.ruoyi.common. utils.SecurityUtils;
import com. ruoyi.platform.domain.RiderBase;
import com.ruoyi.platform.service.IRiderBaseService;
import org. springframework.beans.factory.annotation. Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.config.FileStorageProperties;
import com.ruoyi.framework.storage.CloudStorageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 骑手个人信息接口
 * 仅允许骑手访问、修改自己的个人信息
 *
 * @author ruoyi
 * @date 2025-11-28
 */
@RestController
@RequestMapping("/rider/info")
public class RiderInfoController {

    @Autowired
    private IRiderBaseService riderBaseService;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Autowired(required = false)
    private CloudStorageService cloudStorageService;

    /**
     * 查询当前骑手个人信息
     * 当前骑手只能查自己的信息
     */
    @GetMapping
    public AjaxResult getMyInfo() {
        Long userId = SecurityUtils.getUserId();

        // 根据 userId 查询骑手信息（需要添加新方法）
        RiderBase rider = riderBaseService.selectRiderBaseByUserId(userId);

        if (rider == null) {
            return AjaxResult.error("未找到当前骑手信息，请先完成骑手注册");
        }
        return AjaxResult.success(rider);
    }

    /**
     * 修改当前骑手个人信息
     * 仅允许骑手修改自己的基本信息
     */
    @PutMapping
    public AjaxResult updateMyInfo(@RequestBody RiderBase riderBase) {
        Long userId = SecurityUtils.getUserId();

        // 先查询当前用户对应的骑手信息
        RiderBase currentRider = riderBaseService. selectRiderBaseByUserId(userId);
        if (currentRider == null) {
            return AjaxResult.error("未找到当前骑手信息");
        }

        // 防止越权：只能修改自己的信息
        riderBase.setRiderBaseId(currentRider.getRiderBaseId());

        int rows = riderBaseService.updateRiderBaseBasicInfo(riderBase);
        if (rows > 0) {
            // 返回修改后的完整信息
            RiderBase updated = riderBaseService.selectRiderBaseByRiderBaseId(currentRider.getRiderBaseId());
            return AjaxResult. success("修改成功", updated);
        }
        return AjaxResult.error("修改失败");
    }

    /**
     * 更换骑手工作状态
     */
    @PutMapping("/status")
    public AjaxResult updateWorkStatus(@RequestBody RiderBase riderBase) {
        Long userId = SecurityUtils.getUserId();

        if (riderBase.getWorkStatus() == null ||
                (riderBase.getWorkStatus() < 0 || riderBase. getWorkStatus() > 2)) {
            return AjaxResult.error("非法的工作状态参数，应为 0(下线)/1(上线)/2(忙碌)");
        }

        // 先查询当前用户对应的骑手信息
        RiderBase currentRider = riderBaseService.selectRiderBaseByUserId(userId);
        if (currentRider == null) {
            return AjaxResult.error("未找到当前骑手信息");
        }

        // 防止越权：只能修改自己的状态
        riderBase.setRiderBaseId(currentRider. getRiderBaseId());

        int rows = riderBaseService.updateRiderWorkStatus(riderBase);
        if (rows == 0) {
            return AjaxResult.success("状态未变化，无需更新");
        }

        if (rows > 0) {
            RiderBase updated = riderBaseService.selectRiderBaseByRiderBaseId(currentRider.getRiderBaseId());
            Map<String, Object> result = new HashMap<>();
            result.put("riderBaseId", updated.getRiderBaseId());
            result.put("workStatus", updated.getWorkStatus());
            return AjaxResult.success("状态修改成功", result);
        }

        return AjaxResult.error("状态修改失败，请重试");
    }

    /**
     * 骑手身份认证信息上传
     */
    @PostMapping("/auth")
    public AjaxResult uploadAuthInfo(
            @RequestParam("realName") String realName,
            @RequestParam("idCard") String idCard,
            @RequestParam("frontImage") MultipartFile frontImage,
            @RequestParam("backImage") MultipartFile backImage) {

        Long userId = SecurityUtils.getUserId();

        // 先查询当前用户对应的骑手信息
        RiderBase currentRider = riderBaseService.selectRiderBaseByUserId(userId);
        if (currentRider == null) {
            return AjaxResult.error("未找到当前骑手信息");
        }

        // -------------------- 文件上传逻辑（本地 or 云端） --------------------
        String frontUrl;
        String backUrl;

        try {
            if (fileStorageProperties.getCloud(). isEnabled() && cloudStorageService != null) {
                // 云端优先上传
                frontUrl = cloudStorageService.upload(frontImage, "rider/idcard/front_" + currentRider.getRiderBaseId() + ". jpg");
                backUrl = cloudStorageService.upload(backImage, "rider/idcard/back_" + currentRider.getRiderBaseId() + ".jpg");
            } else {
                // 本地存储逻辑
                String basePath = RuoYiConfig.getProfile() + "/rider/idcard/";
                File dir = new File(basePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String frontFileName = "front_" + currentRider.getRiderBaseId() + "_" + System.currentTimeMillis() + ".jpg";
                String backFileName = "back_" + currentRider.getRiderBaseId() + "_" + System.currentTimeMillis() + ".jpg";

                frontImage.transferTo(new File(dir, frontFileName));
                backImage.transferTo(new File(dir, backFileName));

                // RuoYi 的静态资源路径映射为 /profile/**
                frontUrl = "/profile/rider/idcard/" + frontFileName;
                backUrl = "/profile/rider/idcard/" + backFileName;
            }
        } catch (Exception e) {
            return AjaxResult.error("文件上传失败: " + e.getMessage());
        }

        // 组装认证信息（防止越权：只能修改自己的认证信息）
        RiderBase rider = new RiderBase();
        rider.setRiderBaseId(currentRider.getRiderBaseId());
        rider.setRealName(realName);
        rider.setIdCard(idCard); // TODO: 后续加密存储
        rider.setIdCardFront(frontUrl);
        rider.setIdCardBack(backUrl);
        rider. setAuditStatus(0L); // 0 = 待审核

        int rows = riderBaseService.updateRiderAuthInfo(rider);
        if (rows > 0) {
            return AjaxResult.success("身份信息上传成功，待审核");
        }
        return AjaxResult. error("更新骑手认证信息失败");
    }
}