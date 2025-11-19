package com.ruoyi.platform.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetRiderPayPasswordDTO {

    @NotBlank(message = "支付密码不能为空")
    private String payPassword;

    @NotBlank(message = "确认支付密码不能为空")
    private String confirmPayPassword;

    // 预留：以后可以加短信验证码
    // private String smsCode;
}
