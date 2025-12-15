package com.ruoyi.platform.service;

import com.ruoyi.platform.domain.OrderMain;

import java.math.BigDecimal;

/**
 * 订单流转服务接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface IOrderFlowService {

    /**
     * 商家接单
     *
     * @param merchantId 商家ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    int merchantAcceptOrder(Long merchantId, Long orderMainId);

    /**
     * 商家拒单
     *
     * @param merchantId 商家ID
     * @param orderMainId 订单ID
     * @param refuseReason 拒单原因
     * @return 结果
     */
    int merchantRejectOrder(Long merchantId, Long orderMainId, String refuseReason);

    /**
     * 骑手接单
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    int riderAcceptOrder(Long riderId, Long orderMainId);

    /**
     * 骑手取货
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    int riderPickupOrder(Long riderId, Long orderMainId,
                         BigDecimal actualPickLongitude,
                         BigDecimal actualPickLatitude);

    /**
     * 骑手送达
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    int riderDeliverOrder(Long riderId, Long orderMainId,
                          BigDecimal actualDeliverLongitude,
                          BigDecimal actualDeliverLatitude);
}