package com.ruoyi.platform.merchant.service;

public interface IOrderService {
    void acceptOrder(Long orderNoId);

    void acceptOrderBatch(Long[] orderNoIds);
}
