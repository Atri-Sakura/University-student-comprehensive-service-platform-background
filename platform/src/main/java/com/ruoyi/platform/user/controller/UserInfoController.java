package com.ruoyi.platform.user. controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.UserBase;
import com.ruoyi.platform.service.IUserBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息用户视角接口
 * 仅允许用户访问、修改自己的个人信息
 *
 * @author ruoyi
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Autowired
    private IUserBaseService userBaseService;

    /**
     * 查询当前用户个人信息
     * 当前用户只能查自己的信息
     */
    @GetMapping
    public AjaxResult getMyInfo() {
        Long userBaseId = SecurityUtils.getUserBaseId();
        UserBase userBase = userBaseService.selectUserBaseByUserBaseId(userBaseId);
        if (userBase == null) {
            return AjaxResult. error("未找到当前用户信息");
        }
        return AjaxResult.success(userBase);
    }

    /**
     * 修改当前用户个人信息
     * 仅允许用户修改自己的信息
     */
    @PutMapping
    public AjaxResult updateMyInfo(@RequestBody UserBase userBase) {
        Long userBaseId = SecurityUtils.getUserBaseId();
        // 防止越权：只能修改自己的信息
        userBase.setUserBaseId(userBaseId);
        int rows = userBaseService. updateUserBase(userBase);
        if (rows > 0) {
            return AjaxResult.success("修改成功");
        } else {
            return AjaxResult.error("修改失败");
        }
    }

    /**
     * 上传/修改当前用户头像
     * 图片上传到MinIO，数据库只保存图片URL
     * 会自动删除旧头像，实现头像的替换
     *
     * @param file 头像文件
     * @return 上传结果(包含图片URL)
     */
    @PostMapping("/avatar")
    public AjaxResult uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // 1. 校验文件
            if (file == null || file.isEmpty()) {
                return AjaxResult.error("上传文件不能为空");
            }

            // 2. 校验文件类型(只允许图片)
            String contentType = file. getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return AjaxResult. error("只能上传图片文件");
            }

            // 3. 校验文件大小(限制5MB)
            long maxSize = 5 * 1024 * 1024; // 5MB
            if (file.getSize() > maxSize) {
                return AjaxResult.error("图片大小不能超过5MB");
            }

            // 4. 获取当前用户ID
            Long userBaseId = SecurityUtils.getUserBaseId();

            // 5. 上传到MinIO并更新数据库(Service层会自动删除旧头像)
            String avatarUrl = userBaseService.updateAvatar(file, userBaseId);

            // 6. 判断是否上传成功
            if ("error".equals(avatarUrl)) {
                return AjaxResult.error("头像上传失败");
            }

            // 7. 返回新的头像URL
            return AjaxResult.success("头像上传成功", avatarUrl);

        } catch (Exception e) {
            return AjaxResult.error("头像上传失败: " + e.getMessage());
        }
    }
}