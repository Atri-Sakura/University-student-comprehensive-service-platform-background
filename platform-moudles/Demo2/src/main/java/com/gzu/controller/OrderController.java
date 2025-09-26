package com.gzu.controller;

import com.gzu.order.entity.Order;
import com.gzu.properties.OrderProperties;
import com.gzu.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AUTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
//@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
//    @Value("${test2.timeout}")
//    private String timeout;
//    @Value("${test2.auto-confirm}")
//    private String autoConfirm;

    @Autowired
    private OrderProperties orderProperties;
    @GetMapping("/config")
    public String config(){
        return "Timeout: " + orderProperties.getTimeout()+"auto-confirm: " + orderProperties.getAutoConfirm();
    }
    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId")Long userId, @RequestParam("productId") Long productId) {
        return orderService.createOrder(userId, productId);
    }

    @GetMapping("/seckill")
    public Order secKill(@RequestParam("userId")Long userId, @RequestParam("productId") Long productId) {
        Order order = orderService.createOrder(userId, productId);
        order.setId(Long.MAX_VALUE);
        return order;
    }

    @GetMapping("/readDB")
    public String readDB(){
        log.info("readDB");
        return "readDB success";
    }

    @GetMapping("/writeDB")
    public String writeDB(){

        return "writeDB success";
    }

}
