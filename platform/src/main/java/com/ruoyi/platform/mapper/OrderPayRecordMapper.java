package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.OrderPayRecord;

/**
 * 订单支付记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface OrderPayRecordMapper 
{
    /**
     * 查询订单支付记录
     * 
     * @param orderPayRecordId 订单支付记录主键
     * @return 订单支付记录
     */
    public OrderPayRecord selectOrderPayRecordByOrderPayRecordId(Long orderPayRecordId);

    /**
     * 查询订单支付记录列表
     * 
     * @param orderPayRecord 订单支付记录
     * @return 订单支付记录集合
     */
    public List<OrderPayRecord> selectOrderPayRecordList(OrderPayRecord orderPayRecord);

    /**
     * 新增订单支付记录
     * 
     * @param orderPayRecord 订单支付记录
     * @return 结果
     */
    public int insertOrderPayRecord(OrderPayRecord orderPayRecord);

    /**
     * 修改订单支付记录
     * 
     * @param orderPayRecord 订单支付记录
     * @return 结果
     */
    public int updateOrderPayRecord(OrderPayRecord orderPayRecord);

    /**
     * 删除订单支付记录
     * 
     * @param orderPayRecordId 订单支付记录主键
     * @return 结果
     */
    public int deleteOrderPayRecordByOrderPayRecordId(Long orderPayRecordId);

    /**
     * 批量删除订单支付记录
     * 
     * @param orderPayRecordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderPayRecordByOrderPayRecordIds(Long[] orderPayRecordIds);
}
