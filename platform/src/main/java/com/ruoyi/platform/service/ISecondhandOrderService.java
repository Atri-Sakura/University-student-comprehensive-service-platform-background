package com.ruoyi.platform.service;

import com.ruoyi.platform.domain.dto.SecondhandOrderCreatDTO;

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
}
