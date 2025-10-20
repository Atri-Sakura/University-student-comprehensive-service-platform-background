package com.ruoyi.platform.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.platform.mapper.OrderMainMapper;
import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.service.IOrderMainService;

/**
 * 订单主（整合地址与定位信息）Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-10-16
 */
@Service
public class OrderMainServiceImpl implements IOrderMainService 
{
    @Autowired
    private OrderMainMapper orderMainMapper;

    /**
     * 查询订单主（整合地址与定位信息）
     * 
     * @param orderMainId 订单主（整合地址与定位信息）主键
     * @return 订单主（整合地址与定位信息）
     */
    @Override
    public OrderMain selectOrderMainByOrderMainId(Long orderMainId)
    {
        return orderMainMapper.selectOrderMainByOrderMainId(orderMainId);
    }

    /**
     * 查询订单主（整合地址与定位信息）列表
     * 
     * @param orderMain 订单主（整合地址与定位信息）
     * @return 订单主（整合地址与定位信息）
     */
    @Override
    public List<OrderMain> selectOrderMainList(OrderMain orderMain)
    {
        return orderMainMapper.selectOrderMainList(orderMain);
    }

    /**
     * 新增订单主（整合地址与定位信息）
     * 
     * @param orderMain 订单主（整合地址与定位信息）
     * @return 结果
     */
    @Override
    public int insertOrderMain(OrderMain orderMain)
    {
        orderMain.setCreateTime(DateUtils.getNowDate());
        return orderMainMapper.insertOrderMain(orderMain);
    }

    /**
     * 修改订单主（整合地址与定位信息）
     * 
     * @param orderMain 订单主（整合地址与定位信息）
     * @return 结果
     */
    @Override
    public int updateOrderMain(OrderMain orderMain)
    {
        orderMain.setUpdateTime(DateUtils.getNowDate());
        return orderMainMapper.updateOrderMain(orderMain);
    }

    /**
     * 批量删除订单主（整合地址与定位信息）
     * 
     * @param orderMainIds 需要删除的订单主（整合地址与定位信息）主键
     * @return 结果
     */
    @Override
    public int deleteOrderMainByOrderMainIds(Long[] orderMainIds)
    {
        return orderMainMapper.deleteOrderMainByOrderMainIds(orderMainIds);
    }

    /**
     * 删除订单主（整合地址与定位信息）信息
     * 
     * @param orderMainId 订单主（整合地址与定位信息）主键
     * @return 结果
     */
    @Override
    public int deleteOrderMainByOrderMainId(Long orderMainId)
    {
        return orderMainMapper.deleteOrderMainByOrderMainId(orderMainId);
    }
}
