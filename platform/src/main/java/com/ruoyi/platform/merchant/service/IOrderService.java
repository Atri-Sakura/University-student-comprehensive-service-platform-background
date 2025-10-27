package com.ruoyi.platform.merchant.service;

import com.ruoyi.platform.merchant.vo.MerchantOrderStatusVO;

public interface IOrderService {
    void acceptOrder(Long orderNoId);

    void acceptOrderBatch(Long[] orderNoIds);

    MerchantOrderStatusVO getMerchantOrderStatus(Long MerchantBaseId);
}
