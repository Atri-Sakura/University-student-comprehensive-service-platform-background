package com.ruoyi.platform.controller1.rider;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.vo.RiderBaseInfoVO;
import com.ruoyi.platform.service.IRiderBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rider/base")
@RequiredArgsConstructor
public class RiderBaseController1 {

    private final IRiderBaseService riderBaseService;

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
