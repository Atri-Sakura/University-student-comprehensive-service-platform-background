package com.ruoyi.platform.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RiderChangePayPasswordDTO {
    @NotBlank(message = "原支付密码不能为空")
    private String oldPayPassword;

    @NotBlank(message = "新支付密码不能为空")
    private String newPayPassword;

    @NotBlank(message = "确认新支付密码不能为空")
    private String confirmNewPayPassword;

    //预留短信sms验证码

}
