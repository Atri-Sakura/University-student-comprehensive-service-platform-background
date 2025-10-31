package com.ruoyi.platform.merchant.service;

import java.util.List;
import com.ruoyi.platform.domain.OrderMain;

/**
 * 商家订单服务接口
 *
 * @author ruoyi
 */
public interface IMerchantOrderService {

    /**
     * 查询商家订单列表
     *
     * @param merchantId 商家ID
     * @param orderMain 查询条件
     * @return 订单列表
     */
    public List<OrderMain> selectMerchantOrderList(Long merchantId, OrderMain orderMain);

    /**
     * 查询商家订单详情
     *
     * @param merchantId 商家ID
     * @param orderMainId 订单ID
     * @return 订单详情
     */
    public OrderMain selectMerchantOrderById(Long merchantId, Long orderMainId);

    int acceptOrder(Long merchantId, Long orderMainId);

    public int rejectOrder(Long merchantId, Long orderMainId, String cancelOperator);
}