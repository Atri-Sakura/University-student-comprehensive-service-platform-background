package com.ruoyi.platform.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.OrderErrandDetailMapper;
import com.ruoyi.platform.domain.OrderErrandDetail;
import com.ruoyi.platform.service.IOrderErrandDetailService;

/**
 * 跑腿订单明细（不含地址信息）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
@Service
public class OrderErrandDetailServiceImpl implements IOrderErrandDetailService 
{
    @Autowired
    private OrderErrandDetailMapper orderErrandDetailMapper;

    /**
     * 查询跑腿订单明细（不含地址信息）
     * 
     * @param orderErrandDetailId 跑腿订单明细（不含地址信息）主键
     * @return 跑腿订单明细（不含地址信息）
     */
    @Override
    public OrderErrandDetail selectOrderErrandDetailByOrderErrandDetailId(Long orderErrandDetailId)
    {
        return orderErrandDetailMapper.selectOrderErrandDetailByOrderErrandDetailId(orderErrandDetailId);
    }

    /**
     * 查询跑腿订单明细（不含地址信息）列表
     * 
     * @param orderErrandDetail 跑腿订单明细（不含地址信息）
     * @return 跑腿订单明细（不含地址信息）
     */
    @Override
    public List<OrderErrandDetail> selectOrderErrandDetailList(OrderErrandDetail orderErrandDetail)
    {
        return orderErrandDetailMapper.selectOrderErrandDetailList(orderErrandDetail);
    }

    /**
     * 新增跑腿订单明细（不含地址信息）
     * 
     * @param orderErrandDetail 跑腿订单明细（不含地址信息）
     * @return 结果
     */
    @Override
    public int insertOrderErrandDetail(OrderErrandDetail orderErrandDetail)
    {
        return orderErrandDetailMapper.insertOrderErrandDetail(orderErrandDetail);
    }

    /**
     * 修改跑腿订单明细（不含地址信息）
     * 
     * @param orderErrandDetail 跑腿订单明细（不含地址信息）
     * @return 结果
     */
    @Override
    public int updateOrderErrandDetail(OrderErrandDetail orderErrandDetail)
    {
        return orderErrandDetailMapper.updateOrderErrandDetail(orderErrandDetail);
    }

    /**
     * 批量删除跑腿订单明细（不含地址信息）
     * 
     * @param orderErrandDetailIds 需要删除的跑腿订单明细（不含地址信息）主键
     * @return 结果
     */
    @Override
    public int deleteOrderErrandDetailByOrderErrandDetailIds(Long[] orderErrandDetailIds)
    {
        return orderErrandDetailMapper.deleteOrderErrandDetailByOrderErrandDetailIds(orderErrandDetailIds);
    }

    /**
     * 删除跑腿订单明细（不含地址信息）信息
     * 
     * @param orderErrandDetailId 跑腿订单明细（不含地址信息）主键
     * @return 结果
     */
    @Override
    public int deleteOrderErrandDetailByOrderErrandDetailId(Long orderErrandDetailId)
    {
        return orderErrandDetailMapper.deleteOrderErrandDetailByOrderErrandDetailId(orderErrandDetailId);
    }
}
