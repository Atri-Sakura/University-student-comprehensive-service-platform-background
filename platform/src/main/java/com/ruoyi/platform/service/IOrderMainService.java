package com.ruoyi.platform.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.platform.domain.OrderMain;

/**
 * 订单主（整合地址与定位信息）Service接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface IOrderMainService 
{
    /**
     * 查询订单主（整合地址与定位信息）
     * 
     * @param orderMainId 订单主（整合地址与定位信息）主键
     * @return 订单主（整合地址与定位信息）
     */
    public OrderMain selectOrderMainByOrderMainId(Long orderMainId);

    /**
     * 查询订单主（整合地址与定位信息）列表
     * 
     * @param orderMain 订单主（整合地址与定位信息）
     * @return 订单主（整合地址与定位信息）集合
     */
    public List<OrderMain> selectOrderMainList(OrderMain orderMain);

    /**
     * 新增订单主（整合地址与定位信息）
     * 
     * @param orderMain 订单主（整合地址与定位信息）
     * @return 结果
     */
    public int insertOrderMain(OrderMain orderMain);

    /**
     * 修改订单主（整合地址与定位信息）
     * 
     * @param orderMain 订单主（整合地址与定位信息）
     * @return 结果
     */
    public int updateOrderMain(OrderMain orderMain);

    /**
     * 批量删除订单主（整合地址与定位信息）
     * 
     * @param orderMainIds 需要删除的订单主（整合地址与定位信息）主键集合
     * @return 结果
     */
    public int deleteOrderMainByOrderMainIds(Long[] orderMainIds);

    /**
     * 删除订单主（整合地址与定位信息）信息
     * 
     * @param orderMainId 订单主（整合地址与定位信息）主键
     * @return 结果
     */
    public int deleteOrderMainByOrderMainId(Long orderMainId);

    /**
     * 查询商家今日订单收入
     */
    Map<String, Object> selectMerchantTodayIncome(Long merchantId, Date startTime, Date endTime);

    /**
     * 查询商家今日退款金额
     */
    BigDecimal selectMerchantTodayRefund(Long merchantId, Date startTime, Date endTime);

    /**
     * 订单完成处理(含钱包流水记录)
     *
     * @param orderMainId 订单ID
     * @return 结果
     */
    int handleOrderComplete(Long orderMainId);

    /**
     * 订单退款处理(含钱包流水记录)
     *
     * @param orderMainId 订单ID
     * @param refundReason 退款原因
     * @return 结果
     */
    int handleOrderRefund(Long orderMainId, String refundReason);

    OrderMain selectByOrderNo(String orderNo);
}
