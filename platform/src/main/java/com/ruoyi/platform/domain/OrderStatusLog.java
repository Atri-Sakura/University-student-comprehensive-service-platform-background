package com.ruoyi.platform.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单状态变更日志对象 order_status_log
 *
 * @author ruoyi
 * @date 2025-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderStatusLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long logId;

    /** 订单ID */
    @Excel(name = "订单ID")
    private Long orderMainId;

    /** 原状态 */
    @Excel(name = "原状态")
    private Long oldStatus;

    /** 新状态 */
    @Excel(name = "新状态")
    private Long newStatus;

    /** 操作人类型 */
    @Excel(name = "操作人类型")
    private Long operatorType;

    /** 操作人ID */
    @Excel(name = "操作人ID")
    private Long operatorId;

    /** 操作人名称 */
    @Excel(name = "操作人名称")
    private String operatorName;
}