package com.ruoyi.platform.domain.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 支付订单DTO
 *
 * @author Moli2580
 * @date 2025-11-18
 */
@Data
public class PayOrderDTO {

    /** 预订单号 */
    @NotBlank(message = "预订单号不能为空")
    private String preOrderNo;

    /** 支付金额 */
    @NotNull(message = "支付金额不能为空")
    private BigDecimal payAmount;

    /** 支付方式：1-余额 2-微信 3-支付宝 */
    @NotNull(message = "支付方式不能为空")
    private Long payType;

    /** 支付密码（余额支付时需要） */
    private String payPassword;
}