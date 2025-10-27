package com.ruoyi.platform.merchant.mapper;

import java.util.List;
import com.ruoyi.platform.domain.OrderMain;

/**
 * 订单主Mapper接口
 *
 * @author ruoyi
 * @date 2025-10-20
 */
public interface MerchantOrderMapper
{
    /**
     * 查询商家订单列表
     *
     * @param orderMain 订单主信息（包含merchantId和查询条件）
     * @return 订单主集合
     */
    public List<OrderMain> selectMerchantOrderList(OrderMain orderMain);

    /**
     * 根据订单ID查询商家订单详情
     *
     * @param orderMain 查询条件（必须包含 orderMainId 和 merchantId）
     * @return 订单主对象（包含商品详情列表）
     */
    public OrderMain selectMerchantOrderByOrderMainId(OrderMain orderMain);

    /**
     * 根据ID查询订单主信息（用于内部服务校验）
     * @param orderMainId 订单ID
     * @return 订单主信息
     */
    public OrderMain selectOrderMainByOrderMainId(Long orderMainId);

    /**
     * 修改订单主信息
     *
     * @param orderMain 订单主信息
     * @return 结果
     */
    public int updateOrderMain(OrderMain orderMain);

    OrderMain selectMerchantOrderById(Long merchantId, Long orderMainId);
}