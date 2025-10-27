package com.ruoyi.platform.domain.dto;

import lombok.Data;

/**
 * 添加商家提现账户 DTO
 *
 * @author Jinx
 * @date 2025-10-26
 */
@Data
public class MerchantWithdrawAccountAddDTO {
    private String accountType;
    private String accountName;
    private String accountNumber;
    private String bankName;
    private Integer isDefault = 0;
    private String remark;
}
