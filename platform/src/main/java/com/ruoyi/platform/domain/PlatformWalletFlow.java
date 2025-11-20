package com.ruoyi.platform.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 平台钱包流水对象 platform_wallet_flow
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlatformWalletFlow extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 流水ID */
    private Long flowId;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderMainId;

    /** 类型 */
    @Excel(name = "类型")
    private String flowType;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal flowAmount;

    /** 变动前余额 */
    @Excel(name = "变动前余额")
    private BigDecimal balanceBefore;

    /** 变动后余额 */
    @Excel(name = "变动后余额")
    private BigDecimal balanceAfter;

    /** 变动前冻结金额 */
    @Excel(name = "变动前冻结金额")
    private BigDecimal freezeBefore;

    /** 变动后冻结金额 */
    @Excel(name = "变动后冻结金额")
    private BigDecimal freezeAfter;

    /** 描述 */
    @Excel(name = "描述")
    private String description;
}