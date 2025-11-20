package com.ruoyi.platform.rider.service;

import com.ruoyi.platform.domain.OrderMain;
import java.util.List;

/**
 * 骑手订单服务接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface IRiderOrderService {

    /**
     * 查询可接单的订单列表（待取货状态且未被接单）
     *
     * @param orderMain 查询条件
     * @return 订单列表
     */
    List<OrderMain> selectAvailableOrderList(OrderMain orderMain);

    /**
     * 查询骑手自己的订单列表
     *
     * @param riderId 骑手ID
     * @param orderMain 查询条件
     * @return 订单列表
     */
    List<OrderMain> selectRiderOrderList(Long riderId, OrderMain orderMain);

    /**
     * 查询骑手订单详情
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 订单详情
     */
    OrderMain selectRiderOrderById(Long riderId, Long orderMainId);
}