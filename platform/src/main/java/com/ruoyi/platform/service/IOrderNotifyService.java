package com.ruoyi.platform.service;

import com.ruoyi.platform.domain.OrderMain;

import com.ruoyi.platform.domain.dto.CreateOrderDTO;
import com.ruoyi.platform.domain.vo.CreateErrandOrderDto;

/**
 * 订单通知服务接口
 * @author 岁岁平安
 * @date 2025-12-12
 */
public interface IOrderNotifyService {

    /**
     * 发送外卖订单创建通知（给用户/商家）
     * @param orderMain 订单主信息
     * @param createOrderDTO 下单参数
     */
    void sendTakeoutOrderNotify(OrderMain orderMain, CreateOrderDTO createOrderDTO);

    /**
     * 发送跑腿订单创建通知（给骑手）
     * @param orderMain 订单主信息
     * @param createErrandOrderDto 跑腿下单参数
     */
    void sendErrandOrderNotify(OrderMain orderMain, CreateErrandOrderDto createErrandOrderDto);

    /**
     * 发送用户下单成功通知（给用户）
     * @param orderMain 订单主信息
     * @param userId 用户ID
     */
    void sendUserOrderSuccessNotify(OrderMain orderMain, Long userId);
}