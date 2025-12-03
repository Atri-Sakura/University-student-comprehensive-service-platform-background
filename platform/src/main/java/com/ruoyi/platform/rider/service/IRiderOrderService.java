package com.ruoyi.platform.rider.service;

import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.rider.domain.vo.RiderOrderListVO;
import java.util.List;
import java.util.Map;

/**
 * 骑手订单服务接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface IRiderOrderService {

    /**
     * 查询可接单的订单列表
     *
     * @param orderMain 查询条件
     * @return 订单列表VO
     */
    List<RiderOrderListVO> selectAvailableOrderList(OrderMain orderMain);

    /**
     * 查询骑手自己的订单列表
     *
     * @param riderId 骑手ID
     * @param orderMain 查询条件
     * @param timeRange 时间范围
     * @return 订单列表VO
     */
    List<RiderOrderListVO> selectRiderOrderList(Long riderId, OrderMain orderMain, String timeRange);

    /**
     * 查询骑手订单详情（完整信息）
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 订单详情
     */
    OrderMain selectRiderOrderById(Long riderId, Long orderMainId);

    /**
     * 获取骑手订单统计信息
     *
     * @param riderId 骑手ID
     * @return 统计结果
     */
    Map<String, Object> getOrderStatistics(Long riderId);
}