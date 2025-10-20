package com.ruoyi.platform.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.OrderSecondhandDetailMapper;
import com.ruoyi.platform.domain.OrderSecondhandDetail;
import com.ruoyi.platform.service.IOrderSecondhandDetailService;

/**
 * 二手交易订单明细（不含地址信息）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class OrderSecondhandDetailServiceImpl implements IOrderSecondhandDetailService 
{
    @Autowired
    private OrderSecondhandDetailMapper orderSecondhandDetailMapper;

    /**
     * 查询二手交易订单明细（不含地址信息）
     * 
     * @param orderSecondhandDetailId 二手交易订单明细（不含地址信息）主键
     * @return 二手交易订单明细（不含地址信息）
     */
    @Override
    public OrderSecondhandDetail selectOrderSecondhandDetailByOrderSecondhandDetailId(Long orderSecondhandDetailId)
    {
        return orderSecondhandDetailMapper.selectOrderSecondhandDetailByOrderSecondhandDetailId(orderSecondhandDetailId);
    }

    /**
     * 查询二手交易订单明细（不含地址信息）列表
     * 
     * @param orderSecondhandDetail 二手交易订单明细（不含地址信息）
     * @return 二手交易订单明细（不含地址信息）
     */
    @Override
    public List<OrderSecondhandDetail> selectOrderSecondhandDetailList(OrderSecondhandDetail orderSecondhandDetail)
    {
        return orderSecondhandDetailMapper.selectOrderSecondhandDetailList(orderSecondhandDetail);
    }

    /**
     * 新增二手交易订单明细（不含地址信息）
     * 
     * @param orderSecondhandDetail 二手交易订单明细（不含地址信息）
     * @return 结果
     */
    @Override
    public int insertOrderSecondhandDetail(OrderSecondhandDetail orderSecondhandDetail)
    {
        return orderSecondhandDetailMapper.insertOrderSecondhandDetail(orderSecondhandDetail);
    }

    /**
     * 修改二手交易订单明细（不含地址信息）
     * 
     * @param orderSecondhandDetail 二手交易订单明细（不含地址信息）
     * @return 结果
     */
    @Override
    public int updateOrderSecondhandDetail(OrderSecondhandDetail orderSecondhandDetail)
    {
        return orderSecondhandDetailMapper.updateOrderSecondhandDetail(orderSecondhandDetail);
    }

    /**
     * 批量删除二手交易订单明细（不含地址信息）
     * 
     * @param orderSecondhandDetailIds 需要删除的二手交易订单明细（不含地址信息）主键
     * @return 结果
     */
    @Override
    public int deleteOrderSecondhandDetailByOrderSecondhandDetailIds(Long[] orderSecondhandDetailIds)
    {
        return orderSecondhandDetailMapper.deleteOrderSecondhandDetailByOrderSecondhandDetailIds(orderSecondhandDetailIds);
    }

    /**
     * 删除二手交易订单明细（不含地址信息）信息
     * 
     * @param orderSecondhandDetailId 二手交易订单明细（不含地址信息）主键
     * @return 结果
     */
    @Override
    public int deleteOrderSecondhandDetailByOrderSecondhandDetailId(Long orderSecondhandDetailId)
    {
        return orderSecondhandDetailMapper.deleteOrderSecondhandDetailByOrderSecondhandDetailId(orderSecondhandDetailId);
    }
}
