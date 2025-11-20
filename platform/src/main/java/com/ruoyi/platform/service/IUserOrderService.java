package com.ruoyi.platform.service;

import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.dto.CreateOrderDTO;
import com.ruoyi.platform.domain.dto.PayOrderDTO;
import com.ruoyi.platform.domain.dto.PrePayOrderDTO;

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
     * 用户创建外卖订单（保留旧接口，标记为废弃）
     *
     * @param createOrderDTO 订单创建DTO
     * @return 订单信息
     * @deprecated 建议使用 createPrePayOrder + payAndCreateOrder
     */
    @Deprecated
    OrderMain createTakeoutOrder(CreateOrderDTO createOrderDTO);

    /**
     * 用户支付订单（保留旧接口，标记为废弃）
     *
     * @param userId 用户ID
     * @param orderNo 订单编号
     * @return 支付结果
     * @deprecated 建议使用 payAndCreateOrder
     */
    @Deprecated
    boolean payOrder(Long userId, String orderNo);

    /**
     * 用户取消订单（仅待支付状态可取消）
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
     * 取消预支付订单
     *
     * @param userId 用户ID
     * @param preOrderNo 预订单号
     * @return 结果
     */
    boolean cancelPrePayOrder(Long userId, String preOrderNo);
}