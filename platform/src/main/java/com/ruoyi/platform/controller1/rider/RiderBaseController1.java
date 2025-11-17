package com.ruoyi.platform.controller1.rider;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.platform.domain.dto.RiderChangePasswordDTO;
import com.ruoyi.platform.domain.vo.RiderBaseInfoVO;
import com.ruoyi.platform.service.IRiderBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rider/base")
@RequiredArgsConstructor
public class RiderBaseController1 {

    private final IRiderBaseService riderBaseService;

    private final TokenService tokenService;
    @PostMapping("password")
    public AjaxResult updatePassword(@Validated @RequestBody RiderChangePasswordDTO dto){
        // 1. 先校验两次新密码是否一致（这里做，用户体验更友好）
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return AjaxResult.error("两次输入的新密码不一致");
        }
        // 2. 获取当前登录用户（这里写 riderId 或 userId 看你怎么关联）
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long riderId = loginUser.getRiderBaseId();

        // 3. 调 service 做旧密码校验 + 新密码规则校验 + 更新密码
        riderBaseService.changePassword(riderId, dto.getOldPassword(), dto.getNewPassword());

        // 4. 删除当前 token 会话（让当前登录立即失效）
        String token = loginUser.getToken();
        tokenService.delLoginUser(token);
        return AjaxResult.success("密码修改成功");

    }
    /**
     * 修改骑手基础信息(nickname,avatar,phone)
     */
    @PostMapping("/update")
    public AjaxResult updateRiderBaseInfo(
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String phone,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar
    ){
        Long riderBaseId = SecurityUtils.getRiderBaseId();
        if (riderBaseId == null) {
            return AjaxResult.error("请先登录");
        }
        boolean success = riderBaseService.updateRiderBaseInfo(riderBaseId, nickname, phone, avatar);
        return success ? AjaxResult.success("修改成功") : AjaxResult.error("修改失败");
    }
    /**
     * 获取骑手基础信息
     */
    @GetMapping("/info")
    public AjaxResult getRiderInfo() {
        Long riderId = SecurityUtils.getRiderBaseId();
        RiderBaseInfoVO info = riderBaseService.getRiderBaseInfo(riderId);
        if (info == null) {
            return AjaxResult.error("未找到该骑手信息");
        }
        return AjaxResult.success(info);
    }


}
