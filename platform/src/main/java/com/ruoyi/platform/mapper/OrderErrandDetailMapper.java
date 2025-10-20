package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.OrderErrandDetail;

/**
 * 跑腿订单明细（不含地址信息）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
public interface OrderErrandDetailMapper 
{
    /**
     * 查询跑腿订单明细（不含地址信息）
     * 
     * @param orderErrandDetailId 跑腿订单明细（不含地址信息）主键
     * @return 跑腿订单明细（不含地址信息）
     */
    public OrderErrandDetail selectOrderErrandDetailByOrderErrandDetailId(Long orderErrandDetailId);

    /**
     * 查询跑腿订单明细（不含地址信息）列表
     * 
     * @param orderErrandDetail 跑腿订单明细（不含地址信息）
     * @return 跑腿订单明细（不含地址信息）集合
     */
    public List<OrderErrandDetail> selectOrderErrandDetailList(OrderErrandDetail orderErrandDetail);

    /**
     * 新增跑腿订单明细（不含地址信息）
     * 
     * @param orderErrandDetail 跑腿订单明细（不含地址信息）
     * @return 结果
     */
    public int insertOrderErrandDetail(OrderErrandDetail orderErrandDetail);

    /**
     * 修改跑腿订单明细（不含地址信息）
     * 
     * @param orderErrandDetail 跑腿订单明细（不含地址信息）
     * @return 结果
     */
    public int updateOrderErrandDetail(OrderErrandDetail orderErrandDetail);

    /**
     * 删除跑腿订单明细（不含地址信息）
     * 
     * @param orderErrandDetailId 跑腿订单明细（不含地址信息）主键
     * @return 结果
     */
    public int deleteOrderErrandDetailByOrderErrandDetailId(Long orderErrandDetailId);

    /**
     * 批量删除跑腿订单明细（不含地址信息）
     * 
     * @param orderErrandDetailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderErrandDetailByOrderErrandDetailIds(Long[] orderErrandDetailIds);
}
