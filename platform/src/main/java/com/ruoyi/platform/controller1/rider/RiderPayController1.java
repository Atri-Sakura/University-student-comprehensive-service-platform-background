package com.ruoyi.platform.controller1.rider;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.platform.domain.dto.RiderChangePayPasswordDTO;
import com.ruoyi.platform.domain.dto.SetRiderPayPasswordDTO;
import com.ruoyi.platform.service.IRiderBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rider/pay")
public class RiderPayController1 {
    private final IRiderBaseService riderBaseService;

    /**
     * 修改支付密码
     */
    @PostMapping("/password/update")
    public AjaxResult updatePayPassword(@Validated @RequestBody RiderChangePayPasswordDTO dto){
        //1。检验新旧支付密码是否一致
        if(!dto.getNewPayPassword().equals(dto.getConfirmNewPayPassword())){
            return AjaxResult.error("新密码与确认密码不一致");
        }
        Long riderBaseId = SecurityUtils.getRiderBaseId();

        // 3. 调用 Service 以当前登录用户为基础修改骑手支付密码
        riderBaseService.changePayPassword(riderBaseId, dto.getOldPayPassword(), dto.getNewPayPassword());
        return AjaxResult.success("支付密码修改成功");
    }

    /**
     * 设置支付密码
     */
    @PostMapping("/password/set")
    public AjaxResult setPayPassword(@Validated @RequestBody SetRiderPayPasswordDTO dto){
        //1。检验新旧支付密码是否一致
        if(!dto.getPayPassword().equals(dto.getConfirmPayPassword())){
            return AjaxResult.error("新密码与确认密码不一致");
        }
        Long riderBaseId = SecurityUtils.getRiderBaseId();

        // 3. 调用 Service 以当前登录用户为基础设置骑手支付密码
        riderBaseService.setPayPassword(riderBaseId, dto.getPayPassword());
        return AjaxResult.success("支付密码设置成功");
    }

}
