package com.ruoyi.platform.service;

import java.util.List;
import com.ruoyi.platform.domain.OrderTakeoutDetail;

/**
 * 外卖订单明细（不含地址信息）Service接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface IOrderTakeoutDetailService 
{
    /**
     * 查询外卖订单明细（不含地址信息）
     * 
     * @param orderTakeoutDetailId 外卖订单明细（不含地址信息）主键
     * @return 外卖订单明细（不含地址信息）
     */
    public OrderTakeoutDetail selectOrderTakeoutDetailByOrderTakeoutDetailId(Long orderTakeoutDetailId);

    /**
     * 查询外卖订单明细（不含地址信息）列表
     * 
     * @param orderTakeoutDetail 外卖订单明细（不含地址信息）
     * @return 外卖订单明细（不含地址信息）集合
     */
    public List<OrderTakeoutDetail> selectOrderTakeoutDetailList(OrderTakeoutDetail orderTakeoutDetail);

    /**
     * 新增外卖订单明细（不含地址信息）
     * 
     * @param orderTakeoutDetail 外卖订单明细（不含地址信息）
     * @return 结果
     */
    public int insertOrderTakeoutDetail(OrderTakeoutDetail orderTakeoutDetail);

    /**
     * 修改外卖订单明细（不含地址信息）
     * 
     * @param orderTakeoutDetail 外卖订单明细（不含地址信息）
     * @return 结果
     */
    public int updateOrderTakeoutDetail(OrderTakeoutDetail orderTakeoutDetail);

    /**
     * 批量删除外卖订单明细（不含地址信息）
     * 
     * @param orderTakeoutDetailIds 需要删除的外卖订单明细（不含地址信息）主键集合
     * @return 结果
     */
    public int deleteOrderTakeoutDetailByOrderTakeoutDetailIds(Long[] orderTakeoutDetailIds);

    /**
     * 删除外卖订单明细（不含地址信息）信息
     * 
     * @param orderTakeoutDetailId 外卖订单明细（不含地址信息）主键
     * @return 结果
     */
    public int deleteOrderTakeoutDetailByOrderTakeoutDetailId(Long orderTakeoutDetailId);
}
