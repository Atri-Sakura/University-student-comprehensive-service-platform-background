package com.ruoyi.platform.mapper;

import com.ruoyi.platform.domain.OrderStatusLog;
import java.util.List;

/**
 * 订单状态日志Mapper接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface OrderStatusLogMapper {

    /**
     * 新增订单状态日志
     *
     * @param orderStatusLog 订单状态日志
     * @return 影响行数
     */
    int insertOrderStatusLog(OrderStatusLog orderStatusLog);

    /**
     * 查询订单状态日志列表
     *
     * @param orderStatusLog 订单状态日志
     * @return 订单状态日志集合
     */
    List<OrderStatusLog> selectOrderStatusLogList(OrderStatusLog orderStatusLog);
}