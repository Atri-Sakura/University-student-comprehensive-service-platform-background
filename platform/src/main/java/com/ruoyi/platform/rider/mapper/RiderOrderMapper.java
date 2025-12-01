package com.ruoyi.platform.rider.mapper;

import com.ruoyi.platform.domain.OrderMain;
import org.apache.ibatis.annotations. Param;
import java.util.List;

/**
 * 骑手订单Mapper接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface RiderOrderMapper {

    /**
     * 查询可接单的订单列表
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
    List<OrderMain> selectRiderOrderList(@Param("riderId") Long riderId,
                                         @Param("orderMain") OrderMain orderMain);

    /**
     * 查询骑手订单详情
     *
     * @param riderId 骑手ID
     * @param orderMainId 订单ID
     * @return 订单详情
     */
    OrderMain selectRiderOrderById(@Param("riderId") Long riderId,
                                   @Param("orderMainId") Long orderMainId);
}