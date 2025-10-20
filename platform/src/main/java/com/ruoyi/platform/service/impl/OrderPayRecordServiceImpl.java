package com.ruoyi.platform.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.OrderPayRecordMapper;
import com.ruoyi.platform.domain.OrderPayRecord;
import com.ruoyi.platform.service.IOrderPayRecordService;

/**
 * 订单支付记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class OrderPayRecordServiceImpl implements IOrderPayRecordService 
{
    @Autowired
    private OrderPayRecordMapper orderPayRecordMapper;

    /**
     * 查询订单支付记录
     * 
     * @param orderPayRecordId 订单支付记录主键
     * @return 订单支付记录
     */
    @Override
    public OrderPayRecord selectOrderPayRecordByOrderPayRecordId(Long orderPayRecordId)
    {
        return orderPayRecordMapper.selectOrderPayRecordByOrderPayRecordId(orderPayRecordId);
    }

    /**
     * 查询订单支付记录列表
     * 
     * @param orderPayRecord 订单支付记录
     * @return 订单支付记录
     */
    @Override
    public List<OrderPayRecord> selectOrderPayRecordList(OrderPayRecord orderPayRecord)
    {
        return orderPayRecordMapper.selectOrderPayRecordList(orderPayRecord);
    }

    /**
     * 新增订单支付记录
     * 
     * @param orderPayRecord 订单支付记录
     * @return 结果
     */
    @Override
    public int insertOrderPayRecord(OrderPayRecord orderPayRecord)
    {
        return orderPayRecordMapper.insertOrderPayRecord(orderPayRecord);
    }

    /**
     * 修改订单支付记录
     * 
     * @param orderPayRecord 订单支付记录
     * @return 结果
     */
    @Override
    public int updateOrderPayRecord(OrderPayRecord orderPayRecord)
    {
        return orderPayRecordMapper.updateOrderPayRecord(orderPayRecord);
    }

    /**
     * 批量删除订单支付记录
     * 
     * @param orderPayRecordIds 需要删除的订单支付记录主键
     * @return 结果
     */
    @Override
    public int deleteOrderPayRecordByOrderPayRecordIds(Long[] orderPayRecordIds)
    {
        return orderPayRecordMapper.deleteOrderPayRecordByOrderPayRecordIds(orderPayRecordIds);
    }

    /**
     * 删除订单支付记录信息
     * 
     * @param orderPayRecordId 订单支付记录主键
     * @return 结果
     */
    @Override
    public int deleteOrderPayRecordByOrderPayRecordId(Long orderPayRecordId)
    {
        return orderPayRecordMapper.deleteOrderPayRecordByOrderPayRecordId(orderPayRecordId);
    }
}
