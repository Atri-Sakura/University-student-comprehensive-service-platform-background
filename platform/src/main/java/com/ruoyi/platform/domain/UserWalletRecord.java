package com.ruoyi.platform.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户钱包流水对象 user_wallet_record
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Data
public class UserWalletRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流水唯一ID */
    private Long userWalletRecordId;

    /** 所属钱包ID */
    @Excel(name = "所属钱包ID")
    private Long userWalletId;

    /** 所属用户ID */
    @Excel(name = "所属用户ID")
    private Long userBaseId;

    /** 金额(正数=收入，负数=支出) */
    @Excel(name = "金额(正数=收入，负数=支出)")
    private BigDecimal amount;

    /** 交易类型：1-充值 2-提现 3-外卖支付 4-跑腿支付 5-退款 */
    @Excel(name = "交易类型：1-充值 2-提现 3-外卖支付 4-跑腿支付 5-退款")
    private Long tradeType;   // tinyint -> Long 没问题

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long relatedId;

    /** 交易状态：0-处理中 1-成功 2-失败 */
    @Excel(name = "交易状态：0-处理中 1-成功 2-失败")
    private Long tradeStatus;

    /** 交易时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "交易时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date tradeTime;

    /** 幂等键，同一笔业务唯一标识 */
    private String requestId;

    /** 支付渠道：1-支付宝 2-微信 3-银行卡 */
    private Long payChannel;

    /** 商户侧支付单号/提现单号（发给支付宝的） */
    private String outTradeNo;

    /** 三方返回的交易号(如支付宝trade_no) */
    private String channelTradeNo;

    /** 最后一次支付通知时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date notifyTime;



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userWalletRecordId", getUserWalletRecordId())
            .append("userWalletId", getUserWalletId())
            .append("userBaseId", getUserBaseId())
            .append("amount", getAmount())
            .append("tradeType", getTradeType())
            .append("relatedId", getRelatedId())
            .append("tradeStatus", getTradeStatus())
            .append("tradeTime", getTradeTime())
            .append("requestId", getRequestId())
            .append("payChannel", getPayChannel())
            .append("outTradeNo", getOutTradeNo())
            .append("channelTradeNo", getChannelTradeNo())
            .append("notifyTime", getNotifyTime())
            .append("remark", getRemark())
            .toString();
    }
}
