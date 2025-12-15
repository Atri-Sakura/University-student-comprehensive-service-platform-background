package com.ruoyi.platform.rider.mapper;

import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.rider.domain.vo.RiderOrderListVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 骑手订单Mapper接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface RiderOrderMapper {

    /**
     * 查询可接单的订单列表（简化字段）
     * 状态必须是：2-骑手待接单
     *
     * @param orderMain 查询条件
     * @return 订单列表VO
     */
    List<RiderOrderListVO> selectAvailableOrderList(OrderMain orderMain);

    /**
     * 查询骑手自己的订单列表（简化字段）
     * 包含状态：3-骑手待取货, 4-配送中, 5-已完成, 7-骑手异常报备
     *
     * @param riderId 骑手ID
     * @param orderMain 查询条件
     * @param timeRange 时间范围
     * @return 订单列表VO
     */
    List<RiderOrderListVO> selectRiderOrderList(@Param("riderId") Long riderId,
                                                @Param("orderMain") OrderMain orderMain,
                                                @Param("timeRange") String timeRange);

    /**
     * 查询骑手订单详情（完整字段）
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 订单详情
     */
    OrderMain selectRiderOrderById(@Param("riderId") Long riderId,
                                   @Param("orderMainId") Long orderMainId);

    /**
     * 按时间范围统计订单数量
     *
     * @param riderId 骑手ID
     * @param timeRange 时间范围
     * @return 订单数量
     */
    int countByTimeRange(@Param("riderId") Long riderId,
                         @Param("timeRange") String timeRange);

    /**
     * 骑手异常报备 - 更新订单状态为 7-骑手异常报备
     * 状态流转：3-骑手待取货 或 4-配送中 → 7-骑手异常报备
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @param cancelReason 异常原因
     * @return 影响行数
     */
    @Update("UPDATE order_main SET order_status = 7, cancel_reason = #{cancelReason}, " +
            "cancel_operator = '骑手', update_time = NOW() " +
            "WHERE order_main_id = #{orderMainId} AND order_status IN (3, 4)")
    int reportAbnormal(@Param("riderId") Long riderId,
                       @Param("orderMainId") Long orderMainId,
                       @Param("cancelReason") String cancelReason);

    /**
     * 骑手异常报备 - 更新配送状态为异常
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 影响行数
     */
    @Update("UPDATE order_delivery SET delivery_status = 4, update_time = NOW() " +
            "WHERE order_main_id = #{orderMainId} AND rider_id = #{riderId} " +
            "AND delivery_status IN (1, 2)")
    int reportAbnormal1(@Param("riderId") Long riderId,
                        @Param("orderMainId") Long orderMainId);
}