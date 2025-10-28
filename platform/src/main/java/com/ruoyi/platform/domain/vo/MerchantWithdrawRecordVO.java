package com.ruoyi.platform.domain.vo;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家提现记录 VO
 */
@Data
public class MerchantWithdrawRecordVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 提现记录ID */
    private Long withdrawId;

    /** 提现金额 */
    private BigDecimal amount;

    /** 提现状态 */
    private String status;

    /** 银行名称（从提现账户表冗余） */
    private String bankName;

    /** 到账时间 / 处理时间 */
    private Date arriveTime;

    /** 备注 */
    private String remark;

    @Override
    public String toString() {
        return "MerchantWithdrawRecordVO{" +
                "withdrawId=" + withdrawId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", bankName='" + bankName + '\'' +
                ", arriveTime=" + arriveTime +
                ", remark='" + remark + '\'' +
                '}';
    }

}
