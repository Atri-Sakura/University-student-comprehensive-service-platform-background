package com.ruoyi.platform.service;

import com.ruoyi.platform.domain.OrderMain;
import com.ruoyi.platform.domain.dto.SecondhandOrderCreatDTO;
import com.ruoyi.platform.domain.vo.SecondhandOrderContactDetailVO;

import java.util.List;

/**
 * 二手商品订单Service接口
 *
 * @author ruoyi
 * @date 2025-10-20
 */
public interface ISecondhandOrderService {
    String createSecondhandOrder(SecondhandOrderCreatDTO dto);

    boolean payOrder(String orderNo);

    boolean confirmOrder(String orderNo);
    /**
     * 根据订单号查询二手订单详情（含对方联系方式 + 商品信息）
     */
    SecondhandOrderContactDetailVO getSecondhandOrderDetail(String orderNo, Long currentUserBaseId);

    List<OrderMain> getSecondHandOrderList(Long currentUserBaseId);

}
