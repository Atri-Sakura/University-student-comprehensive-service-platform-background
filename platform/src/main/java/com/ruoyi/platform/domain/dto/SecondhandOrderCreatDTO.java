package com.ruoyi.platform.domain.dto;

import lombok.Data;

/**
 * 二手订单创建的参数
 */
@Data
public class SecondhandOrderCreatDTO {
    private Long goodsId;
    private String receiverName;
    private String receiverPhone;
    private String tradePlace;
    private Long payType;  // 1余额 2微信 3支付宝 4面付
    private String remark;
    private String requestId; // 幂等键，可空
}
