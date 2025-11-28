package com.ruoyi.platform.mapper;

import com.ruoyi.platform.domain.PayOrder;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayOrderMapper {

    int insertPayOrder(PayOrder payOrder);

    int updateStatusById(@Param("payOrderId") Long payOrderId,
                         @Param("status") Integer status);

    PayOrder selectByOutTradeNo(@Param("outTradeNo") String outTradeNo);

    /**
     * 根据主键更新（用于支付回调）
     */
    int updateById(PayOrder payOrder);
}
