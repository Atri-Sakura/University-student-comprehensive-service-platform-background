package com.ruoyi.platform.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 商家提现账户 VO
 *
 * @author Jinx
 * @date 2025-10-26
 */
@Data
public class MerchantWithdrawAccountVO {
    private Long accountId;          // 账户ID
    private String accountType;      // 账户类型：bank/alipay/wechat
    private String accountName;      // 户名
    private String accountNumber;    // 账号（敏感部分打码）
    private String bankName;         // 银行名称（仅银行卡）
    private Integer isDefault;       // 是否默认账户
    private Integer status;          // 状态：1启用 0禁用
    private Date createTime;         // 创建时间
}
