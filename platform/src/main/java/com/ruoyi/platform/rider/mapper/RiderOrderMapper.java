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
     *
     * @param orderMain 查询条件
     * @return 订单列表VO
     */
    List<RiderOrderListVO> selectAvailableOrderList(OrderMain orderMain);

    /**
     * 查询骑手自己的订单列表（简化字段）
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

    @Update("update order_main set order_status = 7 and cancel_reason = #{cancelReason} and cancel_operator = '骑手' where order_main_id = #{orderMainId} and order_status = 3")
    int reportAbnormal(Long riderId, OrderMain orderMain);

    @Update("update order_delivery set delivery_status = 4 where order_main_id = #{orderMainId} and rider_id = #{riderId}")
    int reportAbnormal1(Long riderId, OrderMain orderMain);
}