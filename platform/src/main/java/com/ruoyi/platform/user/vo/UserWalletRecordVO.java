package com.ruoyi.platform.user.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserWalletRecordVO {
    /** 流水唯一ID */
    private Long userWalletRecordId;

    /** 所属钱包ID */
    private Long userWalletId;

    /** 所属用户ID */
    private Long userBaseId;

    /** 金额(正数=收入，负数=支出) */
    private BigDecimal amount;

    /** 交易类型：1-充值 2-提现 3-外卖支付 4-跑腿支付 5-退款 */
    private Long tradeType;

    /** 关联订单ID */
    private Long relatedId;

    /** 交易状态：0-处理中 1-成功 2-失败 */
    private Long tradeStatus;

    /** 交易时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tradeTime;

    /** 备注 */
    private String remark;
}
