package com.ruoyi.platform.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.platform.domain.OrderDelivery;
import com.ruoyi.platform.domain.OrderMain;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 订单主（整合地址与定位信息）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface OrderMainMapper {

    /**
     * 根据订单号查询订单主表
     */
    OrderMain selectByOrderNo(@Param("orderNo") String orderNo);
    /**
     * 插入二手交易订单
     */
    int insertSecondhandOrderMain(OrderMain orderMain);

    /**
     * 修改订单完成时间
     * @param orderNo
     * @param completeTime
     * @return
     */
    int updateOrderComplete(@Param("orderNo") String orderNo,
                            @Param("completeTime") LocalDateTime completeTime);

    /**
     * 根据订单编号查询订单信息
     * @param orderNo
     * @return
     */
    OrderMain selectOrderByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 修改订单支付状态
     * @param orderNo
     * @param payStatus
     * @param orderStatus
     * @param payTime
     * @return
     */
    int updatePayStatus(@Param("orderNo") String orderNo,
                        @Param("payStatus") Long payStatus,
                        @Param("orderStatus") Long orderStatus,
                        @Param("payTime") LocalDateTime payTime);

    /**
     * 修改订单状态
     * @param orderNo
     * @param orderStatus
     * @param payStatus
     * @param cancelReason
     * @return
     */
    int updateOrderStatus(@Param("orderNo") String orderNo,
                          @Param("orderStatus") Long orderStatus,
                          @Param("payStatus") Long payStatus,
                          @Param("cancelReason") String cancelReason);
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

    public OrderMain selectOrderMainWithDetailsByOrderNo(String orderNo);

    /**
     * 根据订单ID查询跑腿订单详情（包含明细和骑手信息）
     * @param orderNo 订单ID
     * @return 跑腿订单信息
     */
    OrderMain selectErrandOrderMainWithDetailsByOrderNo(String orderNo);

    /**
     * 查询跑腿订单列表（包含明细和骑手信息）
     * @param orderMain 跑腿订单信息
     * @return 跑腿订单集合
     */
    List<OrderMain> selectErrandOrderMainListWithDetails(OrderMain orderMain);

    /**
     * 根据订单ID查询二手订单订单详情（包含明细和骑手信息）
     * @param orderNo 订单ID
     * @return 跑腿订单信息
     */
    OrderMain selectSecondHandOrderMainWithDetailsByOrderNo(String orderNo);



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

    /**
     * 查询商家今日订单收入
     */
    Map<String, Object> selectMerchantTodayIncome(@Param("merchantId") Long merchantId,
                                                  @Param("startTime") Date startTime,
                                                  @Param("endTime") Date endTime);

    /**
     * 查询商家今日退款金额
     */
    BigDecimal selectMerchantTodayRefund(@Param("merchantId") Long merchantId,
                                         @Param("startTime") Date startTime,
                                         @Param("endTime") Date endTime);

    /**
     * 根据订单ID查询配送信息
     * @param orderMainId 订单ID
     * @return 配送信息
     */
    OrderDelivery selectOrderDeliveryByOrderMainId(Long orderMainId);
}
