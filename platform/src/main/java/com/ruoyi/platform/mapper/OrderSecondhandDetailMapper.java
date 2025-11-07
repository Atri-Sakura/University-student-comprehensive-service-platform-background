package com.ruoyi.platform.mapper;

import java.util.List;
import com.ruoyi.platform.domain.OrderSecondhandDetail;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 二手交易订单明细（不含地址信息）Mapper接口
 * 
 * @author ruoyi
 * @date 2025-10-20
 */
public interface OrderSecondhandDetailMapper 
{
    /**
     * 根据订单主表ID查询对应的二手商品ID
     */
    Long selectGoodsIdByOrderMainId(@Param("orderMainId") Long orderMainId);
    /**
     * 查询二手交易订单明细（不含地址信息）
     * 
     * @param orderSecondhandDetailId 二手交易订单明细（不含地址信息）主键
     * @return 二手交易订单明细（不含地址信息）
     */
    public OrderSecondhandDetail selectOrderSecondhandDetailByOrderSecondhandDetailId(Long orderSecondhandDetailId);

    /**
     * 查询二手交易订单明细（不含地址信息）列表
     * 
     * @param orderSecondhandDetail 二手交易订单明细（不含地址信息）
     * @return 二手交易订单明细（不含地址信息）集合
     */
    public List<OrderSecondhandDetail> selectOrderSecondhandDetailList(OrderSecondhandDetail orderSecondhandDetail);

    /**
     * 新增二手交易订单明细（不含地址信息）
     * 
     * @param orderSecondhandDetail 二手交易订单明细（不含地址信息）
     * @return 结果
     */
    public int insertOrderSecondhandDetail(OrderSecondhandDetail orderSecondhandDetail);

    /**
     * 修改二手交易订单明细（不含地址信息）
     * 
     * @param orderSecondhandDetail 二手交易订单明细（不含地址信息）
     * @return 结果
     */
    public int updateOrderSecondhandDetail(OrderSecondhandDetail orderSecondhandDetail);

    /**
     * 删除二手交易订单明细（不含地址信息）
     * 
     * @param orderSecondhandDetailId 二手交易订单明细（不含地址信息）主键
     * @return 结果
     */
    public int deleteOrderSecondhandDetailByOrderSecondhandDetailId(Long orderSecondhandDetailId);

    /**
     * 批量删除二手交易订单明细（不含地址信息）
     * 
     * @param orderSecondhandDetailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderSecondhandDetailByOrderSecondhandDetailIds(Long[] orderSecondhandDetailIds);
}
