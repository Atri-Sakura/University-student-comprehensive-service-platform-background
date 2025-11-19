package com.ruoyi.platform.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 平台钱包对象 platform_wallet
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlatformWallet extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 平台钱包ID */
    private Long platformWalletId;

    /** 可用余额 */
    @Excel(name = "可用余额")
    private BigDecimal balance;

    /** 冻结金额 */
    @Excel(name = "冻结金额")
    private BigDecimal freezeAmount;

    /** 累计收入 */
    @Excel(name = "累计收入")
    private BigDecimal totalIncome;

    /** 累计支出 */
    @Excel(name = "累计支出")
    private BigDecimal totalPayout;
}