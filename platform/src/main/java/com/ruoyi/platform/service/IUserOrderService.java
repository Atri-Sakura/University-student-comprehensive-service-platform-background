package com.ruoyi.platform.service;

import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.dto.CreateOrderDTO;
import com.ruoyi.platform.domain.dto.PayOrderDTO;
import com.ruoyi.platform.domain.dto.PrePayOrderDTO;
import com.ruoyi.platform.domain.dto.CreateErrandOrderDto;

/**
 * 用户订单服务接口
 *
 * @author ruoyi
 * @date 2025-11-13
 */
public interface IUserOrderService {

    /**
     * 创建预支付订单（只校验，不真正创建订单）
     *
     * @param createOrderDTO 订单创建DTO
     * @return 预支付订单信息
     */
    PrePayOrderDTO createPrePayOrder(CreateOrderDTO createOrderDTO);

    /**
     * 支付并创建订单（先扣款，再创建订单）
     *
     * @param userId 用户ID
     * @param payOrderDTO 支付订单DTO
     * @return 订单信息
     */
    OrderMain payAndCreateOrder(Long userId, PayOrderDTO payOrderDTO);

    /**
     * 创建预支付跑腿订单（只校验，不真正创建订单）
     * @param createErrandOrderDto
     * @return
     */
    PrePayOrderDTO createPrePayErrandOrder(CreateErrandOrderDto createErrandOrderDto);


    /**
     * 取消预支付订单
     *
     * @param userId 用户ID
     * @param preOrderNo 预订单号
     * @return 结果
     */
    boolean cancelPrePayOrder(Long userId, String preOrderNo);

    /**
     * 用户取消订单
     *
     * @param userId 用户ID
     * @param orderMainId 订单ID
     * @param cancelReason 取消原因
     * @return 结果
     */
    int cancelOrder(Long userId, Long orderMainId, String cancelReason);

    /**
     * 用户确认收货
     *
     * @param userId 用户ID
     * @param orderMainId 订单ID
     * @return 结果
     */
    int confirmReceive(Long userId, Long orderMainId);

    /**
     * 用户确认跑腿订单收货
     * @param userId
     * @param orderMainId
     * @return
     */
    int confirmReceiveErrand(Long userId, Long orderMainId,Long riderId);

    /**
     * 创建并支付跑腿订单
     * @param userId
     * @param payOrderDTO
     * @return
     */
    OrderMain payAndCreateErrandOrder(Long userId, PayOrderDTO payOrderDTO,Long userAddressId);

    /**
     * 取消跑腿预支付订单
     * @param userId
     * @param preOrderNo
     * @return
     */
    boolean cancelPrePayErrandOrder(Long userId, String preOrderNo);
}