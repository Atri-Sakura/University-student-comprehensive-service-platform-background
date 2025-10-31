package com.ruoyi.platform.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 提现状态返回对象
 */
@Data
public class WithdrawStatusVO {
    private Long withdrawId;
    private String withdrawStatus; // PENDING / SUCCESS / FAILED
    private String remark;         // 失败原因等
    private Date processTime;      // 处理完成时间
}
