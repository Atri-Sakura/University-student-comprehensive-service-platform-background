package com.gzu.service;


import com.gzu.order.entity.Order;

public interface OrderService {
    Order createOrder(Long userId, Long productId);
}
