package com.ruoyi.platform.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.OrderTakeoutDetailMapper;
import com.ruoyi.platform.domain.OrderTakeoutDetail;
import com.ruoyi.platform.service.IOrderTakeoutDetailService;

/**
 * 外卖订单明细（不含地址信息）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class OrderTakeoutDetailServiceImpl implements IOrderTakeoutDetailService 
{
    @Autowired
    private OrderTakeoutDetailMapper orderTakeoutDetailMapper;

    /**
     * 查询外卖订单明细（不含地址信息）
     * 
     * @param orderTakeoutDetailId 外卖订单明细（不含地址信息）主键
     * @return 外卖订单明细（不含地址信息）
     */
    @Override
    public OrderTakeoutDetail selectOrderTakeoutDetailByOrderTakeoutDetailId(Long orderTakeoutDetailId)
    {
        return orderTakeoutDetailMapper.selectOrderTakeoutDetailByOrderTakeoutDetailId(orderTakeoutDetailId);
    }

    /**
     * 查询外卖订单明细（不含地址信息）列表
     * 
     * @param orderTakeoutDetail 外卖订单明细（不含地址信息）
     * @return 外卖订单明细（不含地址信息）
     */
    @Override
    public List<OrderTakeoutDetail> selectOrderTakeoutDetailList(OrderTakeoutDetail orderTakeoutDetail)
    {
        return orderTakeoutDetailMapper.selectOrderTakeoutDetailList(orderTakeoutDetail);
    }

    /**
     * 新增外卖订单明细（不含地址信息）
     * 
     * @param orderTakeoutDetail 外卖订单明细（不含地址信息）
     * @return 结果
     */
    @Override
    public int insertOrderTakeoutDetail(OrderTakeoutDetail orderTakeoutDetail)
    {
        return orderTakeoutDetailMapper.insertOrderTakeoutDetail(orderTakeoutDetail);
    }

    /**
     * 修改外卖订单明细（不含地址信息）
     * 
     * @param orderTakeoutDetail 外卖订单明细（不含地址信息）
     * @return 结果
     */
    @Override
    public int updateOrderTakeoutDetail(OrderTakeoutDetail orderTakeoutDetail)
    {
        return orderTakeoutDetailMapper.updateOrderTakeoutDetail(orderTakeoutDetail);
    }

    /**
     * 批量删除外卖订单明细（不含地址信息）
     * 
     * @param orderTakeoutDetailIds 需要删除的外卖订单明细（不含地址信息）主键
     * @return 结果
     */
    @Override
    public int deleteOrderTakeoutDetailByOrderTakeoutDetailIds(Long[] orderTakeoutDetailIds)
    {
        return orderTakeoutDetailMapper.deleteOrderTakeoutDetailByOrderTakeoutDetailIds(orderTakeoutDetailIds);
    }

    /**
     * 删除外卖订单明细（不含地址信息）信息
     * 
     * @param orderTakeoutDetailId 外卖订单明细（不含地址信息）主键
     * @return 结果
     */
    @Override
    public int deleteOrderTakeoutDetailByOrderTakeoutDetailId(Long orderTakeoutDetailId)
    {
        return orderTakeoutDetailMapper.deleteOrderTakeoutDetailByOrderTakeoutDetailId(orderTakeoutDetailId);
    }
}
