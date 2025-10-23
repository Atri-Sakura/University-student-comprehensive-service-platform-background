package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.OrderMain;

/**
 * 订单主（整合地址与定位信息）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface OrderMainMapper 
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
     * 删除订单主（整合地址与定位信息）
     * 
     * @param orderMainId 订单主（整合地址与定位信息）主键
     * @return 结果
     */
    public int deleteOrderMainByOrderMainId(Long orderMainId);

    /**
     * 批量删除订单主（整合地址与定位信息）
     * 
     * @param orderMainIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderMainByOrderMainIds(Long[] orderMainIds);
}
