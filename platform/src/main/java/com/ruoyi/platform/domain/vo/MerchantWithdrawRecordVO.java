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

    private Long withdrawId;
    private BigDecimal amount;
    private String status;
    private Date arriveTime;
    private String remark;

    @Override
    public String toString()
    {
        return "MerchantWithdrawRecordVO{" +
                "withdrawId=" + withdrawId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", arriveTime=" + arriveTime +
                ", remark='" + remark + '\'' +
                '}';
    }

}
