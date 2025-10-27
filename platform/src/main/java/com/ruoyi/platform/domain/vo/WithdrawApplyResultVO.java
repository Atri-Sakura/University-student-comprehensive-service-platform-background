package com.ruoyi.platform.domain.vo;

import lombok.Data;

/**
 * 提现申请结果VO
 *
 * @date 2025/10/24
 */
@Data
public class WithdrawApplyResultVO {

    private Long withdrawId;
    private String withdrawStatus;
    private String message;

}
